package com.example.laporanapk


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.laporanapk.databinding.ActivityLoginBinding
import com.example.laporanapk.model.UserResponse
import com.example.laporanapk.pref.PrefManager
import com.example.laporanapk.utils.RetrofitClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var pref : PrefManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        pref = PrefManager(this)

        binding.buttonLogin.setOnClickListener {
            doLogin()
        }

    }

    private fun doLogin()
    {
        val username = binding.edtUsername.text.toString()
        val password = binding.edtPassword.text.toString()

        if (username.isEmpty())
        {
            binding.edtUsername.error = "Username Tidak Boleh Kosong"
            binding.edtUsername.requestFocus()
        }else if (password.isEmpty())
        {
            binding.edtPassword.error = "Password Tidak Boleh Kosong"
            binding.edtPassword.requestFocus()
        }else {

            RetrofitClient.instances.login(username, password)
                .enqueue(object  : Callback<UserResponse> {
                    override fun onResponse(
                        call: Call<UserResponse>,
                        response: Response<UserResponse>
                    ) {
                        if (response.isSuccessful) {

                            pref.setToken(response.body()?.data?.token)
                            pref.setEmail(response.body()?.data?.email)
                            pref.setNama(response.body()?.data?.nama)

                            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                            finish()


                        }else {
                            val jsonObject = JSONObject(response.errorBody()!!.charStream().readText())
                            val messageError = JSONObject(jsonObject.getString("errors"))
                            if (messageError.getString("message") != "")
                            {
                                binding.edtUsername.error = messageError.getString("message")
                                binding.edtPassword.error = ""
                                binding.edtUsername.requestFocus()
                            }

                        }
                    }

                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {

                    }

                })

        }


    }





}












