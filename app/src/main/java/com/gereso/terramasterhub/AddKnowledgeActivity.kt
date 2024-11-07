package com.gereso.terramasterhub

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddKnowledgeActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_knowledge)

        database = FirebaseDatabase.getInstance().getReference("knowledge")

        val addButton: Button = findViewById(R.id.SaveKnowledgeBtn)
        addButton.setOnClickListener {
            saveKnowledge()
        }
    }

    private fun saveKnowledge() {
        val titleEditText: EditText = findViewById(R.id.titleEditText)
        val descriptionEditText: EditText = findViewById(R.id.descriptionEditText)

        val title = titleEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val imageResource = R.drawable.bookies // Default image resource

        if (title.isNotEmpty() && description.isNotEmpty()) {
            val knowledge = Knowledge(title, description, imageResource)
            val newKnowledgeRef = database.push()
            newKnowledgeRef.setValue(knowledge).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Knowledge added successfully", Toast.LENGTH_SHORT).show()
                    // Open detail activity for the newly added knowledge
                    val intent = Intent(this, KnowledgeDetailActivity::class.java)
                    intent.putExtra("title", title)
                    intent.putExtra("description", description)
                    intent.putExtra("imageResId", imageResource)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Failed to add knowledge", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
        }
    }
}


