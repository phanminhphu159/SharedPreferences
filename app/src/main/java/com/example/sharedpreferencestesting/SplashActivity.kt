package com.example.sharedpreferencestesting

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.sharedpreferencestesting.databinding.ActivityMainBinding
import com.example.sharedpreferencestesting.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private var activitySplashBinding: ActivitySplashBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpView()
    }

    private fun setUpView(){
        activitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(activitySplashBinding?.root)
        supportActionBar?.hide()
        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }, 2000)
    }
}