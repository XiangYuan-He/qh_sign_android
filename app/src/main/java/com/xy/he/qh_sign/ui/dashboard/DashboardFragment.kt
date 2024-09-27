package com.xy.he.qh_sign.ui.dashboard

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.xy.he.qh_sign.R
import com.xy.he.qh_sign.databinding.FragmentDashboardBinding
import com.xy.he.qh_sign.http.EntrySignItem
import com.xy.he.qh_sign.http.SignItemData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val dashboardViewModel: DashboardViewModel by lazy {
        ViewModelProvider(this)[DashboardViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        val root: View = binding.root

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())


        binding.rvMain.setLayoutManager(layoutManager)


        val rvAdapter = EntrySignShortAdapter(emptyList())

        rvAdapter.onItemLongClickListener = {
            showChangeStatsBottomSheet(it)
        }

        binding.rvMain.adapter = rvAdapter

        dashboardViewModel.list.observe(viewLifecycleOwner) {
            rvAdapter.list = it ?: emptyList<EntrySignItem>()
            binding.swipeRefreshLayout.isRefreshing = false
            rvAdapter.notifyDataSetChanged()
        }

        dashboardViewModel.error.observe(viewLifecycleOwner) {
            binding.swipeRefreshLayout.isRefreshing = false
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        binding.swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener { // 在这里执行数据刷新操作
            dashboardViewModel.getSignList()
        })
        return root
    }


    fun showChangeStatsBottomSheet(item: EntrySignItem) {
        // 创建BottomSheetDialog.Builder对象
        var trade_stats = item.trade_result
        var trade_plr: String?
        val bottomSheetDialog = BottomSheetDialog(requireContext())

        // 为BottomSheetDialog设置布局
        val view = layoutInflater.inflate(R.layout.bottom_sheet_update_sign_entry, null)
        bottomSheetDialog.setContentView(view)
        val rgStatus = view.findViewById<RadioGroup>(R.id.rg_stats)
        val tvPlr = view.findViewById<TextView>(R.id.tv_plr)
        val etPlr = view.findViewById<EditText>(R.id.et_plr)
        val btnConfirm = view.findViewById<Button>(R.id.btn_confirm)
        btnConfirm.setOnClickListener {
            lifecycleScope.launch {
                trade_plr = etPlr.text.toString()
                if (trade_plr!!.isBlank()) {
                    trade_plr = null
                }
                if (rgStatus.checkedRadioButtonId == R.id.rb_1 && trade_plr == null) {
                    Toast.makeText(requireContext(), "请输入盈亏比", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                withContext(Dispatchers.IO) {
                    try {
                        dashboardViewModel.changeSubscribeStatus(
                            item._id,
                            trade_stats ?: "未设置",
                            trade_plr?.toDoubleOrNull()
                        )
                    } catch (e: Exception) {
                        println(e)
                    }
                }
                // 显示BottomSheetDialog
                bottomSheetDialog.dismiss()
                delay(200)
                dashboardViewModel.getSignList()
            }
        }

        rgStatus.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_1 -> {
                    trade_stats = "止盈"
                    trade_plr = null
                    tvPlr.visibility = View.VISIBLE
                    etPlr.visibility = View.VISIBLE
                }

                R.id.rb_2 -> {
                    trade_stats = "止损"
                    trade_plr = null
                    tvPlr.visibility = View.GONE
                    etPlr.visibility = View.GONE
                }

                R.id.rb_3 -> {
                    trade_stats = "未交易"
                    trade_plr = null
                    tvPlr.visibility = View.GONE
                    etPlr.visibility = View.GONE
                }
                R.id.rb_4 -> {
                    trade_stats = "保本"
                    trade_plr = null
                    tvPlr.visibility = View.GONE
                    etPlr.visibility = View.GONE
                }
            }
        }
        if (item.trade_result == "止盈") {
            rgStatus.check(R.id.rb_1)
        } else if (item.trade_result == "止损") {
            rgStatus.check(R.id.rb_2)
        } else if (item.trade_result == "保本"){
            rgStatus.check(R.id.rb_4)
        }else{
            rgStatus.check(R.id.rb_3)
        }


        // 显示BottomSheetDialog
        bottomSheetDialog.show()
    }

    override fun onResume() {
        super.onResume()
        dashboardViewModel.getSignList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}