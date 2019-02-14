package com.cniekirk.kreddit.ui.authentication

import android.graphics.Bitmap
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.cniekirk.kreddit.R
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import com.cniekirk.kreddit.core.extensions.contains
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity() {

    private var loggedIn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_login)

        CookieManager.getInstance().removeAllCookies(null)

        login_webview.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {

                if (url contains "code=") {

                    view.stopLoading()
                    loggedIn = true
                    handlePermissionGranted(url)

                } else if (url contains "error=") {

                    view.stopLoading()
                    TODO("not implemented") // Display something to the user

                }

            }

        }

    }

    private fun handlePermissionGranted(url: String) {



    }


}