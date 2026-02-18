package com.example.finanzapro.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.finanzapro.LoginActivity
import com.example.finanzapro.MainActivity
import com.example.finanzapro.R
import com.example.finanzapro.adapter.TransactionAdapter
import com.example.finanzapro.adapter.TransactionViewModel
import com.example.finanzapro.databinding.FragmentRegisterBinding
import com.example.finanzapro.model.Transaction
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var adapter: TransactionAdapter
    private lateinit var viewModel: TransactionViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)
        viewModel = ViewModelProvider(requireActivity())[TransactionViewModel::class.java]
        configurarDatePicker()
        configurarTimePicker()
        establecerFechaHoraActual()
        cargarCategorias()

        binding.btnSave.setOnClickListener {
            if (validateTransactionFields()) {
                val transaction = Transaction(
                    amount = binding.etAmount.text.toString().toDouble(),
                    description = binding.etDescripcion.text.toString(),
                    date = binding.etFecha.text.toString(),
                    hour = binding.etHora.text.toString(),
                    category = binding.actvCategoria.text.toString(),
                    paymentMethod = when (binding.rgMetodoPago.checkedRadioButtonId) {
                        R.id.rbEfectivo -> binding.rbEfectivo.text.toString()
                        R.id.rbTarjeta -> binding.rbTarjeta.text.toString()
                        else -> ""
                    }
                )

                val userId = getUserIdOrToast()
                //lo consulte con la Ia, y dice q esta mal el ! no se si sera cierto
                if (!userId.isEmpty()) {
                    showToast("No hay usuario autenticado")
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                }
                viewModel.saveTransactions(transaction, userId, "transactions")
                showToast("Guardando datos...")
                (requireActivity() as MainActivity).replaceFragment(AnalysisFragment())
            }
        }

        //Solo lo puse para que funcione el dashboard, si no funciona o tiene otro diferente borralo
        //---------------------------------------------------------
        viewModel.transactionSelected.observe(viewLifecycleOwner){transaction ->

            transaction?.let {
                binding.actvCategoria.setText(it.category)
                binding.etAmount.setText(it.amount.toString())
                binding.etFecha.setText(it.date)
                binding.etHora.setText(it.hour)
                binding.actvCategoria.setText(it.category)
            }
        }
        //---------------------------------------------------------

    }

    fun getUserIdOrToast(): String {
        val prefs = requireContext().getSharedPreferences("personal_data", Context.MODE_PRIVATE)
        val userId = prefs.getString("userId", null)

        if (userId.isNullOrEmpty()) {
            showToast("No hay usuario autenticado")
            return ""
        }
        return userId
    }

    private fun configurarDatePicker() {
        binding.etFecha.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Seleccionar fecha")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.addOnPositiveButtonClickListener { selection ->
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC-5"))
                calendar.timeInMillis = selection
                val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                binding.etFecha.setText(formato.format(calendar.time))
            }

            datePicker.show(childFragmentManager, "DATE_PICKER")
        }

        binding.etFecha.isFocusable = false
        binding.etFecha.isClickable = true
    }

    private fun configurarTimePicker() {
        binding.etHora.setOnClickListener {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC-5"))
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H) // O CLOCK_24H para formato 24h
                .setHour(hour)
                .setMinute(minute)
                .setTitleText("Seleccionar hora")
                .build()

            timePicker.addOnPositiveButtonClickListener {
                val selectedHour = timePicker.hour
                val selectedMinute = timePicker.minute

                calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                calendar.set(Calendar.MINUTE, selectedMinute)

                val formato = SimpleDateFormat("hh:mm a", Locale.getDefault())
                formato.timeZone = TimeZone.getTimeZone("UTC-5")
                binding.etHora.setText(formato.format(calendar.time))
            }

            timePicker.show(childFragmentManager, "TIME_PICKER")
        }

        binding.etHora.isFocusable = false
        binding.etHora.isClickable = true
    }

    private fun cargarCategorias() {
        val categorias = listOf(
            "Alimentación",
            "Transporte",
            "Vivienda",
            "Entretenimiento",
            "Salud",
            "Educación",
            "Ropa",
            "Tecnología",
            "Viajes",
            "Cariñosas",
            "Otros"
        )

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            categorias
        )

        binding.actvCategoria.setAdapter(adapter)
        binding.actvCategoria.threshold = 1

        binding.actvCategoria.setOnItemClickListener { _, _, position, _ ->
            val categoriaSeleccionada = categorias[position]
            // todo
        }
    }

    private fun establecerFechaHoraActual() {
        val fechaActual = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        fechaActual.timeZone = TimeZone.getTimeZone("UTC-5")

        val horaActual = SimpleDateFormat("hh:mm a", Locale.getDefault())
        horaActual.timeZone = TimeZone.getTimeZone("UTC-5")

        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC-5"))

        binding.etFecha.setText(fechaActual.format(calendar.time))
        binding.etHora.setText(horaActual.format(calendar.time))
    }

    private fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(requireContext(), message, duration).show()
    }

    private fun validateTransactionFields(): Boolean {
        val amountText = binding.etAmount.text.toString().trim()
        val description = binding.etDescripcion.text.toString().trim()
        val date = binding.etFecha.text.toString().trim()
        val hour = binding.etHora.text.toString().trim()
        val category = binding.actvCategoria.text.toString().trim()
        val paymentMethodId = binding.rgMetodoPago.checkedRadioButtonId

        if (amountText <= 0.0.toString()) {
            showToast("El monto debe ser mayor a 0")
            return false
        }

        if (amountText.isEmpty()) {
            showToast("Ingresa un monto")
            return false
        }

        if (amountText.toDoubleOrNull() == null) {
            showToast("El monto no es válido")
            return false
        }

        if (description.isEmpty()) {
            showToast("Ingresa una descripción")
            return false
        }

        if (date.isEmpty()) {
            showToast("Selecciona una fecha")
            return false
        }

        if (hour.isEmpty()) {
            showToast("Selecciona una hora")
            return false
        }

        if (category.isEmpty()) {
            showToast("Selecciona una categoría")
            return false
        }

        if (paymentMethodId == -1) {
            showToast("Selecciona un método de pago")
            return false
        }

        return true
    }




}