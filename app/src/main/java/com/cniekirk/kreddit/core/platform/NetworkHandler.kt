package com.cniekirk.kreddit.core.platform

import android.content.Context
import com.cniekirk.kreddit.core.extensions.networkInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkHandler
@Inject constructor(private val context: Context) {
    val isConnected get() = context.networkInfo?.isConnected
}