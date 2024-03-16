package com.example.passwordmanager.activityAndFragments

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient

class AutoFillWebViewHelper(private val context: Context, private val webView: WebView) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "AutoFillWebViewHelper",
        Context.MODE_PRIVATE
    )

    init {
        initializeWebView()
    }

    private fun initializeWebView() {
        webView.webViewClient = MyWebViewClient()
        webView.webChromeClient = MyWebChromeClient()
    }

    fun loadUrl(url: String) {
        webView.loadUrl(url)
    }

    private inner class MyWebViewClient : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            autoFillFields(url)
        }

        @Deprecated("Deprecated in Java")
        override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
            super.onReceivedError(view, errorCode, description, failingUrl)
            Log.e("WebViewError", "Error loading URL: $failingUrl, Description: $description")
        }
    }

    private inner class MyWebChromeClient : WebChromeClient() {
    }

    private fun autoFillFields(url: String?) {
        val login = sharedPreferences.getString(getKey(url, "login"), null)
        val password = sharedPreferences.getString(getKey(url, "password"), null)

        if (!login.isNullOrEmpty() && !password.isNullOrEmpty()) {
            webView.evaluateJavascript(
                "document.getElementById('login_field').value = '$login';" +
                        "document.getElementById('password_field').value = '$password';",
                null
            )
        }
    }

    fun saveCredentials(url: String, login: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString(getKey(url, "login"), login)
        editor.putString(getKey(url, "password"), password)
        editor.apply()
    }

    private fun getKey(url: String?, type: String): String {
        return url?.let { "$it-$type" } ?: ""
    }
}