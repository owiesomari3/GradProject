package com.example.graduationproject.chef

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.graduationproject.*
import com.example.graduationproject.databinding.ActivityLoginChefBinding
import com.example.graduationproject.enums.UserType
import org.json.JSONArray
import org.json.JSONObject

class LoginChefActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener,
    View.OnKeyListener {
    lateinit var binding: ActivityLoginChefBinding

    @SuppressLint("MissingInflatedId", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginChefBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            etEmail.onFocusChangeListener = this@LoginChefActivity
            etPass.onFocusChangeListener = this@LoginChefActivity
            btnLoginChef.setOnClickListener {
                val userEmail = Storage.getUserByEmailAndPassword(this@LoginChefActivity,etEmail.text.toString(), etPass.text.toString(),UserType.CHEF)
                userEmail?.let {
                    CacheManager.setUserType(UserType.CHEF)
                    CacheManager.setCurrentUser(it)
                    incorrectPassword.visibility = View.GONE
                    checkAndSaveRememberMe()
                    Storage.saveEmail(this@LoginChefActivity, binding.etEmail.text.toString())
                    startActivity(
                        Intent(
                            this@LoginChefActivity,
                            AfterLoginChefActivity::class.java
                        )
                    )
                } ?: run {
                    incorrectPassword.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun checkAndSaveRememberMe() {
        val jsonArray = Storage.getAlleRememberMe(this) ?: JSONArray()
        if (jsonArray.length() == 0) {
            val jsonObject = JSONObject()
            jsonObject.put(Constants.REMEMBER_ME, binding.remeberMerChef.isChecked.toString())
            jsonObject.put(Constants.Email, binding.etEmail.text.toString())
            jsonObject.put(Constants.USER_TYPE, UserType.CHEF.name)
            jsonArray.put(jsonObject)
        } else {
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                if (isUserFound())
                    jsonObject.put(
                        Constants.REMEMBER_ME,
                        binding.remeberMerChef.isChecked.toString()
                    )
                else {
                    val jsonObject2 = JSONObject()
                    jsonObject2.put(
                        Constants.REMEMBER_ME,
                        binding.remeberMerChef.isChecked.toString()
                    )
                    jsonObject2.put(Constants.Email, binding.etEmail.text.toString())
                    jsonObject2.put(Constants.USER_TYPE, UserType.CHEF.name)
                    jsonArray.put(jsonObject2)
                    jsonArray.put(jsonObject)
                }
            }
        }

        Storage.saveRememberMe(this, jsonArray)
    }

    private fun isUserFound(): Boolean {
        var found = false
        val jsonArray = Storage.getAlleRememberMe(this) ?: JSONArray()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val email = jsonObject.get(Constants.Email)
            if (email == binding.etEmail.text.toString()) found = true
        }
        return found
    }

    private fun validEmail(): Boolean {
        var errorMessage: String? = null
        val value: String = binding.etEmail.text.toString()
        if (value.isEmpty()) errorMessage = getString(R.string.FULL_NAME_IS_REQUIRED)
        else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) errorMessage =
            getString(R.string.EMAIL_ADDRESS_IS_INVALID)
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
        if (value.isEmpty()) errorMessage = getString(R.string.FULL_NAME_IS_REQUIRED)
        else if (value.length < 8) errorMessage =
            getString(R.string.PASSWORD_MUST_BE_8_CHARACTERS_LONG)
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

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Check if the current activity is the one you want to handle differently
        // Perform custom back action for the selected activity
        val intent = Intent(this, MainLogRegActivity::class.java)
        startActivity(intent)
        finish() // Optionally, you can finish the current activity
    }
}






