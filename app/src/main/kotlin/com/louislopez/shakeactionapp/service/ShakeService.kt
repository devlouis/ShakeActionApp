package com.louislopez.shakeactionapp.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import com.louislopez.shakeactionapp.MainShakeActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

/**
 * Created by louislopez on 15,August,2019
 * MDP Consulting,
 * Peru, Lima.
 */
class ShakeService: Service(), SensorEventListener {

    private var mSensorManager: SensorManager? = null
    private var mAccelerometer: Sensor? = null
    private var mAccel: Float = 0.toFloat()
    private var mAccelCurrent: Float = 0.toFloat()
    private var mAccelLast: Float = 0.toFloat()

    override fun onBind(p0: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mAccelerometer = mSensorManager!!
            .getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mSensorManager!!.registerListener(
            this, mAccelerometer,
            SensorManager.SENSOR_DELAY_UI, Handler()
        )
        return START_STICKY
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onSensorChanged(event: SensorEvent) {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]
        mAccelLast = mAccelCurrent
        mAccelCurrent = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
        val delta = mAccelCurrent - mAccelLast
        mAccel = mAccel * 0.9f + delta // perform low-cut filter

        if (mAccel > 11) {
            val rnd = Random()
            val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
            //MainShakeActivity().tvShakeService!!.text = "Hola Belatrix!"
            //MainShakeActivity().tvShakeService!!.setTextColor(color)
            //MainShakeActivity().changeColortext("Hola Belatrix!", color)

            val intent = Intent("SHAKE_ACTION")
            intent.putExtra("TVI_TEXT", "Hola Belatrix!")
            intent.putExtra("TVI_COLOR", color)
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }
    }
}
