package com.example.graduationproject.chef

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.CacheManager
import com.example.graduationproject.Constants
import com.example.graduationproject.Storage
import com.example.graduationproject.databinding.FragmentOrderChefBinding
import org.json.JSONArray

class OrdersChefsFragment : Fragment() {

    private lateinit var binding: FragmentOrderChefBinding
    private lateinit var orderAdapter: CustomOrderAdapterChef
    private var dataOrder = ArrayList<OrderChef>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderChefBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val allOrders = Storage.allOrder(requireContext())
        val allFoods = Storage.getAllFoods(requireContext()) ?: JSONArray()
        allOrders?.let {
            for (position in 0 until allOrders.length()) {
                val jsonObjectOrder = allOrders.getJSONObject(position)
                val foodOrderId = jsonObjectOrder.getString(Constants.FOOD_ID)
               // val userEmail = jsonObjectOrder.getString(Constants.User)
                val chefEmail = jsonObjectOrder?.getString(Constants.CURRENT_CHEF)
                val quantity = jsonObjectOrder?.getString(Constants.QUANTITY)
                val userEmail =  jsonObjectOrder?.getString(Constants.User)
                if (CacheManager.getCurrentUser() == chefEmail) {
                    for (i in 0 until allFoods.length()) {
                        val jsonObjectDataFood = allFoods.getJSONObject(i)
                        val foodId = jsonObjectDataFood?.getString(Constants.FOOD_ID)
                        if (foodOrderId == foodId) {
                            val familiarName = jsonObjectDataFood?.getString(Constants.FAMILIAR_NAME)
                            val price = jsonObjectDataFood?.getString(Constants.PRICE)
                            val image = jsonObjectDataFood?.getString(Constants.IMAGE)
                            dataOrder.add(
                                OrderChef(
                                    familiarName,
                                    price,
                                    image,
                                    quantity,
                                    userEmail,
                                    foodOrderId
                                )
                            )
                        }
                    }
                }
            }
        }
        orderAdapter = CustomOrderAdapterChef(dataOrder)
        binding.recyclerOrderChef.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter
        }
    }
}


