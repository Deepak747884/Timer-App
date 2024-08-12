package com.example.timerapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class TimerActivity : AppCompatActivity() {

    private lateinit var tvTimer: TextView
    private lateinit var tvMessage: TextView
    private lateinit var countdownTimer: CountDownTimer
    private val REQUEST_NOTIFICATION_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        tvTimer = findViewById(R.id.tvTimer)
        tvMessage = findViewById(R.id.tvMessage)

        // Request notification permission for Android 13 and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_NOTIFICATION_PERMISSION)
            }
        }

        val timeInSeconds = intent.getIntExtra("TIME_IN_SECONDS", 0)

        countdownTimer = object : CountDownTimer((timeInSeconds * 1000).toLong(), 1000) {
            @SuppressLint("DefaultLocale")
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                val hours = seconds / 3600
                val minutes = (seconds % 3600) / 60
                val secs = seconds % 60

                val time = String.format("%02d:%02d:%02d", hours, minutes, secs)
                tvTimer.text = time
            }

            override fun onFinish() {
                tvMessage.visibility = TextView.VISIBLE
                sendNotification()
            }
        }.start()
    }

    // Handle the permission request result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with sending notifications
            } else {
                // Permission denied, handle accordingly (optional)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun sendNotification() {
        val notificationId = 1
        val channelId = "timer_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Timer Notification"
            val descriptionText = "Notification when timer is over"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_timer)
            .setContentTitle("Timer Finished")
            .setContentText("Your timer has ended.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
    }
}
