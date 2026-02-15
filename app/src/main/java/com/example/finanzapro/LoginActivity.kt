package com.example.finanzapro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.finanzapro.databinding.ActivityLoginBinding
import com.example.finanzapro.ui.SingInFragment

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.login_container, SingInFragment())
        fragmentTransaction.commit()
    }
}