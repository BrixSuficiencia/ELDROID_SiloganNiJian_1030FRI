package com.gereso.terramasterhub

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class SurveyorDetailActivity: AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_surveyor_detail)

        val name = intent.getStringExtra("Name")
        val specialty = intent.getStringExtra("Specialty")
        val introduction = intent.getStringExtra("Introduction")
        val location = intent.getStringExtra("Location")
        val proposals = intent.getStringExtra("Proposals")
        val price = "Php " + intent.getStringExtra("Price")
        val imageUrl = intent.getStringExtra("ImageUrl")

        val nameTextView = findViewById<TextView>(R.id.nameTextView)
        val specialtyTextView = findViewById<TextView>(R.id.specialtyTextView)
        val introductionTextView = findViewById<TextView>(R.id.introductionTextView)
        val locationTextView = findViewById<TextView>(R.id.Location)
        val proposalsTextView = findViewById<TextView>(R.id.Proposals)
        val priceTextView = findViewById<TextView>(R.id.priceTextView)
        val imageView = findViewById<ImageView>(R.id.expertdp)
        val messageButton = findViewById<ImageButton>(R.id.Message)

        nameTextView.text = name
        specialtyTextView.text = specialty
        introductionTextView.text = introduction
        locationTextView.text = location
        proposalsTextView.text = proposals
        priceTextView.text = price

        if (!imageUrl.isNullOrEmpty()) {
            Picasso.get().load(imageUrl).into(imageView)
        } else {
            // If imageUrl is null or empty, set a default image
            imageView.setImageResource(R.drawable.noprofile)
        }

        messageButton.setOnClickListener {
            val intent = Intent(this@SurveyorDetailActivity, UserListActivity::class.java)
            startActivity(intent)
            finish() // Stop the current activity
        }
    }
}