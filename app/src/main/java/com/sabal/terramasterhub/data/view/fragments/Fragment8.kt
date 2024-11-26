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
import com.sabal.terramasterhub.data.view.adapter.ConsultationLogsAdminAdapter

import com.sabal.terramasterhub.data.viewmodel.Fragment8ViewModel


class Fragment8 : Fragment() {

    private lateinit var logsRecyclerView: RecyclerView
    private lateinit var viewModel: Fragment8ViewModel
    private lateinit var consultationLogsAdminAdapter: ConsultationLogsAdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_8, container, false)

        // Initialize the RecyclerView
        logsRecyclerView = rootView.findViewById(R.id.logsRecyclerView)

        // Set the layout manager for the RecyclerView
        logsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(Fragment8ViewModel::class.java)

        // Observe the LiveData from the ViewModel (now it will be a List<ConsultationLog>)
        viewModel.consultationLogs.observe(viewLifecycleOwner) { logs ->
            if (logs != null) {
                // Initialize the adapter with the list of logs
                consultationLogsAdminAdapter = ConsultationLogsAdminAdapter(logs)
                logsRecyclerView.adapter = consultationLogsAdminAdapter
            }
        }

        // Fetch the consultation logs from the ViewModel
        viewModel.getConsultationLogs()

        return rootView
    }
}