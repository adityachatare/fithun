package com.fithun.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.fithun.R

class CircleProgressBar(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 35f
        color = ContextCompat.getColor(context, R.color.calories_progress_background)
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 35f
        color = ContextCompat.getColor(context, R.color.calories_progress)
    }

    private val oval = RectF()

    private var progress: Float = 0f

    fun setProgress(value: Float) {
        progress = value.coerceIn(0f, 1f)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = (width.coerceAtMost(height) - backgroundPaint.strokeWidth) / 2f

        val startAngle = 180f
        val endAngle = 360f
        oval.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius)
        canvas.drawArc(oval, startAngle, endAngle - startAngle, false, backgroundPaint)

        val progressStartAngle = 180f
        val progressEndAngle = 90f + 270f * progress
        canvas.drawArc(oval, progressStartAngle, progressEndAngle - progressStartAngle, false, progressPaint)
    }
}
