package com.example.myapplication.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.RecylerView.ScheduleDataAdapter
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.db.DbHelper
import com.example.myapplication.db.ScheduleDao
import com.example.myapplication.db.ScheduleDatabase
import kotlinx.coroutines.launch

fun log(info: Any){
    Log.d("TodoHome", info.toString())
}

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var scheduleRecyclerView: RecyclerView
    private lateinit var btnAddData: Button
    private lateinit var btnClrAll: Button
    private lateinit var weathertv: TextView
    lateinit var db: ScheduleDatabase
    //private lateinit var homeViewModel: HomeViewModel
    private val homeViewModel: HomeViewModel by activityViewModels()
    lateinit var adapter: ScheduleDataAdapter
    lateinit var dao : ScheduleDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        scheduleRecyclerView = binding.scheduleRecyclerView
        scheduleRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        btnAddData = binding.btnAdd
        btnClrAll = binding.btnClrAll
        weathertv = binding.weathertv

        db = ScheduleDatabase.getDatabase(requireContext())
        dao = db.scheduleDao()

        val data = homeViewModel.scheduleList
        data.observe(viewLifecycleOwner) {
            adapter = ScheduleDataAdapter(it, homeViewModel)
            scheduleRecyclerView.adapter = adapter
        }

        val weather = homeViewModel.weatherInfo
        weather.observe(viewLifecycleOwner) {
            weathertv.text = it
            if(it.contains("晴")){
                val bg = resources.getDrawable(R.drawable.sunny)
                bg.alpha = 100//设置透明度0-255
                root.background = bg
            }
            else if(it.contains("雨")){
                val bg = resources.getDrawable(R.drawable.rainy)
                bg.alpha = 100//设置透明度0-255
                root.background = bg
            }
            else if(it.contains("雪")){
                val bg = resources.getDrawable(R.drawable.snowy)
                bg.alpha = 100//设置透明度0-255
                root.background = bg
            }
            else if(it.contains("阴") || it.contains("多云")){
                val bg = resources.getDrawable(R.drawable.cloudy)
                bg.alpha = 100//设置透明度0-255
                root.background = bg
            }
            else{
                val bg = resources.getDrawable(R.drawable.sunny)
                bg.alpha = 100//设置透明度0-255
                root.background = bg
            }
        }

        btnAddData.setOnClickListener {//打开另一个fragment
            lifecycleScope.launch {
                findNavController()
                    .navigate(R.id.action_navigation_home_to_editScheduleFragment)
            }
            //滚动到底部
            scheduleRecyclerView.scrollToPosition(adapter.itemCount - 1)
        }
        btnClrAll.setOnClickListener {
            lifecycleScope.launch {
                homeViewModel.clearAllData()
            }
        }

        weathertv.text = homeViewModel.weatherInfo.value

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}