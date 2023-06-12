package com.example.graduationproject.hungry

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.graduationproject.*
import com.example.graduationproject.databinding.FragmentAfterSelectedItemBinding
import com.example.graduationproject.enums.OrderStatus
import com.example.graduationproject.enums.PaymentMethods
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.json.JSONArray
import org.json.JSONObject

class AfterSelectedItemFragment : Fragment() {

    private lateinit var binding: FragmentAfterSelectedItemBinding
    private var dataFood: DataFood? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAfterSelectedItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("InflateParams")
    private fun showDialogForVisaPayment(jsonObject: JSONObject, jsonArray: JSONArray) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.paymentdialog, null)
        val alertDialogBuilder = AlertDialog.Builder(requireContext()).apply {
            setView(dialogView)
            setCancelable(false)
        }
        val alertDialog = alertDialogBuilder.create()
        dialogView.apply {
            val payBtn = findViewById<Button>(R.id.paymentButton)
            val cancelBtn = findViewById<Button>(R.id.cancelButton)
            alertDialog.apply {
                payBtn?.setOnClickListener {
                    dismiss()
                    jsonObject.put(Constants.PAYMENT_METHOD, PaymentMethods.VISA.name)
                    jsonArray.put(jsonObject)
                    saveAllOrder(jsonArray)
                    replaceFragment(HomeFragmentHungry())
                }
                cancelBtn?.setOnClickListener { dismiss() }
            }.show()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataFood = arguments?.getParcelable("food")
        fillViews()
        setUpEditFields()
        binding.orderItem.setOnClickListener {
            getUserLocation(view)
        }
    }

    private fun saveAllOrder(jsonArray: JSONArray) {
        val sharedPreferences =
            activity?.getSharedPreferences(Constants.ORDER, Context.MODE_PRIVATE)
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

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction: FragmentTransaction? = fragmentManager?.beginTransaction()
        fragmentTransaction?.add(R.id.frame_layout_hungry, fragment)
        fragmentTransaction?.commit()
    }

    private fun setButtonEnability() {
        binding.apply {
            orderItem.isEnabled =
                radioGroup.checkedRadioButtonId != -1 && editQuantity.text.isNotEmpty() == true
        }
    }

    private fun setUpEditFields() {
        binding.apply {
            radioGroup.setOnCheckedChangeListener { _, _ ->
                setButtonEnability()
            }
            editQuantity.doOnTextChanged { _, _, _, _ ->
                setButtonEnability()
            }
        }
    }

    private fun generateOrderId(jsonArray: JSONArray): Int {
        return if (jsonArray.length() == 0) {
            1
        } else {
            val jsonObject = jsonArray.getJSONObject(jsonArray.length() - 1)
            jsonObject.getInt(Constants.ORDER_ID) + 1
        }
    }

    private fun getUserLocation(view: View) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            fetchLocation(view)
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun fetchLocation(view: View) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val jsonArray = Storage.allOrder(requireContext()) ?: JSONArray()
                    val jsonObjectOrder = JSONObject()
                    jsonObjectOrder.put(Constants.ORDER_ID, generateOrderId(jsonArray))
                    jsonObjectOrder.put(Constants.FOOD_ID, dataFood?.foodId)
                    jsonObjectOrder.put(Constants.CURRENT_CHEF, dataFood?.chefEmail)
                    jsonObjectOrder.put(Constants.User, CacheManager.getCurrentUser())
                    jsonObjectOrder.put(Constants.QUANTITY, binding.editQuantity.text.toString())
                    jsonObjectOrder.put(Constants.ORDER_STATUS, OrderStatus.PENDING.name)
                    jsonObjectOrder.put(Constants.LATITUDE, location.latitude.toString())
                    jsonObjectOrder.put(Constants.LONGITUDE, location.longitude.toString())

                    val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
                    when (radioGroup.checkedRadioButtonId) {
                        R.id.pay_cash -> {
                            jsonObjectOrder.put(Constants.PAYMENT_METHOD, PaymentMethods.CASH.name)
                            jsonArray.put(jsonObjectOrder)
                            saveAllOrder(jsonArray)
                            replaceFragment(HomeFragmentHungry())
                        }

                        R.id.pay_visa -> {
                            showDialogForVisaPayment(jsonObjectOrder, jsonArray)
                        }
                    }
                }
            }
            .addOnFailureListener {}
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}