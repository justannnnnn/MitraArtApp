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
        /*val request = Request.Builder()
            .url("https://parser-api.com/parser/passport_api/?key=1234&passport_series=6017&passport_number=277920&first_name=Анна&last_name=Кирицева")
            .build()
        var res : String? = null

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                res = response.body?.string()
            }
        })
        return res != "nothing"*/

    }

}