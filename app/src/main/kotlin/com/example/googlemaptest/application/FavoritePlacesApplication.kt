package com.example.googlemaptest.application

import android.app.Application
import android.os.Build
import com.example.googlemaptest.network.Server

class FavoritePlacesApplication : Application() {

    // Called when the app is created
    override fun onCreate() {
        super.onCreate()

        // We start the API server differently depending on whether we are in a testing environment or not
        if (Build.FINGERPRINT == "robolectric") {
            Server.start()
        } else {
            Thread { Server.start() }.start()
        }
    }

    companion object {
        // Default API server port and URL
        // You can modify the port setting if it is conflicting with something else on your machine
        const val DEFAULT_SERVER_PORT = 8989

        const val SERVER_URL = "http://localhost:$DEFAULT_SERVER_PORT/"

        // Put your ID (from ID.txt) here
        const val CLIENT_ID = ""
    }
}
