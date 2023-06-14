package com.example.graduationproject.hungry

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.Constants
import com.example.graduationproject.R
import com.example.graduationproject.Storage
import com.example.graduationproject.databinding.FragmentHomeHungryBinding

class HomeFragmentHungry : Fragment() {
    private var rvAdapter: CustomAdapterFood? = null
    private lateinit var binding: FragmentHomeHungryBinding
    private var foods = ArrayList<DataFood>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeHungryBinding.inflate(inflater, container, false)
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
                val offersPrice = jsonObject?.getString(Constants.OFFER_PRICE)
                foods.add(
                    DataFood(
                        name,
                        price,
                        image,
                        description,
                        id,
                        chefEmail,
                        offersPrice
                    )
                )
            }
        }
        if (foods.isEmpty()) {
            binding.noFoodsLayout.visibility = View.VISIBLE
            binding.mainLayout.visibility = View.GONE
        }
        rvAdapter = CustomAdapterFood(foods, object : CustomAdapterFood.ItemClickInterface {
            @RequiresApi(Build.VERSION_CODES.TIRAMISU)
            override fun onItemClick(data: DataFood) {
                replaceFragment(AfterSelectedItemFragment(), data)
            }
        },"hungry")
        binding.recyclerHomeHungry.apply {
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
        fragmentTransaction?.replace(R.id.frame_layout_hungry, fragment)
        fragmentTransaction?.commit()
    }
}