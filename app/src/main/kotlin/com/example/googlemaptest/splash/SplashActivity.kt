package com.example.googlemaptest.splash

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.googlemaptest.R
import com.example.googlemaptest.base.BaseActivity
import com.example.googlemaptest.databinding.ActivitySplashBinding
import com.example.googlemaptest.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {
                    routeToMain()
                }

                else -> {
                    Toast.makeText(
                        this,
                        "Either ACCESS_COARSE_LOCATION or ACCESS_FINE_LOCATION permissions are required",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun routeToMain() {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                startActivity(Intent(this, HomeActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                })
            },
            800L
        )
    }
}