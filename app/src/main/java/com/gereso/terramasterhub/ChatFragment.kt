package com.gereso.terramasterhub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore

class ChatFragment : Fragment() {
    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: MutableList<Message>
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: Button
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUserId: String
    private lateinit var receiverId: String

    companion object {
        private const val ARG_RECEIVER_ID = "receiverId"
        private const val ARG_RECEIVER_NAME = "receiverName"
        private const val ARG_CURRENT_USER_ID = "currentUserId"

        fun newInstance(receiverId: String, receiverName: String, currentUserId: String): ChatFragment {
            val fragment = ChatFragment()
            val args = Bundle()
            args.putString(ARG_RECEIVER_ID, receiverId)
            args.putString(ARG_RECEIVER_NAME, receiverName)
            args.putString(ARG_CURRENT_USER_ID, currentUserId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        receiverId = arguments?.getString(ARG_RECEIVER_ID) ?: ""
        auth = FirebaseAuth.getInstance()
        currentUserId = arguments?.getString(ARG_CURRENT_USER_ID) ?: ""

        messageRecyclerView = view.findViewById(R.id.messageRecyclerView)
        messageRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        messageList = mutableListOf()
        messageAdapter = MessageAdapter(messageList, currentUserId)
        messageRecyclerView.adapter = messageAdapter

        messageEditText = view.findViewById(R.id.messageEditText)
        sendButton = view.findViewById(R.id.sendButton)

        database = FirebaseDatabase.getInstance().getReference("messages")

        fetchMessages()

        sendButton.setOnClickListener {
            val messageText = messageEditText.text.toString()
            if (messageText.isNotEmpty()) {
                sendMessage(currentUserId, receiverId, messageText)
                messageEditText.text.clear()
            }
        }
    }

    private fun sendMessage(senderId: String, receiverId: String, messageText: String) {
        // Get the current user's name
        val currentUserRef = FirebaseFirestore.getInstance().collection("users").document(senderId)
        currentUserRef.get().addOnSuccessListener { documentSnapshot ->
            val userName = documentSnapshot.getString("firstName") + " " + documentSnapshot.getString("lastName")

            // Create the Message object with the sender's name
            val message = Message(senderId, userName, receiverId, messageText, System.currentTimeMillis())

            // Send the message to the database
            val messageId = database.push().key ?: return@addOnSuccessListener
            database.child(messageId).setValue(message).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Message sent", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to send message", Toast.LENGTH_SHORT).show()
                }
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(requireContext(), "Failed to get current user information: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchMessages() {
        database.orderByChild("timestamp").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for (messageSnapshot in snapshot.children) {
                    val message = messageSnapshot.getValue(Message::class.java)
                    if (message != null &&
                        ((message.senderId == currentUserId && message.receiverId == receiverId) ||
                                (message.senderId == receiverId && message.receiverId == currentUserId))) {
                        messageList.add(message)
                    }
                }
                messageAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to retrieve messages", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
