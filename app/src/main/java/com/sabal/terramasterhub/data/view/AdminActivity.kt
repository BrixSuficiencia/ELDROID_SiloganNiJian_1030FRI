package com.sabal.terramasterhub.data.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sabal.terramasterhub.R
import com.sabal.terramasterhub.data.view.fragments.Fragment1
import com.sabal.terramasterhub.data.view.fragments.Fragment2
import com.sabal.terramasterhub.data.view.fragments.Fragment3
import com.sabal.terramasterhub.data.view.fragments.Fragment4
import com.sabal.terramasterhub.data.view.fragments.Fragment5
import com.sabal.terramasterhub.data.view.fragments.Fragment7
import com.sabal.terramasterhub.data.view.fragments.Fragment8
import com.sabal.terramasterhub.data.view.fragments.Fragment9

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Set the default fragment programmatically
        if (savedInstanceState == null) {
            loadFragment(Fragment1()) // Default to Profile
        }

        // Set listener for bottom navigation item selection
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_profile -> loadFragment(Fragment1())
                R.id.nav_dashboard -> loadFragment(Fragment7())
                R.id.nav_consultations_logs -> loadFragment(Fragment8())
                R.id.nav_users -> loadFragment(Fragment9())
                else -> false
            }
            true
        }
    }

    // Helper function to load fragments dynamically
    private fun loadFragment(fragment: Fragment): Boolean {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
        return true
    }
}