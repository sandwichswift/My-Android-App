package com.example.myapplication.ui.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
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
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.log

private const val PICK_IMAGE_REQUEST_CODE = 1
private const val PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    lateinit var textView : TextView
    lateinit var imgView : ImageView
    lateinit var btnChooseImg : Button

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

        homeViewModel.imgURL.observe(viewLifecycleOwner) {//观察图片路径，当图片路径发生变化时，更新图片
            //图片路径
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted

                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
            }
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
}