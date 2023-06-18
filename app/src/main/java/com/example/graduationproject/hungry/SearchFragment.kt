package com.example.graduationproject.hungry

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.*
import com.example.graduationproject.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private var foods = ArrayList<DataFood>()
    private var rvAdapter: CustomAdapterFood? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSearch.setOnClickListener {
            searchFoods()
        }

        rvAdapter = CustomAdapterFood(foods, object : CustomAdapterFood.ItemClickInterface {
            @RequiresApi(Build.VERSION_CODES.TIRAMISU)
            override fun onItemClick(data: DataFood) {
                replaceFragment(AfterSelectedItemFragment(), data)
            }
        }, "hungry")

        binding.recyclerSearchHungry.apply {
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

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun searchFoods() {
        val query = binding.fulNameETSearch.text.toString().trim()

        if (query.isNotEmpty()) {
            foods.clear()
            val allFoods = Storage.getAllFoods(requireContext())

            allFoods?.let {
                for (i in 0 until allFoods.length()) {
                    val jsonObject = allFoods.getJSONObject(i)
                    val name = jsonObject.getString(Constants.FAMILIAR_NAME)

                    if (name.contains(query,  true)) {
                        val id = jsonObject.getString(Constants.FOOD_ID)
                        var price = jsonObject.getString(Constants.PRICE)
                        var oferprice = jsonObject.getString(Constants.OFFER_PRICE)

                        val description = jsonObject.getString(Constants.DESCRIPTION)
                        val image = jsonObject.getString(Constants.IMAGE)
                        val chefEmail = jsonObject.getString(Constants.CURRENT_CHEF)
                        val offersPrice = jsonObject?.getString(Constants.OFFER_PRICE)

                        foods.add(
                            DataFood(
                                name,
                                if(oferprice == "0") price else oferprice,
                                image,
                                description,
                                id,
                                chefEmail,
                                offersPrice
                            )
                        )
                    }
                }
            }

            if (foods.isNotEmpty()) {
                binding.notFound.visibility = View.GONE
                binding.found.visibility = View.VISIBLE
            }

            rvAdapter?.notifyDataSetChanged()
        }
    }
}
