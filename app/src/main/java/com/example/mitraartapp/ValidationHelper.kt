package com.example.mitraartapp

import android.app.DownloadManager
import android.text.Editable
import android.text.TextUtils
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class ValidationHelper {

    private val client = OkHttpClient()
    fun isText(data: Editable?) : Boolean?{
        return data?.all{it.isLetter()}
    }

    fun isEmail(data: Editable?) : Boolean?{
        return !TextUtils.isEmpty(data) && android.util.Patterns.EMAIL_ADDRESS.matcher(data).matches()
    }

    fun isPhone(data: Editable?) : Boolean?{
        return !TextUtils.isEmpty(data) && android.util.Patterns.PHONE.matcher(data).matches() && (data?.count{it.isDigit()}!! in 6..11)
    }

    fun rightPassport(data: Editable) : Boolean?{
        return data.length == 10 && data.all { it.isDigit() }
    }

    fun rightINN(data: Editable) : Boolean?{
        return data.length == 12 && data.all { it.isDigit() }
    }

    fun rightOGRN(data: Editable) : Boolean?{
        return data.length == 13 && data.all { it.isDigit() }
    }

}