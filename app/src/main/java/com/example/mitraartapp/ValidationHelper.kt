package com.example.mitraartapp

import android.text.Editable
import android.text.TextUtils

class ValidationHelper {
    fun isText(data: Editable?) : Boolean?{
        return data?.all{it.isLetter()}
    }

    fun isEmail(data: Editable?) : Boolean?{
        return !TextUtils.isEmpty(data) && android.util.Patterns.EMAIL_ADDRESS.matcher(data).matches()
    }

    fun isPhone(data: Editable?) : Boolean?{
        return !TextUtils.isEmpty(data) && android.util.Patterns.PHONE.matcher(data).matches() && (data?.count{it.isDigit()}!! in 6..11)
    }

}