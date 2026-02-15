package com.example.finanzapro.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.finanzapro.MainActivity
import com.example.finanzapro.R
import com.example.finanzapro.databinding.FragmentSingInBinding
import com.google.firebase.auth.FirebaseAuth


class SingInFragment : Fragment(R.layout.fragment_sing_in)  {

    var _binding: FragmentSingInBinding? = null
    val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSingInBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val prefs =  requireContext().getSharedPreferences("personal_data", Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.apply()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val currentUser = auth.currentUser
                        prefs.putString("userId", currentUser?.uid)
                        prefs.apply()
                        println("User login: $currentUser?.uid")
                        showToast("Login exitoso")

                        val intent = Intent(requireContext(), MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)

                    } else {
                        println("Error de Session: ${task.exception?.message}")
                        showToast("Error: Correo o Clave Invalida!")
                    }
                }

        } else {
            showToast("Completa todos los campos")
        }
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        println("User: ${currentUser?.uid}")
        if (currentUser != null) {
            startActivity(Intent(requireContext(), MainActivity::class.java))
            requireActivity().finish()
        }
    }
    private fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(requireContext(), message, duration).show()
    }
}