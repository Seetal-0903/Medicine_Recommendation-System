package com.example.healthapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Simulate a delay (2 seconds) for the splash screen
        Handler(Looper.getMainLooper()).postDelayed({
            // Check if the user is logged in
            val isLoggedIn = checkUserLoggedIn()

            // Navigate to Login or Main Activity based on the login status
            if (isLoggedIn) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish() // Close SplashActivity
        }, 2000) // 2-second delay
    }

    // Simulated function to check if the user is logged in
    private fun checkUserLoggedIn(): Boolean {
        // Replace with actual login logic (e.g., shared preferences or database check)
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        return sharedPreferences.getBoolean("is_logged_in", false)
    }
}
