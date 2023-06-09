package com.example.graduationproject.chef

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.graduationproject.*
import com.example.graduationproject.databinding.FragmentAddFoodBinding
import org.json.JSONArray
import org.json.JSONObject

class AddFoodFragment : Fragment() {
    private lateinit var binding: FragmentAddFoodBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val image = arguments?.getParcelable<Bitmap>("image")
        val imageUri = arguments?.getParcelable<Uri>("imageUri")

        binding.apply {
            if (image != null) img.setImageBitmap(image)
            else if (imageUri != null) img.setImageURI(imageUri)
            save.setOnClickListener {
                val jsonArray = Storage.getAllFoods(requireContext()) ?: JSONArray()
                val jsonObject = JSONObject()
                jsonObject.put(Constants.FOOD_ID, generateFoodId(jsonArray))
                jsonObject.put(Constants.CURRENT_CHEF, CacheManager.getCurrentUser())
                jsonObject.put(Constants.FAMILIAR_NAME, familiarName.text.toString())
                jsonObject.put(Constants.PRICE, price.text.toString())
                jsonObject.put(Constants.DESCRIPTION, description.text.toString())
                image?.let { jsonObject.put(Constants.IMAGE, Storage.convertBitmapToBase64(it)) }
                imageUri?.let { jsonObject.put(Constants.IMAGE, Storage.convertImageUriToBase64(requireContext().contentResolver, it)) }
                jsonArray.put(jsonObject)
                Storage.saveAllFoodsList(requireContext(), jsonArray)
                Util.showToastMsg(requireContext() ,R.string.FOOD_ADDED_SUCCESSFULLY )
            }
        }
    }

    private fun generateFoodId(jsonArray: JSONArray): Int {
        return if (jsonArray.length() == 0) {
            1
        } else {
            val jsonObject=  jsonArray.getJSONObject(jsonArray.length()-1)//get last object in the array
            jsonObject.getInt(Constants.FOOD_ID) +1
        }
    }
}
