package com.github.zcapt.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import com.github.zcapt.R
import com.github.zcapt.model.ResultData
import com.github.zcapt.view.drawable.MultiCircleDrawable
import com.github.zcapt.view.task.BitmapLoaderTask
import com.github.zcapt.view.task.ResultTask
import kotlinx.android.synthetic.main.zcaptview.view.*

class ZcaptView : FrameLayout {

    companion object {
        const val MESSAGE_LOAD_BITMAP = 1
        const val MESSAGE_RESULT_SUCCESS = 0
        const val MESSAGE_RESULT_FAIL = -1
    }

    var onResultListener: OnResultListener? = null

    var authID: String? = null

    var onLoadStatus = false

    private lateinit var bitmapList: MutableList<Bitmap>

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context, attributeSet, defStyle) {
        val view = View.inflate(context, R.layout.zcaptview, null)
        removeAllViews()

        with(view) {
            image.setBackgroundColor(Color.GRAY)
            image.setImageDrawable(MultiCircleDrawable())
        }

        addView(view)
    }

    private val mMyHandler = Handler {
        when (it.what) {
            MESSAGE_LOAD_BITMAP -> {
                onLoadStatus = false
                resolved()
                if (bitmapList.size == 2) {
                    initView()
                    image.setImageBitmap(bitmapList[0])
                    key.setImageBitmap(bitmapList[1])
                }
            }
            MESSAGE_RESULT_SUCCESS -> {
                resolved()
                onLoadStatus = false
            }
            MESSAGE_RESULT_FAIL -> {
                val animX = ObjectAnimator.ofFloat(key, "x", 0f)
                val animY = ObjectAnimator.ofFloat(key, "y", 0f)
                AnimatorSet().apply {
                    playTogether(animX, animY)
                    duration = 500
                    interpolator = DecelerateInterpolator(1.8f)
                    start()
                }
                load()
            }
        }
        return@Handler true
    }


    private fun resolving() {
        loading.setImageDrawable(MultiCircleDrawable())
    }

    private fun resolved() {
        loading.setImageDrawable(null)
    }

    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)
        load()
    }


    private fun load() {
        Thread {
            BitmapLoaderTask(context, object : BitmapLoaderTask.CallBack {
                override fun result(authID: String, bitmapList: MutableList<Bitmap>) {
                    this@ZcaptView.bitmapList = bitmapList
                    this@ZcaptView.authID = authID
                    mMyHandler.sendEmptyMessage(MESSAGE_LOAD_BITMAP)
                }
            }).loadInBackground()
        }.start()
    }

    private fun initView() {
        var rx = 0f
        var ry = 0f
        image.viewTreeObserver.addOnGlobalLayoutListener {
            key.setOnTouchListener { view: View?, motionEvent: MotionEvent? ->
                if (onLoadStatus) return@setOnTouchListener false

                when (motionEvent!!.action) {

                    MotionEvent.ACTION_DOWN -> {
                        rx = motionEvent.rawX - view!!.x
                        ry = motionEvent.rawY - view.y

                    }
                    MotionEvent.ACTION_MOVE -> {
                        view!!.x = Math.max(Math.min(motionEvent.rawX - rx, image.width - key.width.toFloat()), 0f)
                        view.y = Math.max(Math.min(motionEvent.rawY - ry, image.height - key.height.toFloat()), 0f)


                    }
                    MotionEvent.ACTION_UP -> {
                        if (view == null) return@setOnTouchListener false

                        resolving()

                        //换算之后请求的X，Y值
                        val pX = view.x / image.width * 300
                        val pY = view.y / image.height * 200

                        Thread {
                            onLoadStatus = true
                            ResultTask(context, authID!!, pX.toInt().toString(), pY.toInt().toString(), object : ResultTask.OnResultCallBack {
                                override fun result(resultData: ResultData) {
                                    Log.e(javaClass.name, resultData.toString())
                                    if (resultData.status == 0) {
                                        onResultListener?.onResult(authID, true)
                                        mMyHandler.sendEmptyMessage(MESSAGE_RESULT_SUCCESS)
                                    } else {
                                        onResultListener?.onResult(authID, false)
                                        mMyHandler.sendEmptyMessage(MESSAGE_RESULT_FAIL)
                                    }
                                }
                            }).loadInBackground()

                        }.start()

                    }
                }

                return@setOnTouchListener true
            }
        }
    }

    interface OnResultListener {
        fun onResult(authID: String?, result: Boolean)
    }
}