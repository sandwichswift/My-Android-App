package com.example.myapplication.ui.aitool

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.RecylerView.RecognitionAdapter
import com.example.myapplication.RecylerView.RecognitionRes
import com.example.myapplication.databinding.FragmentObjectRecognitionBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

val PICK_IMAGE_REQUEST_CODE = 1
val PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2


class ObjectRecognitionFragment : Fragment() {
    private var _binding: FragmentObjectRecognitionBinding? = null
    private val binding get() = _binding!!

    lateinit var recognitionRecylerView: RecyclerView
    lateinit var recognitionAdapter: RecognitionAdapter
    lateinit var btnChooseImg: Button
    lateinit var img : ImageView

    private val aitoolViewModel:AitoolViewModel by activityViewModels()//获取父activity的ViewModel
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentObjectRecognitionBinding.inflate(inflater, container, false)
        val root: View = binding.root
        btnChooseImg = binding.btnChooseImg
        recognitionRecylerView = binding.recognitionRecyclerView
        recognitionRecylerView.layoutManager = LinearLayoutManager(requireContext())
        img = binding.img
        btnChooseImg.setOnClickListener {
            openGallery()
        }

        return root
    }

    private fun openGallery() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestReadExternalStoragePermission()
            //openGallery()//请求权限后再次调用openGallery
        } else {//已经有权限
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            val selectedImageUri = data.data // 获取用户选择的图片的 Uri
            aitoolViewModel.setImgUri(selectedImageUri.toString()) // 将 Uri 保存到 ViewModel 中
            aitoolViewModel.convertImageUriToBase64(requireActivity().contentResolver,selectedImageUri.toString()) // 将 Uri 转换为 Base64 字符串

            // 在协程中调用 generalRecognitionFromBase64 方法
            lifecycleScope.launch(Dispatchers.IO) {
                val base64String = aitoolViewModel.imgBase64String.value ?: return@launch
                if(base64String.length > 4*1024*1024){
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "图片过大，请重新选择", Toast.LENGTH_SHORT).show()
                    }
                    return@launch//图片过大直接返回
                }
                val result = ObjRecognition.generalRecognitionFromBase64(base64String)
                withContext(Dispatchers.Main) {//切换到主线程
                    aitoolViewModel.setRecognitionResult(result) // 将识别结果保存到 ViewModel 中
                }
            }

            img.setImageURI(selectedImageUri) // 将图片显示在 ImageView 中

            // 观察识别结果的变化
            aitoolViewModel.recognitionResult.observe(viewLifecycleOwner) {
                val result = parseJson(it)
                if(result.isEmpty()){
                    Toast.makeText(requireContext(), "识别失败", Toast.LENGTH_SHORT).show()
                    return@observe
                }
                val data = result.map { RecognitionRes(it.first, it.second, it.third) }
                log("recognitionResult: $data")
                recognitionAdapter = RecognitionAdapter(data)
                recognitionRecylerView.adapter = recognitionAdapter//设置适配器
            }
        }
    }

    private fun requestReadExternalStoragePermission() {
        requestPermissions(
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigate(R.id.action_objectRecognitionFragment_to_navigation_aitool)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun parseJson(jsonString: String): List<Triple<String, String,Double>>{
        val result = mutableListOf<Triple<String, String,Double>>()
        val jsonObject = JSONObject(jsonString)
        try{
            val resultArray: JSONArray = jsonObject.getJSONArray("result")

            for (i in 0 until resultArray.length()) {
                val itemObject = resultArray.getJSONObject(i)
                val root = itemObject.getString("root")
                val keyword = itemObject.getString("keyword")
                val score = itemObject.getDouble("score")
                result.add(Triple(root, keyword, score))
            }
        }catch (e: Exception){
            log("parseJson error: $e")
            Toast.makeText(requireContext(), "识别失败", Toast.LENGTH_SHORT).show()
        }
        return result
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // 权限被用户授予，你可以在这里进行你的操作
                    openGallery()
                } else {
                    // 权限被用户拒绝，在这里处理权限被拒绝的情况
                    if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // 用户拒绝了权限请求，但没有选择“不再询问”，所以我们可以再次请求权限
                        requestReadExternalStoragePermission()
                    } else {
                        // 用户拒绝了权限请求，并选择了“不再询问”，我们可以显示一个解释的对话框
                        Toast.makeText(requireContext(), "我们需要读取外部存储的权限来选择图片", Toast.LENGTH_SHORT).show()
                        openSettings()
                    }
                }
                return
            }
            // 其他的权限请求，可以在这里处理
            else -> {
                // Ignore all other requests.
            }
        }
    }
    fun openSettings(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext());
        builder.setTitle("开启位置服务");
        builder.setMessage("本应用需要开启存储服务，是否去设置界面开启存储服务？");
        builder.setPositiveButton("是"){dialog, which ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", requireActivity().packageName, null)
            intent.data = uri
            startActivity(intent)
        }
        builder.setNegativeButton("否"){dialog, which ->
            Toast.makeText(requireContext(),"无法访问存储,请开启定位权限", Toast.LENGTH_SHORT).show()
        }
        builder.show();
    }
}