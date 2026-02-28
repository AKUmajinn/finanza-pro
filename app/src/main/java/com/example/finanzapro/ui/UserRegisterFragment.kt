package com.example.finanzapro.ui

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.finanzapro.LoginActivity
import com.example.finanzapro.R
import com.example.finanzapro.databinding.FragmentUserRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class UserRegisterFragment : Fragment(R.layout.fragment_user_register) {
    var _binding: FragmentUserRegisterBinding? = null
    val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentUserRegisterBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRegister.setOnClickListener {
            registerUser()
        }
        binding.tvGoToLogin.setOnClickListener {
            (requireActivity() as LoginActivity).replaceFragment(SingInFragment())
        }
    }

    private fun registerUser() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etRegisterEmail.text.toString().trim()
        val password = binding.etRegisterPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()

        if (name.isEmpty()) {
            binding.etName.error = "Ingresa tu nombre"
            binding.etName.requestFocus()
            return
        }

        if (email.isEmpty()) {
            binding.etRegisterEmail.error = "Ingresa tu correo"
            binding.etRegisterEmail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etRegisterEmail.error = "Correo inválido"
            binding.etRegisterEmail.requestFocus()
            return
        }

        if (password.length < 6) {
            binding.etRegisterPassword.error = "La contraseña debe tener al menos 6 caracteres"
            binding.etRegisterPassword.requestFocus()
            return
        }

        if (password != confirmPassword) {
            binding.etConfirmPassword.error = "Las contraseñas no coinciden"
            binding.etConfirmPassword.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Registro exitoso", Toast.LENGTH_LONG).show()
                    auth.signOut()
                    requireContext().getSharedPreferences("personal_data", Context.MODE_PRIVATE)
                        .edit().clear().apply()
                    (requireActivity() as LoginActivity).replaceFragment(SingInFragment())
                } else {
                    Toast.makeText(requireContext(),
                        task.exception?.message ?: "Error al registrar usuario",
                        Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



