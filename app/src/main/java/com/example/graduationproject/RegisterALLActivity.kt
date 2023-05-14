package com.example.graduationproject

import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.chef.LoginChefActivity
import com.example.graduationproject.databinding.ActivityRegisterBinding
import com.example.graduationproject.hungry.LoginHungryActivity

class RegisterALLActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener,
    View.OnKeyListener {

    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.btnRegisterInApp.setOnClickListener {
            binding.animationBee.pauseAnimation()
            dialogReg()
        }

        binding.apply {
            fulNameETReg.onFocusChangeListener = this@RegisterALLActivity
            emailETReg.onFocusChangeListener = this@RegisterALLActivity
            passETReg.onFocusChangeListener = this@RegisterALLActivity
            confPassETReg.onFocusChangeListener = this@RegisterALLActivity
        }
    }

    private fun dialogReg() {
        val dialogRegister=Dialog(this)
        dialogRegister.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogRegister.setCancelable(false)
        dialogRegister.setContentView(R.layout.dialog_reg)
        val btnChef:RadioButton=dialogRegister.findViewById(R.id.reg_chef)
        val btnHungry:RadioButton=dialogRegister.findViewById(R.id.reg_hungry)
        val cancel :ImageView=dialogRegister.findViewById(R.id.cancel)
        btnChef.setOnClickListener{
            startActivity(Intent(this,LoginChefActivity::class.java))
        }
        btnHungry.setOnClickListener{
            startActivity(Intent(this,LoginHungryActivity::class.java))
        }
        cancel.setOnClickListener{
            dialogRegister.cancel()
        }
        dialogRegister.show()
    }


    private fun validFullName(): Boolean {
        var errorMessage: String? = null
        val value: String = binding.fulNameETReg.text.toString()
        if (value.isEmpty()) errorMessage = "Full Name is required"
        if (errorMessage != null) {
            binding.fulNameTilReg.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    private fun validEmail(): Boolean {
        var errorMessage: String? = null
        val value: String = binding.emailETReg.text.toString()
        if (value.isEmpty()) errorMessage = "Email is required"
        else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) errorMessage =
            "Email address is invalid"
        if (errorMessage != null) {
            binding.emailTilReg.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    private fun validPass(): Boolean {
        var errorMessage: String? = null
        val value: String = binding.passETReg.text.toString()
        if (value.isEmpty()) errorMessage = "password is required"
        else if (value.length < 10) errorMessage = "Password must be 10 characters long"
        if (errorMessage != null) {
            binding.passTilReg.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    private fun validPassConf(): Boolean {
        var errorMessage: String? = null
        val password: String = binding.passETReg.text.toString()
        val confirmPass: String = binding.passETReg.text.toString()
        if (confirmPass.isEmpty()) errorMessage = "confirm password is required"
        else if (confirmPass.length < 6) errorMessage =
            " Confirm Password must be 6 characters long"
        else if (confirmPass != password) errorMessage =
            "Confirm password doesn't match with Password"
        if (errorMessage != null) {
            binding.confPassTilReg.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    override fun onClick(view: View?) {}

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        binding.apply {
            view?.let {
                when (view.id) {
                    R.id.ful_name_ET_reg -> {
                        if (hasFocus) {
                            if (emailTilReg.isErrorEnabled) {
                                fulNameTilReg.isErrorEnabled = false
                            }
                        } else {
                            validFullName()
                        }
                    }

                    R.id.pass_ET_reg -> {
                        if (hasFocus) {
                            if (passTilReg.isErrorEnabled) {
                                passTilReg.isErrorEnabled = false
                            }
                        } else {
                            if (validPass() && validPassConf() && confPassETReg.text!!.isNotEmpty()) {
                                if (passTilReg.isErrorEnabled) {
                                    passTilReg.isErrorEnabled = false
                                }
                                passTilReg.isStartIconVisible = false
                                passTilReg.apply {
                                    setStartIconDrawable(R.drawable.check_circle_24)
                                    setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                                }
                            }
                        }
                    }

                    R.id.email_ET_reg -> {
                        if (hasFocus) {
                            if (emailTilReg.isErrorEnabled) {
                                emailTilReg.isErrorEnabled = false
                            }
                        } else {
                            if (validEmail()) {

                            }
                        }
                    }

                    R.id.conf_pass_ET_reg -> {
                        if (hasFocus) {
                            if (confPassTilReg.isErrorEnabled) {
                                confPassTilReg.isErrorEnabled = false
                            }
                        } else {
                            if (validPassConf() && validPass()) {
                                if (confPassTilReg.isErrorEnabled) {
                                    passTilReg.isErrorEnabled = false
                                }
                                passTilReg.isStartIconVisible = false
                                passTilReg.apply {
                                    setStartIconDrawable(R.drawable.check_circle_24)
                                    setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onKey(view: View?, keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }
}
