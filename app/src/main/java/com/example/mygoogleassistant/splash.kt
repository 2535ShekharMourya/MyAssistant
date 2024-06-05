package com.example.mygoogleassistant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.blogspot.atifsoftwares.animatoolib.Animatoo

class splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed({
            // task for executing after 3 second
            startActivity(Intent(this,MainActivity::class.java))
            finish()
            Animatoo.animateFade(this)
        },3000)
    }
}