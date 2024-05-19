package com.example.myapplication.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    lateinit var db: ScheduleDatabase
    private lateinit var homeViewModel: HomeViewModel
    lateinit var adapter: ScheduleDataAdapter
    lateinit var dao : ScheduleDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        scheduleRecyclerView = binding.scheduleRecyclerView
        scheduleRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        btnAddData = binding.btnAdd
        btnClrAll = binding.btnClrAll
        db = ScheduleDatabase.getDatabase(requireContext())
        dao = db.scheduleDao()

        val data = homeViewModel.scheduleList
        data.observe(viewLifecycleOwner) {
            adapter = ScheduleDataAdapter(it, homeViewModel)
            scheduleRecyclerView.adapter = adapter
        }

        btnAddData.setOnClickListener {
            lifecycleScope.launch {
                homeViewModel.addDataToDb()
            }
            //滚动到底部
            scheduleRecyclerView.scrollToPosition(adapter.itemCount - 1)
        }
        btnClrAll.setOnClickListener {
            lifecycleScope.launch {
                homeViewModel.clearAllData()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}