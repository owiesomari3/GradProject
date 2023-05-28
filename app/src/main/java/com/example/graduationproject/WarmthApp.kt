package com.example.graduationproject

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import java.util.Locale

class WarmthApp : Application() {

    override fun onCreate() {
        super.onCreate()
       // setLocale()
    }

    private fun getSavedLanguage(): String? {
        val sharedPref = getSharedPreferences("my_preferences_file", Context.MODE_PRIVATE)
        return sharedPref.getString("language", "en")
    }

    private fun setLocale() {
        val context = applicationContext
        val locale = Locale(getSavedLanguage())
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context.resources.apply {
            updateConfiguration(config, displayMetrics)
        }
    }
}