package com.github.zcapt

import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.github.zcapt.view.ZcaptView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Zcapt.init("eyJpbml0IjoiaHR0cHM6Ly9hcGkudGhhdHNlZWQub3JnL2NhcHRjaGEvbmV3Q2FwdGNoYSIsImxhcmdlIjoiaHR0cHM6Ly9hcGkudGhhdHNlZWQub3JnL2NhcHRjaGEvZ2V0SW1hZ2UiLCJzbWFsbCI6Imh0dHBzOi8vYXBpLnRoYXRzZWVkLm9yZy9jYXB0Y2hhL2dldFNtYWxsSW1hZ2UiLCJ2ZXJpIjoiaHR0cHM6Ly9hcGkudGhhdHNlZWQub3JnL2NhcHRjaGEvdmVyaWZ5In0")
        setContentView(R.layout.activity_main)

        zcapt.onResultListener = object : ZcaptView.OnResultListener {
            override fun onResult(authID: String?, result: Boolean) {
                Log.e(javaClass.name, "authID:$authID result:$result")
            }
        }
    }

}
