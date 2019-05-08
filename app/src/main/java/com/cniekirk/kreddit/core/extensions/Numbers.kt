package com.cniekirk.kreddit.core.extensions

fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)