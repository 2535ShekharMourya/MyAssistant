package com.example.mygoogleassistant

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.mygoogleassistant.utils.Utils
import com.example.mygoogleassistant.utils.Utils.setCustomActionBar
import com.example.mygoogleassistant.R
import com.example.mygoogleassistant.assistant.AssistantActivity
import com.example.mygoogleassistant.assistant.ExploreActivity
import com.example.mygoogleassistant.functions.GoogleLensActivity
import com.example.mygoogleassistant.utils.Utils.setCustomActionBar

class MainActivity : AppCompatActivity() {
    // Activity and Fragment known as UI Controller
   /* Both the Activity and Application classes extend the Context class.
     In android, Context is the main important concept and the wrong usage of it leads to memory leakage.
     Activity refers to an individual screen and
     Application refers to the whole app and both extend the Context class.*/

    // variable for views
    private lateinit var imageView: ImageView
    private lateinit var hiGoogle: ImageView
    private lateinit var googleLens: ImageView
    private lateinit var explore: ImageView
    private val Record_Audio_Request_Code:Int=1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setCustomActionBar(supportActionBar, this)
        // id's of views from xml
        imageView = findViewById(R.id.action_button)
        googleLens = findViewById(R.id.action_google_lens)
        explore = findViewById(R.id.action_explore)
        hiGoogle = findViewById(R.id.hiGoogle)

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
        ) {
            checkPermission()
        }
        imageView.setOnClickListener {
            /* Uses of Context
            1.It allows us to access resources.
            2.It allows us to interact with other Android components by sending messages.
            3.It gives you information about your app environment.*/
            startActivity(Intent(this, AssistantActivity::class.java))
            Animatoo.animateDiagonal(this)
        }
        hiGoogle.setOnClickListener {
            startActivity(Intent(this, AssistantActivity::class.java))
            Animatoo.animateSplit(this)
        }
        googleLens.setOnClickListener {
            startActivity(Intent(this, GoogleLensActivity::class.java))
            Animatoo.animateInAndOut(this)
        }
        explore.setOnClickListener {
            startActivity(Intent(this, ExploreActivity::class.java))
            Animatoo.animateFade(this)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==Record_Audio_Request_Code && grantResults.isNotEmpty())
        {
            //permigranted type of array that stores all the permission
            if (grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this,"Permission Granted", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun checkPermission(){
        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.RECORD_AUDIO),
            Record_Audio_Request_Code )
    }

    }
