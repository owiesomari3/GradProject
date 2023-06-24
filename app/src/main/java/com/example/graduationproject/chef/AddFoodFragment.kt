package com.example.graduationproject.chef

import android.graphics.Bitmap
import android.media.audiofx.AudioEffect.Descriptor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.widget.doOnTextChanged
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val image = arguments?.getParcelable<Bitmap>("image")
        val imageUri = arguments?.getParcelable<Uri>("imageUri")

        binding.apply {
            if (image != null) img.setImageBitmap(image)
            else if (imageUri != null) img.setImageURI(imageUri)
            setUpEditFields()
            save.setOnClickListener {
                val jsonArray = Storage.getAllFoods(requireContext()) ?: JSONArray()
                val jsonObject = JSONObject()
                jsonObject.put(Constants.FOOD_ID, generateFoodId(jsonArray))
                jsonObject.put(Constants.CURRENT_CHEF, CacheManager.getCurrentUser())
                jsonObject.put(Constants.FAMILIAR_NAME, familiarName.text.toString())
                jsonObject.put(Constants.PRICE, price.text.toString())
                jsonObject.put(Constants.DESCRIPTION, description.text.toString())
                jsonObject.put(Constants.OFFER_PRICE, "0")

                image?.let { jsonObject.put(Constants.IMAGE, Storage.convertBitmapToBase64(it)) }
                imageUri?.let {
                    jsonObject.put(
                        Constants.IMAGE,
                        Storage.convertImageUriToBase64(requireContext().contentResolver, it)
                    )
                }
                jsonArray.put(jsonObject)
                Storage.saveAllFoodsList(requireContext(), jsonArray)
                Util.showToastMsg(requireContext(), R.string.FOOD_ADDED_SUCCESSFULLY)
                replaceFragment(HomeFragmentChef())
            }
        }
    }

    private fun generateFoodId(jsonArray: JSONArray): Int {
        return if (jsonArray.length() == 0) {
            1
        } else {
            val jsonObject =
                jsonArray.getJSONObject(jsonArray.length() - 1)//get last object in the array
            jsonObject.getInt(Constants.FOOD_ID) + 1
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.frame_layout_chef, fragment)
        fragmentTransaction?.commit()
    }

    private fun setUpEditFields() {
        binding.apply {
            familiarName.doOnTextChanged { _, _, _, _ ->
                setButtonEnability()
            }
            price.doOnTextChanged { _, _, _, _ ->
                setButtonEnability()
            }
            description.doOnTextChanged { _, _, _, _ ->
                setButtonEnability()
            }
        }
    }

    private fun setButtonEnability() {
        binding.apply {
            familiarName.apply {
                error = if (text.isEmpty()) {
                    "Please enter a familiar name"
                } else {
                    null
                }
            }

            price.apply {
                error = if (text.isEmpty()) {"Please enter price"
                } else {
                    null
                }
            }

            description.apply {
                error = if (text.isEmpty()) {
                    "Please enter description"
                } else {
                    null
                }
            }

            save.isEnabled =
                familiarName.text.isNotEmpty() && description.text.isNotEmpty() && price.text.toString()
                    .toDouble() > 0
        }
    }

}

