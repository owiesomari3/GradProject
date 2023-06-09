package com.example.graduationproject

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.airbnb.lottie.LottieAnimationView
import com.example.graduationproject.chef.LoginChefActivity
import com.example.graduationproject.databinding.ActivityRegisterBinding
import com.example.graduationproject.enums.UserType
import com.example.graduationproject.hungry.LoginHungryActivity
import org.json.JSONArray
import org.json.JSONObject

class RegisterALLActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener,
    View.OnKeyListener {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.apply {
            btnRegisterInApp.setOnClickListener {
                binding.animationBee.pauseAnimation()
                dialogReg()
            }

            fulNameETReg.onFocusChangeListener = this@RegisterALLActivity
            emailETReg.onFocusChangeListener = this@RegisterALLActivity
            passETReg.onFocusChangeListener = this@RegisterALLActivity
            confPassETReg.onFocusChangeListener = this@RegisterALLActivity
            phoneETReg.onFocusChangeListener = this@RegisterALLActivity
        }

        setUpEditFields()
    }

    @SuppressLint("MissingInflatedId")
    private fun dialogReg() {
        val dialogRegister = Dialog(this)
        dialogRegister.apply() {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(R.layout.dialog_reg)
            val btnChef: RadioButton = findViewById(R.id.reg_chef)
            val btnHungry: RadioButton = findViewById(R.id.reg_hungry)
            val cancel: ImageView = findViewById(R.id.cancel)
            val logo: LottieAnimationView = findViewById(R.id.animationregtrue)
            btnChef.setOnClickListener {
                addNewUser(
                    binding.fulNameETReg.text.toString(),
                    binding.emailETReg.text.toString(),
                    binding.passETReg.text.toString(),
                    binding.phoneETReg.text.toString(),
                    UserType.CHEF
                )
                startActivity(Intent(this@RegisterALLActivity, LoginChefActivity::class.java))
            }
            btnHungry.setOnClickListener {
                addNewUser(
                    binding.fulNameETReg.text.toString(),
                    binding.emailETReg.text.toString(),
                    binding.passETReg.text.toString(),
                    binding.phoneETReg.text.toString(),
                    UserType.HUNGRY
                )
                startActivity(Intent(this@RegisterALLActivity, LoginHungryActivity::class.java))
            }
            cancel.setOnClickListener { cancel() }
            show()
            Handler().postDelayed(
                {
                    logo.pauseAnimation()
                },
                4850
            )
        }
    }

    private fun setUpEditFields() {
        binding.apply {
            fulNameETReg.doOnTextChanged { _, _, _, _ ->
                setButtonEnability()
            }

            emailETReg.doOnTextChanged { _, _, _, _ ->
                setButtonEnability()
            }

            passETReg.doOnTextChanged { _, _, _, _ ->
                setButtonEnability()
            }

            confPassETReg.doOnTextChanged { _, _, _, _ ->
                setButtonEnability()
            }
        }
    }

    private fun setButtonEnability() {
        binding.apply {
            btnRegisterInApp.isEnabled =
                fulNameETReg.text?.isNotEmpty() == true && validEmail() && validPass() && validateConf() && validFullName() && validateConfAndPass() && validPhone()
        }
    }

    private fun validFullName(): Boolean {
        var errorMessage: String? = null
        val value: String = binding.fulNameETReg.text.toString()
        val fullNamePattern = Regex("^[a-zA-Z ]+\$")
        if (value.isEmpty())
            errorMessage = getString(R.string.FULL_NAME_IS_REQUIRED)
        else if (!fullNamePattern.matches(value))
            errorMessage =
                getString(R.string.INVALIDFULLNAMEONLYALPHABETICALCHARACTERSAREALLOWED)

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
        if (value.isEmpty())
            errorMessage = getString(R.string.FULL_NAME_IS_REQUIRED)
        else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches())
            errorMessage = getString(R.string.EMAIL_ADDRESS_IS_INVALID)

        if (errorMessage != null) {
            binding.emailTilReg.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    private fun validPhone(): Boolean {
        var errorMessage: String? = null
        val value: String = binding.phoneETReg.text.toString()
        val phonePattern = Regex("^[+][0-9]{10,13}\$")
        Log.d("owies", value)
        if (value.isEmpty())
            errorMessage = getString(R.string.FULL_NAME_IS_REQUIRED)
        else if (!phonePattern.matches(value))
            errorMessage = getString(R.string.Correct_Format92xxxxxxxxxx)

        if (errorMessage != null) {
            binding.phoneTilReg.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        } else {
            binding.phoneTilReg.isErrorEnabled = false
        }
        return errorMessage == null
    }

    private fun validPass(): Boolean {
        var errorMessage: String? = null
        val value: String = binding.passETReg.text.toString()
        if (value.isEmpty()) errorMessage = getString(R.string.FULL_NAME_IS_REQUIRED)
        else if (value.length < 8) errorMessage =
            getString(R.string.PASSWORD_MUST_BE_8_CHARACTERS_LONG)
        if (errorMessage != null) {
            binding.passTilReg.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    private fun validateConf(): Boolean {
        var errorMessage: String? = null
        val value: String = binding.passETReg.text.toString()
        if (value.isEmpty()) errorMessage = getString(R.string.FULL_NAME_IS_REQUIRED)
        else if (value.length < 8) errorMessage =
            getString(R.string.Confirm_PASSWORD_MUST_BE_8_CHARACTERS_LONG)
        if (errorMessage != null) {
            binding.confPassTilReg.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }

    private fun validateConfAndPass(): Boolean {
        var errorMessage: String? = null
        val password: String = binding.passETReg.text.toString()
        val confirmPass: String = binding.confPassETReg.text.toString()
        if (confirmPass != password) errorMessage =
            getString(R.string.CONFIRM_PASSWORD_DOESNOT_MATCH_WITH_PASSWORD)
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
            if (view != null) {
                when (view.id) {
                    R.id.ful_name_ET_reg -> {
                        if (hasFocus) {
                            if (fulNameTilReg.isErrorEnabled) {
                                fulNameTilReg.isErrorEnabled = false
                            }
                        } else validFullName()
                    }

                    R.id.email_ET_reg -> {
                        if (hasFocus) {
                            if (emailTilReg.isErrorEnabled) {
                                emailTilReg.isErrorEnabled = false
                            }
                        } else validEmail()
                    }

                    R.id.phone_ET_reg -> {
                        if (hasFocus) {
                            if (phoneTilReg.isErrorEnabled) {
                                phoneTilReg.isErrorEnabled = false
                            }
                        } else validPhone()
                    }

                    R.id.pass_ET_reg -> {
                        if (hasFocus) {
                            if (passTilReg.isErrorEnabled) {
                                passTilReg.isErrorEnabled = false
                            }
                        } else {
                            if (validPass() && validateConf() && confPassETReg.text!!.isNotEmpty() && validateConfAndPass()) {
                                if (passTilReg.isErrorEnabled) {
                                    passTilReg.isErrorEnabled = false
                                }
                                passTilReg.isStartIconVisible = false
                                passTilReg.apply {
                                    setStartIconDrawable(R.drawable.check_circle_24)
                                    setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                                }
                            } else validPass()
                        }
                    }

                    R.id.conf_pass_ET_reg -> {
                        if (hasFocus) {
                            if (confPassTilReg.isErrorEnabled) {
                                confPassTilReg.isErrorEnabled = false
                            }
                        } else {
                            if (validateConf() && validPass() && validateConfAndPass()) {
                                if (confPassTilReg.isErrorEnabled) {
                                    passTilReg.isErrorEnabled = false
                                }
                                passTilReg.isStartIconVisible = false
                                passTilReg.apply {
                                    setStartIconDrawable(R.drawable.check_circle_24)
                                    setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                                }
                            } else validateConf()
                        }
                    }
                }
            }
        }
    }

    override fun onKey(view: View?, keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }

    private fun addNewUser(
        name: String,
        email: String,
        password: String,
        phoneNumber: String,
        type: UserType
    ) {
        val jsonArrayUser = Storage.getAllUsers(this@RegisterALLActivity) ?: JSONArray()
        if (!isEmailExist(email)) {
            val jsonObject = JSONObject()
            jsonObject.put(Constants.USER_ID, generateUserId(jsonArrayUser))
            jsonObject.put(Constants.FULL_NAME, name)
            jsonObject.put(Constants.Email, email)
            jsonObject.put(Constants.Phone, phoneNumber)
            jsonObject.put(Constants.PASSWORD, password)
            jsonObject.put(Constants.USER_TYPE, type.name)
            jsonArrayUser.put(jsonObject)
            saveAllUsersList(jsonArrayUser)
        }
    }

    private fun generateUserId(jsonUserArray: JSONArray): Int {
        return if (jsonUserArray.length() == 0) {
            1
        } else {
            val jsonObject =
                jsonUserArray.getJSONObject(jsonUserArray.length() - 1)//get last object in the array
            jsonObject.getInt(Constants.USER_ID) + 1
        }
    }

    private fun saveAllUsersList(jsonArray: JSONArray) {
        val sharedPreferences = getSharedPreferences(Constants.INFO_USERS_SHARED_PREFS, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(Constants.USER_LIST, jsonArray.toString())
        editor.apply()
    }

    private fun isEmailExist(emailValue: String): Boolean {
        var errorMessage = ""
        // Check if the new email stored before
        val sharedUsers = Storage.getAllUsers(this@RegisterALLActivity)
        if (sharedUsers != null) {
            for (i in 0 until sharedUsers.length()) {
                val userObject = sharedUsers.getJSONObject(i)
                val email = userObject.optString(Constants.Email, "")
                if (email.equals(emailValue, ignoreCase = true)) {
                    errorMessage = getString(R.string.EMAIL_ALREADY_REGISTERED)
                    break
                }
            }
            return if (errorMessage != "") {
                binding.emailTilReg.apply {
                    isErrorEnabled = true
                    error = errorMessage
                }
                true
            } else false
        } else {
            return false
        }
    }
}