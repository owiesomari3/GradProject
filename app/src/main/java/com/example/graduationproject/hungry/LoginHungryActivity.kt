package com.example.graduationproject.hungry

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.CacheManager
import com.example.graduationproject.Constants
import com.example.graduationproject.R
import com.example.graduationproject.Storage
import com.example.graduationproject.databinding.ActivityLoginHungryBinding
import com.example.graduationproject.enums.UserType
import org.json.JSONArray
import org.json.JSONObject

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
                val userEmail = Storage.getUserByEmailAndPassword(
                    this@LoginHungryActivity,
                    etEmail.text.toString(),
                    etPass.text.toString(),
                    UserType.HUNGRY
                )
                userEmail?.let {
                    CacheManager.setUserType(UserType.HUNGRY)
                    CacheManager.setCurrentUser(it)
                    incorrectPassword.visibility = View.GONE
                    checkAndSaveRememberMe()
                    Storage.saveEmail(this@LoginHungryActivity, binding.etEmail.text.toString())
                    startActivity(
                        Intent(
                            this@LoginHungryActivity,
                            AfterLoginHungryActivity::class.java
                        )
                    )
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

    private fun checkAndSaveRememberMe() {
        val jsonArray = Storage.getAlleRememberMe(this) ?: JSONArray()
        if (jsonArray.length() == 0) {
            val jsonObject = JSONObject()
            jsonObject.put(Constants.REMEMBER_ME, binding.rememberMeHungry.isChecked.toString())
            jsonObject.put(Constants.Email, binding.etEmail.text.toString())
            jsonObject.put(Constants.USER_TYPE, UserType.HUNGRY.name)
            jsonArray.put(jsonObject)
        } else {
            var isUserExist = false
            var jsonObject = JSONObject()
            for (i in 0 until jsonArray.length()) {
                jsonObject = jsonArray.getJSONObject(i)
                val email = jsonObject.get(Constants.Email)
                if (email == binding.etEmail.text.toString()) {
                    isUserExist = true
                    break
                }
            }

            if (isUserExist) {
                jsonObject.put(Constants.REMEMBER_ME, binding.rememberMeHungry.isChecked.toString())
            } else {
                val newJsonObject = JSONObject()
                newJsonObject.put(Constants.REMEMBER_ME, binding.rememberMeHungry.isChecked.toString())
                newJsonObject.put(Constants.Email, binding.etEmail.text.toString())
                newJsonObject.put(Constants.USER_TYPE, UserType.HUNGRY.name)
                jsonArray.put(newJsonObject)
            }
        }

        Storage.saveRememberMe(this, jsonArray)
    }

    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }
}