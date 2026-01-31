package com.example.finanzapro

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.finanzapro.databinding.ActivityRegisterBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarDatePicker()
        configurarTimePicker()
        establecerFechaHoraActual()
        cargarCategorias()
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

            datePicker.show(supportFragmentManager, "DATE_PICKER")
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

            timePicker.show(supportFragmentManager, "TIME_PICKER")
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
            "Otros"
        )

        val adapter = ArrayAdapter(
            this,
            R.layout.simple_dropdown_item_1line,
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
}