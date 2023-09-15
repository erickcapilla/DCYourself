package com.erickcapilla.dcyourself.util

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.erickcapilla.dcyourself.R
import android.widget.TextView
import androidx.core.graphics.component1
import androidx.core.graphics.toColorFilter

class ProgressBarComponent(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
  var color: Int
  init {
    inflate(context, R.layout.progress_bar_component, this)

    val attributes = context.obtainStyledAttributes(attrs, R.styleable.ProgressBarComponent)
    color = attributes.getColor(R.styleable.ProgressBarComponent_progressColor, ContextCompat.getColor(context, R.color.white))

    findViewById<ProgressBar>(R.id.progressBarCircle).indeterminateDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    findViewById<TextView>(R.id.progressTitle).setTextColor(color)
    findViewById<TextView>(R.id.progressTitle).text = attributes.getString(R.styleable.ProgressBarComponent_message)
    attributes.recycle()
  }

}