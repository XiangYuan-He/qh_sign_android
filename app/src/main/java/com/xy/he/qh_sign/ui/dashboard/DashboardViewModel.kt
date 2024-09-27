package com.xy.he.qh_sign.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xy.he.qh_sign.http.ChangeEntrySignStatsReq
import com.xy.he.qh_sign.http.ChangeSubscribeStatsReq
import com.xy.he.qh_sign.http.EntrySignItem
import com.xy.he.qh_sign.http.QHHttp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardViewModel : ViewModel() {

    private val _list = MutableLiveData<List<EntrySignItem>?>()
    val list: LiveData<List<EntrySignItem>?> = _list

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getSignList() {
        viewModelScope.launch {
            try {
                val resp = withContext(Dispatchers.IO) {
                    QHHttp.service.getRecentEntrySignSymbols()
                }

                if (resp?.code == 0L) {
                    with(resp.data) {
                        _list.value = this
                    }
                } else {
                    _error.value = "获取数据失败"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }

        }

    }


    suspend fun changeSubscribeStatus(
        id: String,
        trade_result: String,  // 保本 止损 止盈 未交易
        trade_plr: Double?,// 盈亏比
    ): Boolean {
        val resp = QHHttp.service.setRecentEntrySignSymbols(
            ChangeEntrySignStatsReq(
                _id = id,
                trade_result = trade_result,
                trade_plr = trade_plr.toString(),
            )
        )
        return resp?.code == 0L && resp.data
    }
}