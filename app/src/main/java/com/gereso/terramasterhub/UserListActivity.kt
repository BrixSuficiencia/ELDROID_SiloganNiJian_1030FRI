package com.gereso.terramasterhub

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserListActivity : AppCompatActivity(), UserAdapter.UserClickListener {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private lateinit var userList: MutableList<User>
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        userRecyclerView = findViewById(R.id.userRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        userList = mutableListOf()
        userAdapter = UserAdapter(userList, this)
        userRecyclerView.adapter = userAdapter

        fetchUsers()
    }

    private fun fetchUsers() {
        val currentUserId = auth.currentUser?.uid
        firestore.collection("users").get().addOnSuccessListener { documents ->
            userList.clear()
            for (document in documents) {
                val userId = document.id
                if (userId != currentUserId) { // Exclude the current user's profile
                    val firstName = document.getString("firstName") ?: ""
                    val lastName = document.getString("lastName") ?: ""
                    val email = document.getString("email") ?: ""
                    val profileImageUrl = document.getString("image") ?: "" // Fetch profile image URL
                    val user = User(userId, firstName, lastName, email, profileImageUrl)
                    userList.add(user)
                }
            }
            // Sort the userList by the full name before notifying the adapter
            userList.sortBy { it.name }
            userAdapter.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Error fetching users: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onUserClicked(user: User) {
        // Get the current user ID
        val currentUserId = auth.currentUser?.uid ?: ""
        // Replace fragment with ChatFragment
        val chatFragment = ChatFragment.newInstance(user.userId, user.name, currentUserId)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, chatFragment)
            .commit()
    }
}
