package com.sabal.terramasterhub.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sabal.terramasterhub.R


class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Set the default fragment programmatically
        if (savedInstanceState == null) {
            loadFragment(Fragment1())
        }

        // Set listener for bottom navigation item selection
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_fragment1 -> loadFragment(Fragment1())
                R.id.nav_fragment2 -> loadFragment(Fragment2())
                R.id.nav_fragment3 -> loadFragment(Fragment3())
                R.id.nav_fragment4 -> loadFragment(Fragment4())
                R.id.nav_fragment5 -> loadFragment(Fragment5())
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
