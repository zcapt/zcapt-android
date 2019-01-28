package com.github.zcapt.view.task

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.content.AsyncTaskLoader
import android.util.Log
import com.github.zcapt.Zcapt
import com.github.zcapt.model.AuthData
import com.google.gson.Gson
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL

class BitmapLoaderTask(
        context: Context,
        private var call: CallBack
) : AsyncTaskLoader<Boolean>(context) {

    override fun loadInBackground(): Boolean? {

        val bitmapList: MutableList<Bitmap> = ArrayList()
        return initAuthID(object : OnAuthLoadResult {
            override fun result(result: String) {
                val authData = Gson().fromJson(result, AuthData::class.java)
                val large = loadBitmapFromUrl(Zcapt.zcaptData.largeUrl + "?authID=${authData.authID}")
                bitmapList.add(large!!)
                val small = loadBitmapFromUrl(Zcapt.zcaptData.smallUrl + "?authID=${authData.authID}")
                bitmapList.add(small!!)
                call.result(authData.authID!!, bitmapList)
            }
        })
    }

    private fun initAuthID(onAuthLoadResult: OnAuthLoadResult): Boolean? {
        val httpURLConnection: HttpURLConnection
        try {
            httpURLConnection = URL(Zcapt.zcaptData.initUrl).openConnection() as HttpURLConnection
            with(httpURLConnection) {
                requestMethod = "GET"
                readTimeout = 10000
                return if (responseCode == 200) {
                    onAuthLoadResult.result(
                            inputStream.use {
                                val byteArrayOutputStream = ByteArrayOutputStream()
                                it.copyTo(byteArrayOutputStream)
                                byteArrayOutputStream.toString()
                            }
                    )
                    true
                } else {
                    false
                }
            }

        } catch (e: Exception) {
            Log.e(javaClass.name, "", e)
            return false
        }
    }

    private fun loadBitmapFromUrl(url: String): Bitmap? {
        val httpURLConnection: HttpURLConnection
        Log.e(javaClass.name, url)
        try {
            httpURLConnection = URL(url).openConnection() as HttpURLConnection
            with(httpURLConnection) {
                requestMethod = "GET"
                readTimeout = 10000
                return if (responseCode == 200) {
                    BitmapFactory.decodeStream(inputStream)
                } else {
                    null
                }
            }

        } catch (e: Exception) {
            Log.e(javaClass.name, "", e)
        }

        return null
    }


    interface OnAuthLoadResult {
        fun result(result: String)
    }

    interface CallBack {
        fun result(authID: String, bitmapList: MutableList<Bitmap>)
    }
}