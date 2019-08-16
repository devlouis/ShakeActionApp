package com.louislopez.shakeactionapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.util.Log

import com.louislopez.shakeactionapp.service.ShakeService
import kotlinx.android.synthetic.main.activity_main.*


class MainShakeActivity : AppCompatActivity() {
    private lateinit var myReceiver: MyReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val intent = Intent(this, ShakeService::class.java)
        startService(intent)
        myReceiver = MyReceiver()

        val intentFilter = IntentFilter()
        intentFilter.addAction("SHAKE_ACTION")
        LocalBroadcastManager.getInstance(this).registerReceiver(
            myReceiver, intentFilter
        )
    }

    fun changeColortext(text: String, color: Int) {
        tvShakeService.text = text
        tvShakeService.setTextColor(color)
        Log.v("Main", "changeColortext $text")
    }

    private inner class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            when {
                action == "SHAKE_ACTION" -> {
                    val extras = intent.extras
                    val text = extras.getString("TVI_TEXT")
                    val color = extras.getInt("TVI_COLOR")
                    changeColortext(text, color)
                }
            }
        }
    }
}
