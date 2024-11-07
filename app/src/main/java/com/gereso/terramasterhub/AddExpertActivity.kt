package com.gereso.terramasterhub

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class AddExpertActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null
    private lateinit var selectedImage: ImageView
    private lateinit var storageReference: StorageReference
    private lateinit var nameEditText: EditText
    private lateinit var specialtyEditText: EditText
    private lateinit var introductionEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var proposalsEditText: EditText
    private lateinit var priceEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expert)

        selectedImage = findViewById(R.id.AddImgbtn)
        nameEditText = findViewById(R.id.InputName)
        specialtyEditText = findViewById(R.id.InputSpecialty)
        introductionEditText = findViewById(R.id.InputIntroduction)
        locationEditText = findViewById(R.id.InputLocation)
        proposalsEditText = findViewById(R.id.InputProposals)
        priceEditText = findViewById(R.id.InputPrice)
        val addButton: Button = findViewById(R.id.SaveExprtbtn)

        storageReference = FirebaseStorage.getInstance().reference.child("expert_images")

        selectedImage.setOnClickListener { openImageChooser() }

        addButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val specialty = specialtyEditText.text.toString()
            val introduction = introductionEditText.text.toString()
            val location = locationEditText.text.toString()
            val proposals = proposalsEditText.text.toString()
            val price = priceEditText.text.toString()

            if (name.isNotEmpty() && specialty.isNotEmpty() && introduction.isNotEmpty() && location.isNotEmpty() && proposals.isNotEmpty() && price.isNotEmpty()) {
                if (selectedImageUri != null) {
                    uploadImageToFirebase(name, specialty, introduction, location, proposals, price)
                } else {
                    Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data
            selectedImage.setImageURI(selectedImageUri)
        }
    }

    private fun uploadImageToFirebase(name: String, specialty: String, introduction: String, location: String, proposals: String, price: String) {
        selectedImageUri?.let {
            val fileReference = storageReference.child("${UUID.randomUUID()}.jpg")
            fileReference.putFile(it).addOnSuccessListener { taskSnapshot ->
                fileReference.downloadUrl.addOnSuccessListener { uri ->
                    val newItem = Expert(name, specialty, introduction, location, proposals, price, uri.toString())
                    saveExpertToDatabase(newItem)
                }
            }.addOnFailureListener { e ->
                Toast.makeText(this@AddExpertActivity, "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveExpertToDatabase(newItem: Expert) {
        val database = FirebaseDatabase.getInstance().reference.child("experts")
        val expertId = database.push().key
        expertId?.let {
            database.child(it).setValue(newItem).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this@AddExpertActivity, "Failed to add expert: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
