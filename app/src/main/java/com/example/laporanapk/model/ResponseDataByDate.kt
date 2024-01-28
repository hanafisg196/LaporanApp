package com.example.laporanapk.model

import com.example.laporanapk.data.DataReport
import com.google.gson.annotations.SerializedName

data class ResponseDataByDate (
    @SerializedName("errors") val err : String,
    @SerializedName("message") val msg : String,

    @SerializedName("data") val data: List<DataReport>
)
