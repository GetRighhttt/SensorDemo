package com.example.accelerometersensordemo

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var rotationTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        rotationTV = findViewById<TextView>(R.id.tv_rotation)

        createSensorInfo()
    }

    /*
    Determine what to do with each sensor.

    In this case, we're just testing for the accelerometer.
     */
    private fun createSensorInfo() {

        // get an instance of system service
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_FASTEST,
                SensorManager.SENSOR_DELAY_FASTEST
            )
        }

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            /*
            sides will Retrieve the values when tilted left and right.

            upDown will Retrieve the values when tilted up and down.
             */
            val sides = event.values[0]
            val upDown = event.values[1]

            /*
            Set the rotation limits for the sensor
             */
            rotationTV.apply {
                rotationX = upDown * 3f
                rotationY = sides * 3f
                rotation = -sides
                translationX = sides * -10
                translationY = upDown * 10
            }

            /*
            Switch the color based on the device orientation
             */
            val color = if(upDown.toInt() == 0 && sides.toInt() == 0) Color.RED else Color.BLUE
            rotationTV.setBackgroundColor(color)

            /*
            Use String interpolation to update the textview
             */
            rotationTV.text = "up/down ${upDown.toInt()} \nleft/right ${sides.toInt()}"
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        return
    }

    // unregister the listener when activity is destroyed
    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }
}