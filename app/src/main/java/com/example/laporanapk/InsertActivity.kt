package com.example.laporanapk


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.example.laporanapk.databinding.ActivityInsertBinding
import com.example.laporanapk.model.ResponseCreateData
import com.example.laporanapk.pref.PrefManager
import com.example.laporanapk.utils.RetrofitClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InsertActivity : AppCompatActivity() {

    private lateinit var binding : ActivityInsertBinding
    private lateinit var pref : PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        pref = PrefManager(this)

        val toolbar = binding.toolbar
        
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

       binding.btnSave.setOnClickListener {
                saveData()
       }
    }

    override fun onSupportNavigateUp(): Boolean {
       onBackPressed()

        return true
    }




    private fun saveData() {
        with(binding)
        {
            val nagari = binding.edtNagari.text.toString()
            val kegiatan = binding.edtKegiatan.text.toString()
            val hasil = binding.edtHasil.text.toString()
            val langkah = binding.edtLangkah.text.toString()
            val rekomendasi = binding.edtRekomendasi.text.toString()

            val  tokenAuth = pref.getToken()

            RetrofitClient.instances.createData(tokenAuth,nagari,kegiatan,hasil,langkah,rekomendasi).enqueue(object : Callback<ResponseCreateData>{
                override fun onResponse(
                    call: Call<ResponseCreateData>,
                    response: Response<ResponseCreateData>
                ) {
                    if (response.isSuccessful)
                    {
                        Toast.makeText(applicationContext,"Data Tersimpan", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        //handler validation from api
                        val jsonObject = JSONObject(response.errorBody()!!.charStream().readText())
                        val nagariError = jsonObject.getJSONObject("errors").
                        optJSONArray("nagari_kunjungan")?.getString(0)
                        val kegiatanError = jsonObject.getJSONObject("errors").
                        optJSONArray("kegiatan")?.getString(0)
                        val hasilError = jsonObject.getJSONObject("errors").
                        optJSONArray("hasil")?.getString(0)
                        val langkahError = jsonObject.getJSONObject("errors").
                        optJSONArray("langkah")?.getString(0)
                        val rekomendasiError = jsonObject.getJSONObject("errors").
                        optJSONArray("rekomendasi")?.getString(0)

                        edtNagari.error = nagariError
                        edtKegiatan.error = kegiatanError
                        edtHasil.error = hasilError
                        edtLangkah.error = langkahError
                        edtRekomendasi.error = rekomendasiError

                    }

                }

                override fun onFailure(call: Call<ResponseCreateData>, t: Throwable) {

                }

            })
        }

    }





}