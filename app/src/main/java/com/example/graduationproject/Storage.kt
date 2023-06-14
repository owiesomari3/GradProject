package com.example.graduationproject

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import com.example.graduationproject.enums.UserType
import org.json.JSONArray
import java.io.ByteArrayOutputStream
import java.io.InputStream

object Storage {

    fun getAllFoods(context: Context): JSONArray? {
        val sharedPreferences = context.getSharedPreferences(
            "mainSharedPrefs",
            Context.MODE_PRIVATE
        )
        val jsonString = sharedPreferences.getString(Constants.FOODS_LIST, null)
        return if (jsonString != null) {
            try {
                JSONArray(jsonString)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else {
            null
        }
    }

    fun saveAllFoodsList(context: Context, jsonArray: JSONArray) {
        val jsonString = jsonArray.toString()
        val sharedPreferences =
            context.getSharedPreferences("mainSharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(Constants.FOODS_LIST, jsonString)
        editor.apply()
    }

    fun convertBitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }


    fun convertImageUriToBase64(contentResolver: ContentResolver, imageUri: Uri): String? {
        var inputStream: InputStream? = null
        try {
            inputStream = contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            return convertBitmapToBase64(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
        }
        return null
    }

    fun getAllUsers(context: Context): JSONArray? {
        val sharedPreferences = context.getSharedPreferences(
            Constants.INFO_USERS_SHARED_PREFS,
            Context.MODE_PRIVATE
        )
        val jsonString = sharedPreferences.getString(Constants.USER_LIST, null)
        return if (jsonString != null) {
            try {
                JSONArray(jsonString)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else {
            null
        }
    }

    fun allOrder(context: Context): JSONArray? {
        val sharedPreferences = context.getSharedPreferences(
            Constants.ORDER,
            Context.MODE_PRIVATE
        )
        val jsonString = sharedPreferences.getString(Constants.ORDER_LIST, null)
        return if (jsonString != null) {
            try {
                JSONArray(jsonString)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else {
            null
        }
    }

    fun getAllWallet(context: Context): JSONArray? {
        val sharedPreferences = context.getSharedPreferences(
            Constants.WALLET,
            Context.MODE_PRIVATE
        )
        val jsonString = sharedPreferences.getString(Constants.WALLET_List, null)
        return if (jsonString != null) {
            try {
                JSONArray(jsonString)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else {
            null
        }
    }

    fun getUserByEmailAndPassword(
        context: Context,
        email: String,
        password: String,
        type: UserType
    ): String? {
        val users = getAllUsers(context) ?: JSONArray()
        for (i in 0 until users.length()) {
            val userObject = users.getJSONObject(i)
            val userEmail = userObject.getString(Constants.Email)
            val userPassword = userObject.getString(Constants.PASSWORD)
            val userType = userObject.getString(Constants.USER_TYPE)
            if (email.equals(userEmail, true) && password == userPassword && type.name == userType)
                return email
        }
        return null
    }

    fun saveRememberMe(context: Context, jsonArray: JSONArray) {
        val jsonString = jsonArray.toString()
        val sharedPreferences =
            context.getSharedPreferences("mainSharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(Constants.REMEMBER_ME, jsonString)
        editor.apply()
    }

    fun getAlleRememberMe(context: Context): JSONArray? {
        val sharedPreferences = context.getSharedPreferences(
            "mainSharedPrefs",
            Context.MODE_PRIVATE
        )
        val jsonString = sharedPreferences.getString(Constants.REMEMBER_ME, null)
        return if (jsonString != null) {
            try {
                JSONArray(jsonString)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } else {
            null
        }
    }

    fun saveEmail(context: Context, email: String) {
        val sharedPreferences =
            context.getSharedPreferences("mainSharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(Constants.Email, email)
        editor.apply()
    }

    fun getEmail(context: Context): String? {
        val sharedPreferences =
            context.getSharedPreferences("mainSharedPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString(Constants.Email, null)
    }
}