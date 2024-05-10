package com.fithun.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.fithun.R

class VerticalProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val backgroundPaint = Paint()
    private val progressPaint = Paint()

    private var progress = 0
    private val maxProgress = 100
    private val backgroundColor = ContextCompat.getColor(context, R.color.grey)
    private val progressColor = ContextCompat.getColor(context, R.color.check_color)

    init {
        backgroundPaint.color = backgroundColor

    }

    fun setProgress(progress: Int) {
        this.progress = progress
        invalidate()
    }

    fun setProgressColor(progressColor:Int){
        progressPaint.color = progressColor
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        val width = width.toFloat()
        val height = height.toFloat()

        val progressTop = (progress.toFloat() / maxProgress) * height

        // Draw background
        canvas.drawRect(0f, 0f, width, height, backgroundPaint)

        // Draw progress
        canvas.drawRect(0f, 0f, width, progressTop, progressPaint)
    }



}
