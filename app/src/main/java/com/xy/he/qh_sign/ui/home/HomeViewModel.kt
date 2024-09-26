package com.xy.he.qh_sign.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xy.he.qh_sign.http.ChangeSubscribeStatsReq
import com.xy.he.qh_sign.http.QHHttp
import com.xy.he.qh_sign.http.SignItemData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    private val _list = MutableLiveData<List<SignItemData>>()
    val list: LiveData<List<SignItemData>> = _list

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getSignList() {
        viewModelScope.launch {
            try {
                val resp = withContext(Dispatchers.IO) {
                    QHHttp.service.getRecentSubscribeSymbols()
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


    suspend fun changeSubscribeStatus(id: String, isSubscribe: Boolean): Boolean {
        val resp = QHHttp.service.changeSubscribeSymbolsStatus(
            ChangeSubscribeStatsReq(
                is_subscribe = isSubscribe,
                _id = id
            )
        )
        return resp?.code == 0L && resp.data
    }

}