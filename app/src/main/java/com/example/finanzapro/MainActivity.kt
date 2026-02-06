package com.example.finanzapro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.finanzapro.databinding.ActivityMainBinding
import com.example.finanzapro.ui.ConfigFragment
import com.example.finanzapro.ui.DashboardFragment
import com.example.finanzapro.ui.TicketsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // carga por defecto el dhasboard, esto es temp no borrarlo por el momentp
        if (savedInstanceState == null) {
            replaceFragment(DashboardFragment())
        }
        setBottomNavigation()
    }

    private fun setBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    replaceFragment(DashboardFragment())
                    true
                }
                R.id.nav_tickets -> {
                    replaceFragment(TicketsFragment())
                    true
                }
                R.id.nav_register -> {
                    replaceFragment(AnalysisFragment())
                    true
                }
                R.id.nav_config -> {
                    replaceFragment(ConfigFragment())
                    true
                }
                else -> false
            }
        }
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_container, fragment)
        fragmentTransaction.commit()
    }
}