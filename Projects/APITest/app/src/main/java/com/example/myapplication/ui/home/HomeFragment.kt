package com.example.myapplication.ui.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.log

private const val PICK_IMAGE_REQUEST_CODE = 1
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

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

        val textView: TextView = binding.tvRes
        homeViewModel.imgURL.observe(viewLifecycleOwner) {
            log("imgURL: $it")
            textView.text = it
        }
        val btnChooseImg: Button = binding.btnChooseImg
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
        homeViewModel.imgURL.observe(viewLifecycleOwner) {
            //图片路径
            textView.text = it
        }
        return root
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
            val path = getPathFromUri(uri!!)
            homeViewModel.setImagePath(path!!)
            log(path)
        }
        else {
            homeViewModel.setImagePath("wrong path")
            log("未选择图片")
        }
    }
    private fun getPathFromUri(uri: Uri): String {
        var res: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireActivity().contentResolver.query(uri, proj, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                res = cursor.getString(columnIndex)
            }
            cursor.close()
        }
        return res!!
    }
}