package com.example.graduationproject.hungry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.*
import com.example.graduationproject.databinding.FragmentCartBinding
import com.example.graduationproject.enums.OrderStatus
import org.json.JSONArray

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var orderAdapter: CustomOrderAdapter
    private var dataOrder = ArrayList<Order>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
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
                val userEmail = jsonObjectOrder.getString(Constants.User)
                val quantity = jsonObjectOrder?.getString(Constants.QUANTITY)
                val orderId = jsonObjectOrder.getString(Constants.ORDER_ID)
                val isOrderRated = jsonObjectOrder.getString(Constants.IS_ORDER_RATED)

                if (CacheManager.getCurrentUser() == userEmail) {
                    for (i in 0 until allFoods.length()) {
                        val jsonObjectDataFood = allFoods.getJSONObject(i)
                        val foodId = jsonObjectDataFood?.getString(Constants.FOOD_ID)
                        if (foodOrderId == foodId) {
                            val familiarName = jsonObjectDataFood?.getString(Constants.FAMILIAR_NAME)
                            val price = jsonObjectDataFood?.getString(Constants.PRICE)
                            val offerPrice = jsonObjectDataFood?.getString(Constants.OFFER_PRICE)
                            val image = jsonObjectDataFood?.getString(Constants.IMAGE)
                            val description = jsonObjectDataFood?.getString(Constants.DESCRIPTION)
                            val chefEmail = jsonObjectDataFood?.getString(Constants.CURRENT_CHEF)
                            val hungryPhone = getChefPhone(chefEmail)
                            val status = jsonObjectOrder?.getString(Constants.ORDER_STATUS)
                            if (price != null) {
                                dataOrder.add(
                                    Order(
                                        familiarName,
                                        if(offerPrice == "0") price.toDouble() else offerPrice?.toDouble(),
                                        image,
                                        description,
                                        foodOrderId,
                                        chefEmail,
                                        quantity,
                                        status,
                                        hungryPhone,
                                        orderId,
                                        isOrderRated
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
        val filteredData: ArrayList<Order> = ArrayList()

        dataOrder.forEach {
            if(it.status == OrderStatus.DONE.name && it.isOrderRated == "false") filteredData.add(it)
        }

        dataOrder.forEach {
            if(it.status != OrderStatus.DONE.name && it.status != OrderStatus.CANCELED.name) filteredData.add(it)
        }

        if(filteredData.isEmpty()){
            binding.noFoodsLayout.visibility = View.VISIBLE
            binding.mainLayout.visibility = View.GONE
        }

        orderAdapter = CustomOrderAdapter(dataOrder)
        binding.recyclerOrderHungry.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                val homeFragmentHungry = HomeFragmentHungry()
                fragmentTransaction.replace(R.id.frame_layout_hungry, homeFragmentHungry)
                fragmentTransaction.commit()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    private fun getChefPhone(email: String?): String {
        var phoneNumber = ""
        val allUsers = Storage.getAllUsers(requireContext()) ?: JSONArray()
        for (i in 0 until allUsers.length()) {
            val json = allUsers.getJSONObject(i)
            if (json.getString(Constants.Email) == email)
                phoneNumber = json.getString(Constants.Phone)
        }
        return phoneNumber
    }
}
