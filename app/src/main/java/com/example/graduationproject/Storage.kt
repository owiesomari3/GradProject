package com.example.graduationproject

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
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
}