package com.example.graduationproject

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setLocale(getSavedLanguage())
        Handler().postDelayed(
            {
                startActivity(Intent(this@SplashActivity, MainLogRegActivity::class.java))
            },
            2000
        )
    }

    private fun setLocale(localeName: String?) {
        val configuration = resources.configuration
        configuration.setLocale(Locale(localeName))
        resources.updateConfiguration(configuration, resources.displayMetrics)
        saveLanguage(localeName)
    }

    private fun saveLanguage(lang: String?) {
        val sharedPref = getSharedPreferences("my_preferences_file", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("language", lang)
        editor.apply()
    }

    private fun getSavedLanguage(): String? {
        val sharedPref = getSharedPreferences("my_preferences_file", Context.MODE_PRIVATE)
        return sharedPref.getString("language", "en")
    }
}