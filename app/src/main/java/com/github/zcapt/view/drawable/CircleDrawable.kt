package com.github.zcapt.view.drawable

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.*
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.util.Property
import android.view.animation.DecelerateInterpolator

class CircleDrawable : Drawable(), Animatable {

    private var mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mValueAnimator: ValueAnimator? = null

    private var mRect = RectF()

    private var mRadius = 0f

    var mDelay = 0L

    init {
        with(mPaint) {
            color = Color.WHITE

            //region 画饼
            style = Paint.Style.FILL
            //endregion


            //region 画线
            //style = Paint.Style.STROKE
            //strokeWidth = 5f
            //endregion
        }
    }

    private var mRadiusProperty: Property<CircleDrawable, Float> = object : Property<CircleDrawable, Float>(Float::class.java, "radius") {
        override fun set(obj: CircleDrawable, value: Float) {
            obj.mRadius = value
        }

        override fun get(obj: CircleDrawable): Float {
            return obj.mRadius
        }
    }


    override fun draw(canvas: Canvas) {
        canvas.drawCircle(mRect.centerX(), mRect.centerY(), mRadius, mPaint)
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
        val maxRadius = Math.max(mRect.width(), mRect.height()) / 2
//        val maxRadius = (mRect.right - mRect.left) / 2

        val radiusHolder = PropertyValuesHolder.ofFloat(mRadiusProperty, 0f, maxRadius)
        val alphaHolder = PropertyValuesHolder.ofInt("alpha", 255, 0)

        mValueAnimator = ObjectAnimator.ofPropertyValuesHolder(this, radiusHolder, alphaHolder)
        with(mValueAnimator!!) {
            startDelay = mDelay
            duration = 1200
            interpolator = DecelerateInterpolator(1.0f)
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