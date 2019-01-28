package com.github.zcapt

import android.util.Base64
import com.github.zcapt.model.ZcaptData
import com.google.gson.Gson

class Zcapt {

    companion object {
        lateinit var zcaptData: ZcaptData

        fun init(base64code: String) {
            val json = Base64.decode(base64code.toByteArray(Charsets.UTF_8), Base64.DEFAULT)
            zcaptData = Gson().fromJson(String(json), ZcaptData::class.java)
        }

    }


}