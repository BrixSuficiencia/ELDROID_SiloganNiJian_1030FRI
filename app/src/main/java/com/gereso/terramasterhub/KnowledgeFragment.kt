package com.gereso.terramasterhub

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class KnowledgeFragment : Fragment(), KnowledgeAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: KnowledgeAdapter
    private val knowledgeList = ArrayList<Knowledge>()
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_knowledge, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = KnowledgeAdapter(knowledgeList, this)
        recyclerView.adapter = adapter

        database = FirebaseDatabase.getInstance().getReference("knowledge")

        fetchData()

        val addButton: Button = view.findViewById(R.id.addButton)
        addButton.setOnClickListener {
            startActivity(Intent(context, AddKnowledgeActivity::class.java))
        }

        val appBar = view.findViewById<View>(R.id.appBar)

        // Find the notification_icon within the appBar layout
        val notificationIcon = appBar.findViewById<ImageButton>(R.id.notification_icon)
        notificationIcon.setOnClickListener {
            val intent = Intent(activity, UserListActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun fetchData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                knowledgeList.clear() // Clear the list to avoid duplication
                val newItems = mutableListOf<Knowledge>()
                if (snapshot.exists()) {
                    for (knowledgeSnapshot in snapshot.children) {
                        val knowledge = knowledgeSnapshot.getValue(Knowledge::class.java)
                        if (knowledge != null) {
                            newItems.add(knowledge)
                        }
                    }
                    knowledgeList.addAll(newItems)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onItemClick(knowledge: Knowledge) {
        val intent = Intent(context, KnowledgeDetailActivity::class.java)
        intent.putExtra("title", knowledge.title)
        intent.putExtra("description", knowledge.description)
        intent.putExtra("imageResId", knowledge.imageResId)
        startActivity(intent)
    }
}

