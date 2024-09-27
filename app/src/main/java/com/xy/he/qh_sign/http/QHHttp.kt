package com.xy.he.qh_sign.http

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

object QHHttp {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://49.235.157.216/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: QHService = retrofit.create(QHService::class.java)
}


interface QHService {
    @GET("qh/subscribe-symbols")
    suspend fun getRecentSubscribeSymbols(): SignListResp?

    @POST("qh/subscribe-symbols-set")
    suspend fun changeSubscribeSymbolsStatus(@Body req: ChangeSubscribeStatsReq): BoolResp?


    @GET("qh/get-subscribe-entry")
    suspend fun getRecentEntrySignSymbols(): ListResp<EntrySignItem>?


    @POST("qh/set-subscribe-entry")
    suspend fun setRecentEntrySignSymbols(@Body req: ChangeEntrySignStatsReq): BoolResp?


}