package com.fithun.utils

import android.app.Activity
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.fithun.R
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

fun Fragment.displayToast(message: String?) {

    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun Activity.displayToast(message: String?) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun ImageView.load(url: String) {
    Glide.with(this)
        .load(url)
        .thumbnail(0.25f)
        .into(this)

}


fun ImageView.loadWithPlaceHolder(url: String?) {
    if (url == "") {
        Glide.with(this)
            .load(R.drawable.placeholder)
            .into(this)
    } else {
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.placeholder)
            .into(this)
    }

}

fun ImageView.loadImageResource(image: Int) {
    Glide.with(this).load(image).into(this)
}

fun EditText.setTextValue(value: String?) {
    if (!value.isNullOrEmpty()) {
        this.setText(value)
    } else {
        this.text.clear()
    }
}


fun TextView.setTextViewValue(value: String?) {
    this.text = value?.takeIf { it.isNotBlank() } ?: "NA"
}


fun String.capitalizeFirstLetter(): String {
    if (isEmpty()) {
        return this
    }
    return substring(0, 1).uppercase() + substring(1)
}

