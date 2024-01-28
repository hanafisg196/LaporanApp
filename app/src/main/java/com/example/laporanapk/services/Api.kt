package com.example.laporanapk.services

import com.example.laporanapk.model.ResponseCreateData
import com.example.laporanapk.model.ResponseDataByDate
import com.example.laporanapk.model.ResponseGetData
import com.example.laporanapk.model.UserResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query



interface Api {

      @GET("kegiatan/latest")
      fun getDataLatest(
          @Header("Authorization") tokenAuth : String?,
          @Query("page") page : Int
      ): Call<ResponseGetData>


       @POST("users/login")
       fun login(
           @Query("username") username : String,
           @Query("password") password : String
       ) : Call<UserResponse>

       @FormUrlEncoded
       @POST("kegiatan")
       fun createData(
           @Header("Authorization") tokenAuth : String?,
           @Field("nagari_kunjungan") nagari:String?,
           @Field("kegiatan") kegiatan:String?,
           @Field("hasil") hasil:String?,
           @Field("langkah") langkah:String?,
           @Field("rekomendasi") rekomendasi:String?
       ):Call<ResponseCreateData>


    @GET("kegiatan/filterBydate")
    fun getDataByDate(
        @Header("Authorization") tokenAuth : String?,
        @Query("start_date") startDate : String?,
        @Query("end_date") endDate : String?,
    ): Call<ResponseDataByDate>




}