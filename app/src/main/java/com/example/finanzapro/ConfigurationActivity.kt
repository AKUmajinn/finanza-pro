package com.example.finanzapro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.finanzapro.databinding.ConfigurationBinding

class ConfigurationActivity : AppCompatActivity() {

    private lateinit var binding: ConfigurationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ConfigurationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}