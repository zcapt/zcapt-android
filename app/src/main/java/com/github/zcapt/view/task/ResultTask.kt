package com.github.zcapt.view.task

import android.content.Context
import android.support.v4.content.AsyncTaskLoader
import android.util.Log
import com.github.zcapt.Zcapt
import com.github.zcapt.model.ResultData
import com.google.gson.Gson
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL

class ResultTask(
        context: Context,
        private var authID: String,
        private var x: String,
        private var y: String,
        private var onResultCallBack: OnResultCallBack
) : AsyncTaskLoader<Boolean>(context) {

    override fun loadInBackground(): Boolean? {
        val httpURLConnection: HttpURLConnection
        try {
            httpURLConnection = URL(Zcapt.zcaptData.veriUrl + "?authID=$authID&x=$x&y=$y").openConnection() as HttpURLConnection
            with(httpURLConnection) {
                requestMethod = "GET"
                readTimeout = 5000

                return if (responseCode == 200) {
                    val json = inputStream.use {
                        val byteArrayOutputStream = ByteArrayOutputStream()
                        it.copyTo(byteArrayOutputStream)
                        byteArrayOutputStream.toString()
                    }
                    onResultCallBack.result(Gson().fromJson(json, ResultData::class.java))
                    true
                } else {
                    onResultCallBack.result(ResultData())
                    false
                }
            }

        } catch (e: Exception) {
            Log.e(javaClass.name, "", e)
            return false
        }
    }

    interface OnResultCallBack {
        fun result(resultData: ResultData)
    }
}
