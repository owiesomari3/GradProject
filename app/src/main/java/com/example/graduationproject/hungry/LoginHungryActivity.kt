package com.example.graduationproject.hungry

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import com.example.graduationproject.CacheManager
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityLoginHungryBinding
import com.example.graduationproject.enums.UserType

class LoginHungryActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener,
    View.OnKeyListener {
    @SuppressLint("MissingInflatedId")
    lateinit var binding: ActivityLoginHungryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginHungryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.apply {
            etEmail.onFocusChangeListener = this@LoginHungryActivity
            etPass.onFocusChangeListener = this@LoginHungryActivity

            btnLogHungry.setOnClickListener {
                val user = CacheManager.getUserByEmailAndPassword(etEmail.text.toString(), etPass.text.toString())
                user?.let {
                    CacheManager.setUserType(UserType.HUNGRY)
                    incorrectPassword.visibility = View.GONE
                    startActivity(Intent(this@LoginHungryActivity, AfterLoginHungryActivity::class.java))
                } ?: run {
                    incorrectPassword.visibility = View.VISIBLE
                }
            }
        }
    }
    private fun validEmail(): Boolean {
        var errorMessage: String? = null
        val value: String = binding.etEmail.text.toString()
        if (value.isEmpty()) errorMessage =getString(R.string.FULL_NAME_IS_REQUIRED)
        else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) errorMessage =getString(R.string.EMAIL_ADDRESS_IS_INVALID)
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
        if (value.isEmpty()) errorMessage =getString(R.string.FULL_NAME_IS_REQUIRED)
        else if (value.length < 8) errorMessage = getString(R.string.Confirm_PASSWORD_MUST_BE_8_CHARACTERS_LONG)
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
                            validEmail()

                        }
                    }

                    R.id.et_pass -> {
                        if (hasFocus) {
                            if (tilPass.isErrorEnabled) {
                                tilPass.isErrorEnabled = false
                            }
                        } else validPass()
                    }
                }
            }
        }
    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
     return false
    }

}