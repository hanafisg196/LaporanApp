package com.example.laporanapk.model

import com.example.laporanapk.data.DataReport
import com.example.laporanapk.data.PaginationData
import com.google.gson.annotations.SerializedName

data class ResponseGetData (

    @SerializedName("errors") val err : String,
    @SerializedName("message") val msg : String,

val page : Int,
val data : List<DataReport>,
val meta: PaginationData


)