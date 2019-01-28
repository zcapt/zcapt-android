package com.github.zcapt.view.drawable

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable

class MultiCircleDrawable : Drawable(), Animatable, Drawable.Callback {


//    private var mCircleDrawables: Array<PointDrawable> = arrayOf(
//            PointDrawable(),
//            PointDrawable(),
//            PointDrawable(),
//            PointDrawable(),
//            PointDrawable(),
//            PointDrawable()
//
//
//    )

    private var mCircleDrawables: Array<CircleDrawable> = arrayOf(
            CircleDrawable(),
            CircleDrawable(),
            CircleDrawable(),
            CircleDrawable()
    )

    companion object {
        const val SPACE_TIME = 200L
    }

    init {
        mCircleDrawables.forEachIndexed { index, it ->
            it.mDelay = SPACE_TIME * index
            it.callback = this
            it.alpha = 255 * 0.5.toInt()
        }
    }

    override fun draw(canvas: Canvas) {
        mCircleDrawables.forEach {
            val count = canvas.save()
            it.draw(canvas)
            canvas.restoreToCount(count)
        }
    }

    override fun setAlpha(alpha: Int) {
    }

    @SuppressLint("WrongConstant")
    override fun getOpacity(): Int {
        return PixelFormat.RGBA_8888
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }


    override fun onBoundsChange(bounds: Rect?) {
        super.onBoundsChange(bounds)
        mCircleDrawables.forEach { it.onBoundsChange(bounds) }
    }

    override fun isRunning(): Boolean {
        mCircleDrawables.forEach {
            if (it.isRunning) return true
        }
        return false
    }

    override fun start() {
        mCircleDrawables.forEach { it.start() }
    }

    override fun stop() {
        mCircleDrawables.forEach { it.stop() }
    }

    override fun unscheduleDrawable(who: Drawable, what: Runnable) {
    }

    override fun invalidateDrawable(who: Drawable) {
        invalidateSelf()
    }

    override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {
    }
}