package com.sabal.terramasterhub.data.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sabal.terramasterhub.R
import com.sabal.terramasterhub.data.view.fragments.Fragment1
import com.sabal.terramasterhub.data.view.fragments.Fragment5
import com.sabal.terramasterhub.data.view.fragments.Fragment6

class ExpertSurveyorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expert_surveyor)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_expert_surveyor)

        // Set the default fragment programmatically
        if (savedInstanceState == null) {
            loadFragment(Fragment1())
        }
        // Set listener for bottom navigation item selection
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_profile -> loadFragment(Fragment1())
                R.id.nav_consultations -> loadFragment(Fragment6())
                R.id.nav_dashboard -> loadFragment(Fragment5())
                else -> false
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment): Boolean {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
        return true
    }

}