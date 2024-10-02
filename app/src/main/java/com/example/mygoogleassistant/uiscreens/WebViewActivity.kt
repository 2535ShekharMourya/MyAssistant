package com.example.mygoogleassistant.uiscreens

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mygoogleassistant.R

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_web_view)
        supportActionBar?.hide()

        val webView = findViewById<WebView>(R.id.webView)

        // Get the URL passed from the RecyclerView item
        val url = intent.getStringExtra("URL_KEY")

        // Enable JavaScript (optional)
        webView.settings.javaScriptEnabled = true

        // Load the URL in the WebView
        webView.webViewClient = WebViewClient()  // Prevent opening in browser
        webView.loadUrl(url ?: "https://www.example.com")
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}