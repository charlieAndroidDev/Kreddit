package com.cniekirk.kreddit.core.extensions

/**
 * Extension function to format a double into a 1 decimal place string
 */
fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)