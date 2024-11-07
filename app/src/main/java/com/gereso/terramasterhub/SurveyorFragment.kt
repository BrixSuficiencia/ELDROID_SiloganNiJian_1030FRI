package com.gereso.terramasterhub

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SurveyorFragment : Fragment(), SurveyorAdapter.OnItemClickListener {

    private val ADD_EXPERT_REQUEST_CODE = 1

    private lateinit var itemList: MutableList<Surveyor>
    private lateinit var adapter: SurveyorAdapter
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = FirebaseDatabase.getInstance().getReference("surveyors")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_surveyor, container, false)

        itemList = mutableListOf()
        val recyclerView: RecyclerView = view.findViewById(R.id.expertrecycler)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = SurveyorAdapter(itemList, this)
        recyclerView.adapter = adapter

        val addButton: Button = view.findViewById(R.id.AddExprtbtn)
        addButton.setOnClickListener {
            val intent = Intent(context, AddSurveyorActivity::class.java)
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
                for (surveyorSnapshot in snapshot.children) {
                    val surveyor = surveyorSnapshot.getValue(Surveyor::class.java)
                    surveyor?.let { itemList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onItemClick(item: Surveyor) {
        val intent = Intent(context, SurveyorDetailActivity::class.java).apply {
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

    override fun onMessageClick(item: Surveyor) {
        val intent = Intent(activity, UserListActivity::class.java)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_EXPERT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            fetchData()
            Toast.makeText(context, "Surveyor added successfully", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun newInstance(param1: String, param2: String): SurveyorFragment {
            return SurveyorFragment().apply {
                arguments = Bundle().apply {
                    putString("param1", param1)
                    putString("param2", param2)
                }
            }
        }
    }
}
