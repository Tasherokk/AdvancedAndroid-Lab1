package com.example.lab1.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AirplaneModeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // AIRPLANE_MODE broadcast?
        if (intent.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
            val isAirplaneModeOn = intent.getBooleanExtra("state", false)
            Toast.makeText(
                context,
                "Airplane mode is ${if (isAirplaneModeOn) "ON" else "OFF"}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}