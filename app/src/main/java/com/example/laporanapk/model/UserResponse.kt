package com.example.laporanapk.model


import com.example.laporanapk.data.UserData
import com.google.gson.annotations.SerializedName



 data class UserResponse (
     @SerializedName("data") val data: UserData?,
     @SerializedName("errors") val err: String,
     @SerializedName("message") val msg: String
)




