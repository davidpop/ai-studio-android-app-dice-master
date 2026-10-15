package com.example.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

class ShakeDetector(
  context: Context,
  private val onShake: () -> Unit
) : SensorEventListener {

  private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
  private val accelerometer: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

  private var lastShakeTimestamp: Long = 0
  private val shakeThresholdGForce = 2.4f
  private val shakeSlopTimeMs = 800

  fun start() {
    accelerometer?.let { sensor ->
      sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)
    }
  }

  fun stop() {
    sensorManager?.unregisterListener(this)
  }

  override fun onSensorChanged(event: SensorEvent?) {
    if (event == null || event.sensor.type != Sensor.TYPE_ACCELEROMETER) return

    val x = event.values[0]
    val y = event.values[1]
    val z = event.values[2]

    val gX = x / SensorManager.GRAVITY_EARTH
    val gY = y / SensorManager.GRAVITY_EARTH
    val gZ = z / SensorManager.GRAVITY_EARTH

    val gForce = sqrt(gX * gX + gY * gY + gZ * gZ)

    if (gForce > shakeThresholdGForce) {
      val now = System.currentTimeMillis()
      if (lastShakeTimestamp + shakeSlopTimeMs < now) {
        lastShakeTimestamp = now
        onShake()
      }
    }
  }

  override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    // No-op
  }
}
