package com.example.graduationproject

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class Rating : AppCompatActivity() {
      private lateinit var btnRat:Button
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)
        btnRat= findViewById<Button>(R.id.btn2_rat_app)
        btnRat.setOnClickListener(){
            val uri:Uri= Uri.parse("market://")
        }
    }
}