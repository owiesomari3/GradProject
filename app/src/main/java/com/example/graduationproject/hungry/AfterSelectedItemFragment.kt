package com.example.graduationproject.hungry

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.CacheManager
import com.example.graduationproject.Constants
import com.example.graduationproject.R
import com.example.graduationproject.Storage
import com.example.graduationproject.databinding.FragmentAfterSelectedItemBinding

class AfterSelectedItemFragment : Fragment() {
    private lateinit var binding: FragmentAfterSelectedItemBinding
    private var asadapter: CustomAdapterFood? = null
    var foods = ArrayList<DataFood>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val allFoods = Storage.getAllFoods(requireContext())

        allFoods?.let {
            for (i in allFoods.length() - 1 downTo 0) {
                val jsonObject = allFoods.getJSONObject(i)
                val name = jsonObject.getString(Constants.FAMILIAR_NAME)
                val price = jsonObject.getString(Constants.PRICE)
                val description = jsonObject.getString(Constants.DESCRIPTION)
                val image = jsonObject.getString(Constants.IMAGE)
                //val quantity = jsonObject.optString(Constants.QUANTITY, "0") // Use a default value if "quantity" is missing
                if (jsonObject.getString(Constants.CURRENT_CHEF) == CacheManager.getCurrentChef())
                    foods.add(DataFood(name, price, image, 5.0, description))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_after_selected_item, container, false)
    }




}