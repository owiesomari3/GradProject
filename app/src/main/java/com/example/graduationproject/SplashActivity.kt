package com.example.graduationproject

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.chef.AfterLoginChefActivity
import com.example.graduationproject.enums.UserType
import com.example.graduationproject.hungry.AfterLoginHungryActivity
import org.json.JSONArray
import java.util.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setLocale(getSavedLanguage())
        Handler().postDelayed({ navigateToTheCorrectActivity() }, 2000)
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

    private fun navigateToTheCorrectActivity() {
        val jsonArray = Storage.getAlleRememberMe(this) ?: JSONArray()
        if (jsonArray.length() == 0) {
            startActivity(Intent(this@SplashActivity, MainLogRegActivity::class.java))
        } else {
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val email = jsonObject.getString(Constants.Email)
                if (email == Storage.getEmail(this)) {
                    if (jsonObject.getString(Constants.REMEMBER_ME) == "true") {
                        CacheManager.setCurrentUser(email)
                        if (jsonObject.getString(Constants.USER_TYPE) == UserType.HUNGRY.name)
                            startActivity(
                                Intent(
                                    this@SplashActivity,
                                    AfterLoginHungryActivity::class.java
                                )
                            )
                        else
                            startActivity(
                                Intent(
                                    this@SplashActivity,
                                    AfterLoginChefActivity::class.java
                                )
                            )
                    } else
                        startActivity(Intent(this@SplashActivity, MainLogRegActivity::class.java))
                }
            }
        }
    }
}