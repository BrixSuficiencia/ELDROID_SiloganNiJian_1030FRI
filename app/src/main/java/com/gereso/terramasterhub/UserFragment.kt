package com.gereso.terramasterhub

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.gereso.terramasterhub.databinding.FragmentUserBinding

class UserFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        fetchAndPopulateUserInfo()

        val logoutBtn: Button = binding.logoutBtn
        val imageButton = binding.imageButton

        fetchAndPopulateUserInfo()

        logoutBtn.setOnClickListener {
            auth.signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // Set up click listener for the image button
        imageButton.setOnClickListener {
            // Handle click on the image button
            val intent = Intent(activity, EditUserActivity::class.java)
            startActivity(intent)
        }

        // Find the notification icon in the layout
        val notificationIcon = binding.appBar.notificationIcon

        // Set up click listener for the notification icon
        notificationIcon.setOnClickListener {
            // Handle click on the notification icon
            val intent = Intent(activity, UserListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchAndPopulateUserInfo() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val docRef = db.collection("users").document(userId)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val firstName = document.getString("firstName")
                        val lastName = document.getString("lastName")
                        val username = document.getString("nickname")
                        val email = document.getString("email")
                        val userImage = document.getString("image") // Assuming "image" is the key for the user's image URL

                        // Combine first name and last name
                        val name = if (firstName != null && lastName != null) {
                            "$firstName $lastName"
                        } else {
                            "Name not available"
                        }

                        // Set the values to TextViews
                        binding.textView4.text = name
                        binding.textView5.text = username
                        binding.textView7.text = email

                        // Set user image using Glide
                        if (userImage != null && userImage != "noprofile") {
                            // Load user's image using Glide into the ImageView
                            Glide.with(this@UserFragment)
                                .load(userImage)
                                .placeholder(R.drawable.noprofile) // Placeholder image while loading
                                .error(R.drawable.noprofile) // Error image if loading fails
                                .into(binding.imageView6)
                        } else {
                            // If user image is null or "noprofile", use default image
                            binding.imageView6.setImageResource(R.drawable.noprofile)
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    // Handle any errors
                }
        } else {
            // Set default values if user is not logged in
            binding.textView4.text = "Name not available"
            binding.textView5.text = "Username not available"
            binding.textView7.text = "Email not available"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
