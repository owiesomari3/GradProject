package com.example.graduationproject.hungry

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.LinearLayout
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityLoginHungryBinding
import com.google.android.material.textfield.TextInputEditText

class LoginHungryActivity: AppCompatActivity() ,View.OnClickListener, View.OnFocusChangeListener,
    View.OnKeyListener  {
    @SuppressLint("MissingInflatedId")
   lateinit var binding:ActivityLoginHungryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginHungryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLogHungry.setOnClickListener {
            startActivity(Intent(this,AfterLoginHungryActivity::class.java))
        }
        binding.apply {
            binding.etEmail.onFocusChangeListener = this@LoginHungryActivity
            binding.etPass.onFocusChangeListener = this@LoginHungryActivity
        }

    }
    private fun validEmail(): Boolean {
        var errorMessage: String? = null
        val value: String = binding.etEmail.text.toString()
        if (value.isEmpty()) errorMessage = "Email is required"
        else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) errorMessage =
            "Email address is invalid"
        if (errorMessage != null) {
            binding.tilEmail.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    private fun validPass(): Boolean {
        var errorMessage: String? = null
        val value: String = binding.etPass.text.toString()
        if (value.isEmpty()) errorMessage = "password is required"
        else if (value.length < 10) errorMessage = "Password must be 10 characters long"
        if (errorMessage != null) {
            binding.tilPass.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    override fun onClick(v: View?) {}

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        binding.apply {
            view?.let {
                when (view.id) {
                    R.id.et_email -> {
                        if (hasFocus) {
                            if (tilEmail.isErrorEnabled) {
                                tilEmail.isErrorEnabled = false
                            }
                        } else {
                            if (validEmail()) {

                            }
                        }
                    }
                    R.id.et_pass -> {
                        if (hasFocus) {
                            if (tilPass.isErrorEnabled) {
                                tilPass.isErrorEnabled = false
                            }
                        } else {
                            if (validPass()) {

                            }
                        }
                    }
                }
            }
        }

    }
    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        TODO("Not yet implemented")
    }

}