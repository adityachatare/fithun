package com.fithun.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.fithun.R

class RoundedHorizontalProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val backgroundPaint = Paint()
    private val progressPaint = Paint()
    private val path = Path()
    private val cornerRadius = 20f

    private var progress = 0
    private var maxProgress = 100 // Change this to your desired maximum progress
    private val backgroundColor = ContextCompat.getColor(context, R.color.grey)
    private val progressColor = ContextCompat.getColor(context, R.color.check_color) // Change to your desired fill color

    init {
        backgroundPaint.color = backgroundColor
        progressPaint.color = progressColor
    }



    fun setMaxProgress(progress: Int) {
        this.maxProgress = progress

    }

    fun setProgress(progress: Int) {
        this.progress = progress
        invalidate() // Redraw the view
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        val width = width.toFloat()
        val height = height.toFloat()

        // Calculate the right position of the progress based on the progress value
        val progressRight = (progress.toFloat() / maxProgress) * width

        // Clear the path and start a new one
        path.reset()

        // Create a rounded rectangle path
        path.addRoundRect(0f, 0f, progressRight, height, cornerRadius, cornerRadius, Path.Direction.CW)

        // Draw background
        canvas.drawRect(0f, 0f, width, height, backgroundPaint)

        // Draw progress using the rounded rectangle path
        canvas.drawPath(path, progressPaint)
    }
}
