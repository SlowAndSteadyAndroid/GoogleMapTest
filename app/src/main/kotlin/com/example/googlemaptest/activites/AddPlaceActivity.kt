package com.example.googlemaptest.activites

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.googlemaptest.R
import com.example.googlemaptest.application.FavoritePlacesApplication
import com.example.googlemaptest.models.Place
import com.example.googlemaptest.models.ResultMightThrow
import com.example.googlemaptest.network.Client
import java.util.function.Consumer

class AddPlaceActivity : AppCompatActivity(),
    Consumer<ResultMightThrow<Boolean>> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addplace)

        findViewById<Button>(R.id.save_button).setOnClickListener {


            val description = findViewById<EditText>(R.id.description).text.toString()

            val place = Place(
                id = FavoritePlacesApplication.CLIENT_ID,
                name = "New Place",
                latitude = intent.getStringExtra("latitude")?.toDouble() ?: 0.0,
                longitude = intent.getStringExtra("longitude")?.toDouble() ?: 0.0,
                description = description
            )


            Client.postFavoritePlace(place, this)

        }

    }

    override fun accept(p0: ResultMightThrow<Boolean>) {
        if (p0.result) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

}