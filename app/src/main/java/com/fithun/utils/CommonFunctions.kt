package com.fithun.utils

object CommonFunctions {
    fun convertDistanceKM(value: Double) :String {
        return if (value >= 1.0) {
            "Km"
        } else {
            "m"
        }
    }
}