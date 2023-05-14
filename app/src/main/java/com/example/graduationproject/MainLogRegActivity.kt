package com.example.graduationproject

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.example.graduationproject.chef.LoginChefActivity
import com.example.graduationproject.databinding.ActivityMainLogRegBinding
import com.example.graduationproject.hungry.LoginHungryActivity
import java.util.*
import kotlin.system.exitProcess

class MainLogRegActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    lateinit var binding: ActivityMainLogRegBinding

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        when (position) {
            0 -> setLocale("Select language")
            1 -> setLocale("en")
            2 -> setLocale("ar")
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {}

    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainLogRegBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val list = arrayOf("Select language", "English", "العربية")
        val adapter = ArrayAdapter(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            list
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinner.adapter = adapter


        binding.spinner.post { binding.spinner.onItemSelectedListener = this }

        binding.btnLoginMain.setOnClickListener {

            showDialogBOX()
        }
        binding.btnRegMain.setOnClickListener {
            startActivity(Intent(this, RegisterALLActivity::class.java))
        }
    }

    override fun onPause() {
        super.onPause()
        binding.animationWelcome.pauseAnimation()

    }


    private fun showDialogBOX() {
        val dialog=Dialog(this)
       dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.logindialog)
       val cancel :ImageView=dialog.findViewById(R.id.cancel)
        val btnChef:RadioButton=dialog.findViewById(R.id.reg_chef)
        val btnHungry:RadioButton=dialog.findViewById(R.id.reg_hungry)
        btnChef.setOnClickListener{
            startActivity(Intent(this,LoginChefActivity::class.java))
        }
        btnHungry.setOnClickListener{
            startActivity(Intent(this,LoginHungryActivity::class.java))
        }
        cancel.setOnClickListener{
            dialog.cancel()
        }
        dialog.show()
    }

    private fun setLocale(localeName: String) {
        val confic = resources.configuration
        confic.setLocale(Locale(localeName))
        resources.updateConfiguration(confic, resources.displayMetrics)
        recreate()
    }


}


