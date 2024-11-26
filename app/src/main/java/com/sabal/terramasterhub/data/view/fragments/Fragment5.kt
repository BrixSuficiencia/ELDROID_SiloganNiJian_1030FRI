package com.sabal.terramasterhub.data.view.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sabal.terramasterhub.R
import com.sabal.terramasterhub.data.repositories.DashboardRepository
import com.sabal.terramasterhub.data.view.adapter.UpdateAdapter
import com.sabal.terramasterhub.data.viewmodel.Fragment5ViewModel


class Fragment5 : Fragment() {

    private lateinit var updatesRecyclerView: RecyclerView
    private lateinit var viewModel: Fragment5ViewModel
    private lateinit var updateAdapter: UpdateAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_5, container, false)

        updatesRecyclerView = view.findViewById(R.id.recyclerViewUpdates)
        updatesRecyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize the DashboardAdapter with an empty list
        updateAdapter = UpdateAdapter(requireContext(), listOf())

        updatesRecyclerView.adapter = updateAdapter

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(Fragment5ViewModel::class.java)

        // Observe updates data from ViewModel
        viewModel.dashboardData.observe(viewLifecycleOwner) { updates ->
            updateAdapter.updateUpdates(updates)
        }

        // Observe error state
        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
        }

        // Fetch updates from ViewModel
        viewModel.fetchDashboard()
        return view

    }
}