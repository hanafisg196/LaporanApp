package com.example.laporanapk.pref

import android.content.Context
import android.content.SharedPreferences

class PrefManager ( context: Context?){

    private val PRIVATE_MODE = 0

    private val PREF_NAME = "SharedPreferences"
    private val IS_LOGIN = "is_login"

    private var pref: SharedPreferences? = context?.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
    private var editor: SharedPreferences.Editor? = pref?.edit()

    fun setLogin(islogin: Boolean)
    {
        editor?.putBoolean(IS_LOGIN, islogin)
        editor?.commit()
    }

    fun setToken(token: String?)
    {
        editor?.putString("token", token)
        editor?.commit()
    }

    fun setEmail(email: String?)
    {
        editor?.putString("email", email)
        editor?.commit()
    }

    fun setNama(nama: String?)
    {
        editor?.putString("nama", nama)
        editor?.commit()
    }


    fun isLogin() : Boolean? {
        return pref?.getBoolean(IS_LOGIN, false)
    }

    fun getToken(): String? {
        return pref?.getString("token", "")
    }

    fun getEmail(): String? {
        return pref?.getString("email", "")
    }
    fun getNama(): String? {
        return pref?.getString("nama", "")
    }

    fun removeData() {
        editor?.clear()
        editor?.commit()
    }
}