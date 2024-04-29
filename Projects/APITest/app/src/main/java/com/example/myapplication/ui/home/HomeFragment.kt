package com.example.myapplication.ui.home

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.log
import kotlin.coroutines.*

private const val PICK_IMAGE_REQUEST_CODE = 1
private const val PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2
private const val PERMISSIONS_REQUEST_INTERNET = 3
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    lateinit var textView : TextView
    lateinit var tvRecognition: TextView
    lateinit var imgView : ImageView
    lateinit var btnChooseImg : Button
    lateinit var btnRecognize : Button

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val homeViewModel : HomeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        imgView= binding.imgView
        textView = binding.tvRes
        btnChooseImg = binding.btnChooseImg
        btnRecognize = binding.btnRecognize
        tvRecognition = binding.tvRecognition

        btnChooseImg.setOnClickListener {
            homeViewModel.chooseImage()
        }

        homeViewModel.openGallery.observe(viewLifecycleOwner) {
            if (it) {
                //打开相册
                openGallery()
                //重置状态
                homeViewModel.resetOpenGallery()
            }
        }

        homeViewModel.imgURI.observe(viewLifecycleOwner) {//观察图片路径，当图片路径发生变化时，更新图片
            //检查读取存储权限
            requestReadExternalStoragePermission()

            textView.text = it
            val bitmap = getBitmapFromUri(Uri.parse(it))
            if(bitmap != null) {
                log("bitmap is not null")
                imgView.setImageBitmap(bitmap)
                textView.append("\n图片大小：${bitmap.width} x ${bitmap.height}")
            }
            else {
                log("bitmap is null")
                textView.append("\n图片加载失败")
            }
        }

        btnRecognize.setOnClickListener {
            //检查网络权限
            requestInternetPermission()
            val path = getPathFromURI(requireContext(), homeViewModel.imgURI.value ?: "")
            log("path: $path")
            homeViewModel.generalRecognition(Uri.parse(homeViewModel.imgURI.value?: ""))
        }

        homeViewModel.res.observe(viewLifecycleOwner) {
            tvRecognition.text = it
        }


        return root
    }
    fun getPathFromURI(context: Context, contentUri: String): String? {
        var cursor: Cursor? = null
        try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(Uri.parse(contentUri), proj, null, null, null)
            val column_index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor?.moveToFirst()
            return column_index?.let { cursor?.getString(it) }
        } finally {
            cursor?.close()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openGallery() {
        //打开相册
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //log("requestCode: $requestCode, resultCode: $resultCode")
        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            homeViewModel.setImageUri(uri.toString())
            log("uri: $uri")
        }
        else {
            homeViewModel.setImageUri("wrong path")
            log("未选择图片")
        }
    }
    private fun getBitmapFromUri(uri: Uri):Bitmap{
        return MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
    }

    private fun requestReadExternalStoragePermission() {
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
        }
        else {
            log("Read-external-storage Permission granted")
        }
    }
    private fun requestInternetPermission() {
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.INTERNET)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.INTERNET),
                PERMISSIONS_REQUEST_INTERNET)
        }
        else {
            log("Internet Permission granted")
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    log("Permission granted")
                }
                else {
                    log("Permission denied")
                }
            }
            PERMISSIONS_REQUEST_INTERNET -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    log("Permission granted")
                }
                else {
                    log("Permission denied")
                }
            }
        }
    }
}