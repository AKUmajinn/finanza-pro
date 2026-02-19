package com.example.finanzapro.utils

import android.content.Context

object SessionManager {

    fun getUserId(context: Context): String? {
        val prefs = context.getSharedPreferences("personal_data", Context.MODE_PRIVATE)
        return prefs.getString("userId", null)
    }

}