package com.gereso.terramasterhub

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ExpertFragment : Fragment(), ExpertAdapter.OnItemClickListener, ExpertAdapter.OnMessageClickListener {

    private val ADD_EXPERT_REQUEST_CODE = 1

    private lateinit var itemList: MutableList<Expert>
    private lateinit var adapter: ExpertAdapter
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().getReference("experts")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_expert, container, false)

        itemList = mutableListOf()
        val recyclerView: RecyclerView = view.findViewById(R.id.expertrecycler)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ExpertAdapter(itemList, this, this)
        recyclerView.adapter = adapter

        val addButton: Button = view.findViewById(R.id.AddExprtbtn)
        addButton.setOnClickListener {
            val intent = Intent(context, AddExpertActivity::class.java)
            startActivityForResult(intent, ADD_EXPERT_REQUEST_CODE)
        }

        val searchEditText: EditText = view.findViewById(R.id.SearchBar)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                s?.toString()?.let { filterItems(it) }
            }
        })

        fetchData()

        val appBar = view.findViewById<View>(R.id.appBar)

        // Find the notification_icon within the appBar layout
        val notificationIcon = appBar.findViewById<ImageButton>(R.id.notification_icon)
        notificationIcon.setOnClickListener {
            val intent = Intent(activity, UserListActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun filterItems(query: String) {
        val filteredList = itemList.filter { it.name.contains(query, ignoreCase = true) }
        adapter.submitList(filteredList)
    }

    private fun fetchData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                itemList.clear()
                for (expertSnapshot in snapshot.children) {
                    try {
                        val expert = expertSnapshot.getValue(Expert::class.java)
                        expert?.let { itemList.add(it) }
                    } catch (e: DatabaseException) {
                        // Log the snapshot key and value that caused the issue
                        Log.e("FetchDataError", "Error parsing expert at ${expertSnapshot.key}: ${expertSnapshot.value}", e)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onItemClick(item: Expert) {
        val intent = Intent(context, ExpertDetailActivity::class.java).apply {
            putExtra("Name", item.name)
            putExtra("Specialty", item.specialty)
            putExtra("Introduction", item.introduction)
            putExtra("Location", item.location)
            putExtra("Proposals", item.proposals)
            putExtra("Price", item.price)
            putExtra("ImageUrl", item.imageUrl)
        }
        startActivity(intent)
    }

    override fun onMessageClick(item: Expert) {
        val intent = Intent(context, UserListActivity::class.java)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_EXPERT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            fetchData()
            Toast.makeText(context, "Expert added successfully", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun newInstance(param1: String, param2: String): ExpertFragment {
            return ExpertFragment().apply {
                arguments = Bundle().apply {
                    putString("param1", param1)
                    putString("param2", param2)
                }
            }
        }
    }
}
