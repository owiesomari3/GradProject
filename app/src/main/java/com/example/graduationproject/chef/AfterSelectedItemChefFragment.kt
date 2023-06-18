package com.example.graduationproject.chef

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.graduationproject.Constants
import com.example.graduationproject.R
import com.example.graduationproject.Storage
import com.example.graduationproject.Util
import com.example.graduationproject.databinding.FragmentAfterSelectedItimeChefBinding
import com.example.graduationproject.hungry.DataFood
import org.json.JSONArray

class AfterSelectedItemChefFragment : Fragment() {

    private lateinit var binding: FragmentAfterSelectedItimeChefBinding
    private var dataFood: DataFood? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAfterSelectedItimeChefBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataFood = arguments?.getParcelable("food")
        fillViews()
        setUpEditFields()
        binding.btnUpdate.setOnClickListener {
            val allFoods = Storage.getAllFoods(requireContext()) ?: JSONArray()
            allFoods.let {
                for (i in 0 until allFoods.length()) {
                    val jsonObjectUpdate = allFoods.getJSONObject(i)
                    if (jsonObjectUpdate.getString(Constants.FOOD_ID) == dataFood?.foodId) {
                        jsonObjectUpdate.put(
                            Constants.FAMILIAR_NAME,
                            binding.editFamiliarName.text.toString()
                        )
                        jsonObjectUpdate.put(
                            Constants.DESCRIPTION,
                            binding.editDescribtion.text.toString()
                        )
                    }
                }
            }
            Storage.saveAllFoodsList(requireContext(), allFoods)
            Toast.makeText(
                context,
                "Update the data Successfully",
                Toast.LENGTH_LONG
            ).show()
            replaceFragment(HomeFragmentChef())
        }
    }

    private fun setUpEditFields() {
        binding.apply {
            editDescribtion.doOnTextChanged { _, _, _, _ ->
                setButtonEnability()
            }
            editFamiliarName.doOnTextChanged { _, _, _, _ ->
                setButtonEnability()
            }
        }
    }

    private fun setButtonEnability() {
        binding.apply {
            btnUpdate.isEnabled =
                editDescribtion.text.isNotEmpty() == true && editDescribtion.text.isNotEmpty() == true
        }
    }

    private fun fillViews() {
        val decodedBytes = Base64.decode(dataFood?.image, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        binding.apply {
            imageFood.setImageBitmap(bitmap)
            tvPrice.text =  Util.currencyFormat(dataFood?.price.toString())
        }

        binding.apply {
            editFamiliarName.setText(dataFood?.familiar_name)
            editDescribtion.setText(dataFood?.description)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction: FragmentTransaction? = fragmentManager?.beginTransaction()
        fragmentTransaction?.add(R.id.frame_layout_chef, fragment)
        fragmentTransaction?.commit()
    }
}
