package com.example.graduationproject.chef

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.CacheManager
import com.example.graduationproject.Constants
import com.example.graduationproject.R
import com.example.graduationproject.Storage
import com.example.graduationproject.databinding.FragmentHomeChefBinding
import com.example.graduationproject.hungry.AfterSelectedItemFragment
import com.example.graduationproject.hungry.CustomAdapterFood
import com.example.graduationproject.hungry.DataFood

class HomeFragmentChef : Fragment() {
    private var rvAdapter: CustomAdapterFood? = null
    private lateinit var binding: FragmentHomeChefBinding
    var foods = ArrayList<DataFood>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeChefBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val allFoods = Storage.getAllFoods(requireContext())

        allFoods?.let {
            for (i in allFoods.length() - 1 downTo 0) {
                val jsonObject = allFoods.getJSONObject(i)
                val id = jsonObject.getString(Constants.FOOD_ID)
                val name = jsonObject.getString(Constants.FAMILIAR_NAME)
                val price = jsonObject.getString(Constants.PRICE)
                val description = jsonObject.getString(Constants.DESCRIPTION)
                val image = jsonObject.getString(Constants.IMAGE)
                val chefEmail = jsonObject.getString(Constants.CURRENT_CHEF)
                val offerPrice = jsonObject?.getString(Constants.OFFER_PRICE)

                if (jsonObject.getString(Constants.CURRENT_CHEF) == CacheManager.getCurrentUser())
                    foods.add(
                        DataFood(
                            name,
                            price,
                            image,
                            5.0,
                            description,
                            id,
                            chefEmail,
                            offerPrice
                        )
                    )
            }
        }

        rvAdapter = CustomAdapterFood(foods, object : CustomAdapterFood.ItemClickInterface {
            @RequiresApi(Build.VERSION_CODES.TIRAMISU)
            override fun onItemClick(data: DataFood) {
                replaceFragment(AfterSelectedItemFragment(), data)
            }
        },"chef")

        binding.recyclerHomeChef.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = rvAdapter
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun replaceFragment(fragment: Fragment, data: DataFood) {
        val data1 = Bundle()
        data1.putParcelable("food", data)
        fragment.arguments = data1
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.frame_layout_chef, fragment)
        fragmentTransaction?.commit()
    }
}
