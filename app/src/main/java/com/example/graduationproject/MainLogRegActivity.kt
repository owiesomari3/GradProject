package com.example.graduationproject

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.chef.LoginChefActivity
import com.example.graduationproject.databinding.ActivityMainLogRegBinding

import com.example.graduationproject.hungry.LoginHungryActivity
import java.util.*

class MainLogRegActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private lateinit var binding:ActivityMainLogRegBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainLogRegBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val list = arrayOf(getString(R.string.SELECT_LANGUAGE), "English", "العربية")
        val adapter = ArrayAdapter(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            list
        )
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)

        binding.apply {
            spinner.adapter = adapter
            spinner.post { binding.spinner.onItemSelectedListener = this@MainLogRegActivity }
            btnLoginMain.setOnClickListener { showDialogBOX() }
            btnRegMain.setOnClickListener { startActivity(Intent(this@MainLogRegActivity,
                RegisterALLActivity::class.java)) }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        when (position) {
            0 -> setLocale("Select language")
            1 -> setLocale(Constants.EN)
            2 -> setLocale(Constants.AR)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {}

    override fun onPause() {
        super.onPause()
        binding.animationWelcome.pauseAnimation()
    }

    private fun showDialogBOX() {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.logindialog, null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogView.apply {
            val cancel: ImageView = findViewById(R.id.cancel)
            val btnChef: RadioButton = findViewById(R.id.reg_chef)
            val btnHungry: RadioButton = findViewById(R.id.reg_hungry)
            dialog.apply {
                btnChef.setOnClickListener {
                    dismiss()
                    startActivity(Intent(this@MainLogRegActivity, LoginChefActivity::class.java))
                }

                btnHungry.setOnClickListener {
                    dismiss()
                    startActivity(Intent(this@MainLogRegActivity, LoginHungryActivity::class.java))
                }

                cancel.setOnClickListener {
                    dismiss()
                }

                show()
            }
        }
    }

    private fun setLocale(localeName: String?) {
        val configuration = resources.configuration
        configuration.setLocale(localeName?.let { Locale(it) })
        resources.updateConfiguration(configuration, resources.displayMetrics)
        saveLanguage(localeName)
        recreate()
    }

    private fun saveLanguage(lang: String?) {
        val sharedPref = getSharedPreferences("my_preferences_file", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("language", lang)
        editor.apply()
    }
}