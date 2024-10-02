package com.example.mygoogleassistant

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.example.mygoogleassistant.Networking.RetrofitClient
import com.example.mygoogleassistant.utils.Utils
import com.example.mygoogleassistant.utils.Utils.setCustomActionBar
import com.example.mygoogleassistant.R
import com.example.mygoogleassistant.assistant.AssistantActivity
import com.example.mygoogleassistant.assistant.ExploreActivity
import com.example.mygoogleassistant.assistant.NewsAdapter
import com.example.mygoogleassistant.databinding.ActivityMainBinding
import com.example.mygoogleassistant.functions.GoogleLensActivity
import com.example.mygoogleassistant.uiscreens.NewsActivity
//import com.example.mygoogleassistant.topnews.NewsActivity
import com.example.mygoogleassistant.utils.Utils.setCustomActionBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    // Activity and Fragment known as UI Controller
   /* Both the Activity and Application classes extend the Context class.
     In android, Context is the main important concept and the wrong usage of it leads to memory leakage.
     Activity refers to an individual screen and
     Application refers to the whole app and both extend the Context class.*/

    // variable for views
    lateinit var binding: ActivityMainBinding
    private lateinit var imageView: ImageView
    private lateinit var hiGoogle: ImageView
    private lateinit var googleLens: ImageView
    private lateinit var explore: ImageView
    lateinit var adapter: NewsAdapter
    private val Record_Audio_Request_Code:Int=1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setCustomActionBar(supportActionBar, this)

        makeApiRequest()

       // val topN=findViewById<TextView>(R.id.news_top)
       /* topN.setOnClickListener {
            startActivity(Intent(this,NewsActivity::class.java))
        }*/
        val getCurrentLocation=findViewById<FloatingActionButton>(R.id.get_location)
        // id's of views from xml
        imageView = findViewById(R.id.action_button)
        googleLens = findViewById(R.id.action_google_lens)
        explore = findViewById(R.id.action_explore)
        hiGoogle = findViewById(R.id.hiGoogle)

        getCurrentLocation.setOnClickListener {
            startActivity(Intent(this,CurrentLocationActivity::class.java))
        }

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
        binding.AllNews.setOnClickListener {
            startActivity(Intent(this,NewsActivity::class.java))
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

    private fun makeApiRequest() {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("NewsResponse","Start Coroutine scope")
            try {
                val response = RetrofitClient.instance.getTopHeadlines()
                Log.d("NewsResponse","New Response getting")
                if (response.isSuccessful) {
                    val newsResponse = response.body()
                    Log.d("NewsResponse","News Article fetch successfully")
                    withContext(Dispatchers.Main) {
                        val listOfArticle=newsResponse?.articles
                        adapter = NewsAdapter(listOfArticle,this@MainActivity)
                        binding.newsRecyclerview.adapter = adapter
                        binding.newsRecyclerview.layoutManager =
                            LinearLayoutManager(this@MainActivity)

                    }
                } else {
                    Log.d("NewsResponse", "failed to fetch News Articles")
                }
            }catch (e:Exception){
                Log.d("NewsResponse", "failed to fetch News Articles")
                e.printStackTrace()
            }
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
            else{
                Toast.makeText(this,"Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun checkPermission(){
        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.RECORD_AUDIO),
            Record_Audio_Request_Code )
    }

    }
