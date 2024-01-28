package com.example.laporanapk.data

import com.google.gson.annotations.SerializedName

data class DataReport (
    @SerializedName("nagari_kunjungan") val nkunjungan : String,
    @SerializedName("kegiatan") val kegiatan : String,
    @SerializedName("hasil") val hasil : String,
    @SerializedName("langkah") val langkah : String,
    @SerializedName("rekomendasi") val rekomendasi : String,
    @SerializedName("created_at") val tanggal : String,

    )



