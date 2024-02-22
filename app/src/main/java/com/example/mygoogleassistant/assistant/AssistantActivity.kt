package com.example.mygoogleassistant.assistant

import android.content.ClipboardManager
import android.content.Intent
import android.hardware.camera2.CameraManager
import android.media.Ringtone
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
//import androidx.databinding.DataBindingUtil
import com.example.mygoogleassistant.R
//import com.example.mygoogleassistant.databinding.ActivityAssistantBinding
import com.example.mygoogleassistant.utils.Utils

import android.Manifest
import android.animation.Animator
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.location.Location
import android.media.RingtoneManager
import android.os.Build
import android.provider.Settings
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mygoogleassistant.data.AssistantDatabase
import com.example.mygoogleassistant.utils.Utils.logTTS


/*
import com.example.google.databinding.ActivityAssistantBinding
import com.example.google.functions.AssistantFunctions.Companion.Animation_TIME
import com.example.google.functions.AssistantFunctions.Companion.CAPTUREPHOTO
import com.example.google.functions.AssistantFunctions.Companion.Dips
import com.example.google.functions.AssistantFunctions.Companion.READCONTACTS
import com.example.google.functions.AssistantFunctions.Companion.READSMS
import com.example.google.functions.AssistantFunctions.Companion.REQUEST_CALL
import com.example.google.functions.AssistantFunctions.Companion.REQUEST_CODE_SELECT_DOC
import com.example.google.functions.AssistantFunctions.Companion.REQUEST_ENABLE_BT
import com.example.google.functions.AssistantFunctions.Companion.SHAREAFILE
import com.example.google.functions.AssistantFunctions.Companion.SHAREATEXTFILE
import com.example.google.functions.AssistantFunctions.Companion.callContact
import com.example.google.functions.AssistantFunctions.Companion.capturePhoto
import com.example.google.functions.AssistantFunctions.Companion.clipBoardCopy
import com.example.google.functions.AssistantFunctions.Companion.clipBoardSpeak
import com.example.google.functions.AssistantFunctions.Companion.getAllPairedDevices
import com.example.google.functions.AssistantFunctions.Companion.getDate
import com.example.google.functions.AssistantFunctions.Companion.getTextFromBitmap
import com.example.google.functions.AssistantFunctions.Companion.getTime
import com.example.google.functions.AssistantFunctions.Companion.joke
import com.example.google.functions.AssistantFunctions.Companion.makeAPhoneCall
import com.example.google.functions.AssistantFunctions.Companion.motivationalThoughts
import com.example.google.functions.AssistantFunctions.Companion.openFacebook
import com.example.google.functions.AssistantFunctions.Companion.openGmail
import com.example.google.functions.AssistantFunctions.Companion.openGoogle
import com.example.google.functions.AssistantFunctions.Companion.openMaps
import com.example.google.functions.AssistantFunctions.Companion.openMessages
import com.example.google.functions.AssistantFunctions.Companion.openWhatsAPP
import com.example.google.functions.AssistantFunctions.Companion.openYoutube
import com.example.google.functions.AssistantFunctions.Companion.playRingtone
import com.example.google.functions.AssistantFunctions.Companion.question
import com.example.google.functions.AssistantFunctions.Companion.readMe
import com.example.google.functions.AssistantFunctions.Companion.readSMS
import com.example.google.functions.AssistantFunctions.Companion.search
import com.example.google.functions.AssistantFunctions.Companion.sendSMS
import com.example.google.functions.AssistantFunctions.Companion.shareAFile
import com.example.google.functions.AssistantFunctions.Companion.shareATextMessage
import com.example.google.functions.AssistantFunctions.Companion.speak
import com.example.google.functions.AssistantFunctions.Companion.stopRingtone
import com.example.google.functions.AssistantFunctions.Companion.turnOffBluetooth
import com.example.google.functions.AssistantFunctions.Companion.turnOffFlash
import com.example.google.functions.AssistantFunctions.Companion.turnOnBluetooth
import com.example.google.functions.AssistantFunctions.Companion.turnOnFlash
import com.example.google.functions.GoogleLensActivity

import com.example.google.utils.UiUtils.*
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
*/

import java.io.File
import java.io.FileNotFoundException
import java.util.*


class AssistantActivity : AppCompatActivity() {
    /*private lateinit var binding: ActivityAssistantBinding
    private lateinit var assistantViewModel: AssistantViewModel
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var recognizerIntent: Intent
    private lateinit var keeper: String
    private lateinit var cameraManager: CameraManager
    private lateinit var clipboardManager: ClipboardManager
    private lateinit var cameraID: String
    private lateinit var ringnote: Ringtone
    private lateinit var imageuri: Uri*/
  //  private lateinit var helper: OpenWeatherMapHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assistant)
       /* binding = DataBindingUtil.setContentView(this, R.layout.activity_assistant)
        Utils.setCustomActionBar(supportActionBar, this)
        if (Settings.System.canWrite(this)) {
            ringnote = RingtoneManager.getRingtone(
                applicationContext,
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            )
        } else {
            //Migrate to Setting write permission screen.
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.data = Uri.parse("package:" + this.packageName)
            startActivity(intent)
        }

        val application = requireNotNull(this).application
        val dataSource = AssistantDatabase.getInstance(this).assistantDao
        val viewModelFactory = AssistantViewModelFactory(dataSource, application)
        assistantViewModel =
            ViewModelProvider(this, viewModelFactory).get(AssistantViewModel::class.java)
        val adapter = AssisantAdapter()
        binding.recylerView.adapter = adapter
        assistantViewModel.messages.observe(this, {
            it?.let {
                adapter.data = it
            }
        })

        binding.lifecycleOwner = this
        //animations
        if (savedInstanceState == null) {
            binding.assistantConstraintLayout.visibility = View.INVISIBLE
            //Q&A so like a tree for ques and ans like a conversation with help if tree
            //chat like applications

            val viewTreeObserver: ViewTreeObserver =
                binding.assistantConstraintLayout.viewTreeObserver
            if (viewTreeObserver.isAlive) {
                viewTreeObserver.addOnGlobalLayoutListener(object :
                    ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        //circularRevealActivity()
                        binding.assistantConstraintLayout.viewTreeObserver
                            .removeOnGlobalLayoutListener(this)

                    }
                })


            }
        }
        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        try {
            cameraID = cameraManager.cameraIdList[0]
            //0 for back camera
            // 1 for front camera
        } catch (e: java.lang.Exception) {
            e.printStackTrace()

        }
       *//* clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
       // helper = OpenWeatherMapHelper(R.string.OPEN_WEATHER_MAP_API_KEY.toString())
        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result: Int = textToSpeech.setLanguage(Locale.ENGLISH)
                if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA) {
                    Log.i(logTTS, "Language Not Supported")

                } else {
                    Log.i(logTTS, "Language Supported")
                }
            } else {
                Log.i(logTTS, "Initialization of TTS Failed")
            }

        }*/
    }
/*
    private fun circularRevealActivity() {

    }*/
}