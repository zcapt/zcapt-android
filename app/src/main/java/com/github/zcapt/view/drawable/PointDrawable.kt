package com.github.zcapt.view.drawable

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.*
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.util.Property
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator

class PointDrawable : Drawable(), Animatable {

    private var mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mValueAnimator: ValueAnimator? = null

    private var mRect = RectF()

    private var mRadius = 20f

    private var mCenterX = 0f

    private var mCenterY = 0f

    private var mRotate = 0f

    var mDelay = 0L

    init {
        with(mPaint) {
            color = Color.DKGRAY

            //region 画饼
            style = Paint.Style.FILL
            //endregion


            //region 画线
            //style = Paint.Style.STROKE
            //strokeWidth = 5f
            //endregion
        }
    }

    private var mRotationProperty: Property<PointDrawable, Float> = object : Property<PointDrawable, Float>(Float::class.java, "rotate") {
        override fun set(obj: PointDrawable, value: Float) {
            obj.mRotate = value
        }

        override fun get(obj: PointDrawable): Float {
            return obj.mRotate
        }
    }

    override fun draw(canvas: Canvas) {
        val r = mRect.height() / 3
//        mCenterX = mRect.centerX() + r * Math.sin(Math.toRadians(mRotate.toDouble())).toFloat()
//        mCenterY = mRect.centerY() + r * Math.cos(Math.toRadians(mRotate.toDouble())).toFloat()
        canvas.rotate(mRotate,mRect.centerX(),mRect.centerY())
        canvas.drawCircle(mRect.centerX(), mRect.centerY() + r, mRadius, mPaint)
    }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
    }

    @SuppressLint("WrongConstant")
    override fun getOpacity(): Int {
        return PixelFormat.RGBA_8888
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mPaint.colorFilter = colorFilter
    }


    public override fun onBoundsChange(bounds: Rect?) {
        super.onBoundsChange(bounds)
        mRect.set(bounds)
        if (isRunning) {
            stop()
        }
//        val maxRadius = Math.max(mRect.width(), mRect.height()) / 2
//        val maxRadius = (mRect.right - mRect.left) / 2

        val rotateHolder = PropertyValuesHolder.ofFloat(mRotationProperty, 0f, 360f)

//        val alphaHolder = PropertyValuesHolder.ofInt("alpha", 255, 0)


        mValueAnimator = ObjectAnimator.ofPropertyValuesHolder(this, rotateHolder)
        with(mValueAnimator!!) {
            startDelay = mDelay
            duration = 1200
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                invalidateSelf()
            }
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            start()
        }
    }

//    private fun clipSquare(rect: Rect) {
//
//    }

    override fun isRunning(): Boolean {
        return mValueAnimator != null && mValueAnimator!!.isRunning
    }

    override fun start() {
        mValueAnimator!!.start()
    }

    override fun stop() {
        mValueAnimator!!.end()
    }
}