package com.example.finanzapro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.finanzapro.databinding.DashboardBinding

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: DashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}