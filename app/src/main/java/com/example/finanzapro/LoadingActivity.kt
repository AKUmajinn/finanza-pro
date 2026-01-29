package com.example.finanzapro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.finanzapro.databinding.LoadingBinding

class LoadingActivity : AppCompatActivity() {

    private lateinit var binding: LoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}