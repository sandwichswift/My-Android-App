package com.example.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentEditScheduleBinding
import com.example.myapplication.db.Schedule
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*

class EditScheduleFragment : Fragment() {
    private var _binding: FragmentEditScheduleBinding? = null
    private val binding get() = _binding!!

    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var btnSave: Button
    private lateinit var btnDelete: Button
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigate(R.id.action_editScheduleFragment_to_navigation_home)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentEditScheduleBinding.inflate(inflater, container, false)
        val root : View = binding.root


        titleEditText = binding.titleEditText
        contentEditText = binding.contentEditText
        dateEditText = binding.dateEditText
        btnSave = binding.btnSave
        btnDelete = binding.btnDelete

        val scheduleid = arguments?.getLong("id")
        titleEditText.setText(arguments?.getString("title"))
        contentEditText.setText(arguments?.getString("detail"))
        dateEditText.setText(arguments?.getString("time"))


        btnSave.setOnClickListener {
            // 获取输入框中的数据
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()
            val date = dateEditText.text.toString()



            // 使用 HomeViewModel 的 addDataToDb 方法将数据保存到数据库
            lifecycleScope.launch {
                if(scheduleid != null){
                    // 创建一个 Schedule 对象
                    val createTime = LocalDate.now().toString()
                    val schedule = Schedule(scheduleid,title = title, time = date, createTime = createTime,description = content)
                    homeViewModel.update(schedule)
                }
                else {
                    // 创建一个 Schedule 对象
                    val id = System.currentTimeMillis() // 获取当前时间戳作为 id
                    val createTime = LocalDate.now().toString()
                    val schedule = Schedule(id, title = title, time = date, createTime = createTime,description = content)
                    homeViewModel.addDataToDb(schedule)
                }
            }

            //返回
            findNavController().navigate(R.id.navigation_home)
        }

        btnDelete.setOnClickListener {
            val id = arguments?.getLong("id")
            if (id != null) {
                val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("确认删除")
                    .setMessage("你确定要删除这个计划吗？")
                    .setPositiveButton("确定") { _, _ ->
                        lifecycleScope.launch {
                            homeViewModel.delete(id)
                        }
                        findNavController().navigate(R.id.navigation_home)
                    }
                    .setNegativeButton("取消", null)
                    .create()
                dialog.show()
            }
        }
        dateEditText.setOnClickListener {
            val datePicker = DatePicker(requireContext())
            val calendar = Calendar.getInstance()
            datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null)
            val dialog = androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setView(datePicker)
                .setPositiveButton("OK") { _, _ ->
                    val year = datePicker.year
                    val month = datePicker.month
                    val day = datePicker.dayOfMonth
                    val formattedDate = String.format("%04d-%02d-%02d", year, month+1, day)//标准化日期格式
                    dateEditText.setText(formattedDate)
                }
                .setNegativeButton("Cancel", null)
                .create()
            dialog.show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}