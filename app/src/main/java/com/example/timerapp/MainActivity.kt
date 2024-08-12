package com.example.timerapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etHours: EditText = findViewById(R.id.etHours)
        val etMinutes: EditText = findViewById(R.id.etMinutes)
        val etSeconds: EditText = findViewById(R.id.etSeconds)
        val btnStartTimer: Button = findViewById(R.id.btnStartTimer)

        btnStartTimer.setOnClickListener {
            val hours = etHours.text.toString().toIntOrNull() ?: 0
            val minutes = etMinutes.text.toString().toIntOrNull() ?: 0
            val seconds = etSeconds.text.toString().toIntOrNull() ?: 0

            val totalTimeInSeconds = hours * 3600 + minutes * 60 + seconds

            val intent = Intent(this, TimerActivity::class.java)
            intent.putExtra("TIME_IN_SECONDS", totalTimeInSeconds)
            startActivity(intent)
        }
    }
}
