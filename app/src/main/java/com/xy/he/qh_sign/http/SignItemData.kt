package com.xy.he.qh_sign.http


data class SignListResp(
    val code: Long,
    val data: List<SignItemData>
)


data class BoolResp(
    val code: Long,
    val data: Boolean
)


data class ChangeSubscribeStatsReq(
    val is_subscribe: Boolean,
    val _id: String,
)


data class SignItemData(
    val _id: String,
    val datetime: String,
    val direction: String,
    val exchange: String,
    val high: Double,
    val is_subscribe: Boolean,
    val low: Double,
    val period_label: String,
    val symbol: String,
    val symbol_label: String
)


class ListResp<T>(
    val code: Long,
    val data: List<T>?
)

// 入场信号配置i/*
//
//  {
//            "_id": "66f649aeae6c6d801b30fa2d",
//            "createdAt": {
//                "$date": "2024-09-27T05:59:10.941Z"
//            },
//            "datatime": 1.72741668e+18,
//            "datatime_dt": "2024-09-27 13:58:00",
//            "direction": "l",
//            "mark": "回撤50%后出现连续三个阳线",
//            "period": "1M",
//            "price": 14150.0,
//            "symbol": "NR0",
//            "symbol_name": "20号胶连续",
//            "symbol_tq": "INE.nr2411"
//        },
// */
data class EntrySignItem(
    val _id: String,
    val datatime_dt: String?,
    val direction: String?, // l 多 s 空
    val mark: String?,
    val period: String?,
    val price: Double?,
    val symbol: String?,
    val symbol_name: String?,
    val symbol_tq: String?,
    val trade_result: String?,
    val trade_plr: String?,
)

data class ChangeEntrySignStatsReq(
    val _id: String,
    val trade_result: String?,  // 保本 止损 止盈 未交易
    val trade_plr: String?,
)