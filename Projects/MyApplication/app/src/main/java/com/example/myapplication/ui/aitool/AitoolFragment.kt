package com.example.myapplication.ui.aitool

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAitoolBinding
val TAG = "AitoolFragment"
fun log(info:Any?){
    Log.d(TAG,info.toString())
}

class AitoolFragment : Fragment() {

    private var _binding: FragmentAitoolBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var btnObjRecognition : ImageButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(AitoolViewModel::class.java)

        _binding = FragmentAitoolBinding.inflate(inflater, container, false)
        val root: View = binding.root
        btnObjRecognition = binding.btnObjRecognition

        btnObjRecognition.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_aitool_to_objectRecognitionFragment)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}