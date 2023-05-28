package com.example.graduationproject.hungry

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.graduationproject.CacheManager
import com.example.graduationproject.Constants
import com.example.graduationproject.Storage
import com.example.graduationproject.databinding.FragmentAfterSelectedItemBinding
import org.json.JSONArray
import org.json.JSONObject

class AfterSelectedItemFragment : Fragment() {

    private lateinit var binding: FragmentAfterSelectedItemBinding
    private var dataFood: DataFood? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAfterSelectedItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataFood = arguments?.getParcelable("food")

        fillViews()
        binding.orderItem.setOnClickListener {
            val jsonArray = Storage.allOrder(requireContext()) ?: JSONArray()
            val jsonObjectOrder = JSONObject()
            jsonObjectOrder.put(Constants.FOOD_ID,dataFood?.foodId)
            jsonObjectOrder.put(Constants.CURRENT_CHEF,dataFood?.chefEmail)
            jsonObjectOrder.put(Constants.User,CacheManager.getCurrentUser())
            jsonObjectOrder.put(Constants.QUANTITY,binding.editQuantity.text.toString())
            jsonArray.put(jsonObjectOrder)
            saveAllOrder(jsonArray)
        }
    }

    private fun saveAllOrder(jsonArray:JSONArray) {
        val sharedPreferences = activity?.getSharedPreferences(Constants.ORDER, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString(Constants.ORDER_LIST, jsonArray.toString())
        editor?.apply()
    }

    private fun fillViews() {
        val decodedBytes = Base64.decode(dataFood?.image, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        binding.apply {
            imageFood.setImageBitmap(bitmap)
            tvFamiliarName.text = dataFood?.familiar_name
            tvDescribtion.text = dataFood?.description
            tvPrice.text = dataFood?.price
            tvRate.text = dataFood?.rate.toString()
        }
    }
}