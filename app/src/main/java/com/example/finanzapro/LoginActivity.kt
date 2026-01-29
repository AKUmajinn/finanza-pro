package com.example.finanzapro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.finanzapro.databinding.LoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}