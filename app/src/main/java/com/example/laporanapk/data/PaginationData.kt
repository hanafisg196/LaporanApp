package com.example.laporanapk.data

import com.google.gson.annotations.SerializedName

data class PaginationData(
    @SerializedName("current_page")
    val page: Int,
    val from: Int,
    @SerializedName("last_page")
    val lastPage: Int,
    val path: String,
    @SerializedName("per_page")
    val perPage: Int,
    val to: Int,
    val total: Int
)
