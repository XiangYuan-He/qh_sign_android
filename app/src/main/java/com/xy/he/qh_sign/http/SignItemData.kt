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