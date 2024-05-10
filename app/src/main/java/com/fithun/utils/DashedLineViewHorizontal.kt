package com.fithun.utils
import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.fithun.R

class DashedLineViewHorizontal(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint()

    init {
        paint.color = ContextCompat.getColor(context,R.color.viewColor) // Set the color of the dashed line
        paint.strokeWidth = 2f // Set the line width
        paint.pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f) // Set the dash pattern (10 pixels on, 10 pixels off)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerY = height / 2f

        canvas.drawLine(0f, centerY, width.toFloat(), centerY, paint)
    }
}
