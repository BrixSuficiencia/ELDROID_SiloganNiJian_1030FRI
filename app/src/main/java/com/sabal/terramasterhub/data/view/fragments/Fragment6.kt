package com.sabal.terramasterhub.data.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sabal.terramasterhub.R
import com.sabal.terramasterhub.data.view.adapter.ConsultationRequestAdapter
import com.sabal.terramasterhub.data.viewmodel.Fragment6ViewModel

class Fragment6 : Fragment() {

    private lateinit var viewModel: Fragment6ViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ConsultationRequestAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_6, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewConsultations)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize Adapter (Assume an existing adapter class)
        adapter = ConsultationRequestAdapter(
            requests = listOf(),
            onAccept = { request -> viewModel.acceptRequest(request) },  // Define accept action in ViewModel
            onDecline = { request -> viewModel.declineRequest(request) }  // Define decline action in ViewModel
        )
        recyclerView.adapter = adapter

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(Fragment6ViewModel::class.java)

        // Observe LiveData for Expert Requests
        viewModel.expertRequests.observe(viewLifecycleOwner) { requests ->
            adapter.updateData(requests)  // Update RecyclerView data when it changes
        }

        // Observe LiveData for Surveyor Requests
        viewModel.surveyorRequests.observe(viewLifecycleOwner) { requests ->
            adapter.updateData(requests)  // Update RecyclerView data when it changes
        }

        // Fetch requests based on the user type
        viewModel.fetchRequestsBasedOnUserType()

        return view
    }


}
