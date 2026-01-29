package com.example.finanzapro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.finanzapro.databinding.MyTicketsBinding

class TicketsActivity : AppCompatActivity() {

    private lateinit var binding: MyTicketsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MyTicketsBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}