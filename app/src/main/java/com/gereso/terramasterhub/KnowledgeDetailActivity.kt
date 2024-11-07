package com.gereso.terramasterhub

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class KnowledgeDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_knowledge_detail)

        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val imageResId = intent.getIntExtra("imageResId", R.drawable.bookies)

        val titleTextView = findViewById<TextView>(R.id.detailTitleTextView)
        val descriptionTextView = findViewById<TextView>(R.id.detailDescriptionTextView)
        val imageView = findViewById<ImageView>(R.id.detailImageView)

        titleTextView.text = title
        descriptionTextView.text = description
        imageView.setImageResource(imageResId)
    }
}