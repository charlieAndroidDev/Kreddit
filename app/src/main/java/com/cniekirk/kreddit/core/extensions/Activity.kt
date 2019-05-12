package com.cniekirk.kreddit.core.extensions

import android.app.Activity
import android.content.Intent

/**
 * Extension function to make starting another activity easy
 */
inline fun <reified T: Activity> Activity.startActivity() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}