package com.example.laporanapk.model


import com.google.gson.annotations.SerializedName

data class ResponseCreateData (
    @SerializedName("errors") val err: String,
    @SerializedName("message") val msg: String
)