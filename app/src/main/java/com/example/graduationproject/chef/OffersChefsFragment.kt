package com.example.graduationproject.chef

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.*
import com.example.graduationproject.databinding.FragmentOffersChefsBinding
import com.example.graduationproject.hungry.AfterSelectedItemFragment
import com.example.graduationproject.hungry.CustomAdapterFood
import com.example.graduationproject.hungry.DataFood

class OffersChefsFragment : Fragment() {
    private var offerAdapter: CustomAdapterFood? = null
    private lateinit var binding: FragmentOffersChefsBinding
    var offerFoods = ArrayList<DataFood>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOffersChefsBinding.inflate(inflater, container, false)
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
                val offerPrice = jsonObject.getString(Constants.OFFER_PRICE)
                if (jsonObject.getString(Constants.CURRENT_CHEF) == CacheManager.getCurrentUser()
                    &&jsonObject.getString(Constants.OFFER_PRICE) !="0")
                    offerFoods.add(
                        DataFood(
                            name,
                            price,
                            image,
                            description,
                            id,
                            chefEmail,
                            offerPrice
                        )
                    )
            }
        }
        if (offerFoods.isEmpty()) {
            binding.noOffersLayout.visibility = View.VISIBLE
            binding.mainLayout.visibility = View.GONE
        }

        offerAdapter = CustomAdapterFood(offerFoods, object : CustomAdapterFood.ItemClickInterface {
            @RequiresApi(Build.VERSION_CODES.TIRAMISU)
            override fun onItemClick(data: DataFood) {
                replaceFragment(AfterSelectedItemFragment(), data)
            }
        },"offers_chef", activity as AppCompatActivity?)

        binding.recyclerOfferChef.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = offerAdapter
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