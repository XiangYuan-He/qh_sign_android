package com.xy.he.qh_sign.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.xy.he.qh_sign.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private val homeViewModel:HomeViewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())


        binding.rvMain.setLayoutManager(layoutManager)


        val rvAdapter = SignHoursAdapter(emptyList())

        rvAdapter.onItemLongClickListener = {
            lifecycleScope.launch {
                val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                builder.setTitle("订阅管理")
                builder.setMessage(
                    if (it.is_subscribe) {
                        "您确定要取消 1 分钟信号订阅吗？"
                    } else {
                        "您确定要进行 1 分钟信号订阅吗？"
                    }
                )

                builder.setPositiveButton(
                    "确认"
                ) { dialog, which ->
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            Log.i("AAAA", it._id)
                            homeViewModel.changeSubscribeStatus(it._id, !it.is_subscribe)
                        }
                        dialog.dismiss()
                        homeViewModel.getSignList()
                    }
                }

                builder.show()
            }
        }

        binding.rvMain.adapter = rvAdapter

        homeViewModel.list.observe(viewLifecycleOwner) {
            rvAdapter.list = it
            binding.swipeRefreshLayout.isRefreshing = false
            rvAdapter.notifyDataSetChanged()
        }

        homeViewModel.error.observe(viewLifecycleOwner) {
            binding.swipeRefreshLayout.isRefreshing = false
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        binding.swipeRefreshLayout.setOnRefreshListener(OnRefreshListener { // 在这里执行数据刷新操作
            homeViewModel.getSignList()
        })



        return root
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.getSignList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}