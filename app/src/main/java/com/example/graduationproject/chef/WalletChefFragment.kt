package com.example.graduationproject.chef

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.example.graduationproject.CacheManager
import com.example.graduationproject.Constants
import com.example.graduationproject.Storage
import com.example.graduationproject.Util
import com.example.graduationproject.databinding.FragmentWalletChefBinding
import org.json.JSONArray
import org.json.JSONObject

class WalletChefFragment : Fragment() {

    private lateinit var binding: FragmentWalletChefBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWalletChefBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val allWallet = Storage.getAllWallet(requireContext()) ?: JSONArray()
        var balance = ""
        var objectPosition = 0
        var jsonObjectWallet: JSONObject
        for (position in 0 until allWallet.length()) {
            jsonObjectWallet = allWallet.getJSONObject(position)
            val chefEmail = jsonObjectWallet?.getString(Constants.CURRENT_CHEF)
            allWallet.let {
                if (chefEmail == CacheManager.getCurrentUser()) {
                    objectPosition = position
                    balance = jsonObjectWallet.getString(Constants.BALANCE)
                }
            }
        }

        binding.apply {
            WalletWithdrawMoney.doOnTextChanged { _, _, _, _ ->
                btnOk.isEnabled = WalletWithdrawMoney.text.toString().isNotEmpty()
            }
            WalletEmailChef.text = CacheManager.getCurrentUser()
            if (balance == "") {
                WalletBalance.text = Util.currencyFormat("0")
                WalletWithdrawMoney.visibility = View.GONE
                btnOk.visibility = View.GONE
                withdrowText.visibility = View.GONE
            } else WalletBalance.text = Util.currencyFormat(balance)

            btnOk.setOnClickListener {
                if (WalletWithdrawMoney.text.toString()
                        .toDouble() > balance.toDouble() && WalletWithdrawMoney.text.toString()
                        .toDouble() > 0
                ) {
                    Util.showToastMsg(requireContext(), "No sufficient balance")
                } else {
                    allWallet.remove(objectPosition)
                    val newBalance =
                        balance.toDouble() - WalletWithdrawMoney.text.toString().toDouble()
                    balance = newBalance.toString()
                    val newObject = JSONObject()
                    newObject.put(Constants.CURRENT_CHEF, CacheManager.getCurrentUser())
                    newObject.put(Constants.BALANCE, newBalance.toString())
                    allWallet.put(newObject)
                    saveAllWallets(allWallet)
                    WalletBalance.text = newBalance.toString()
                    Util.showToastMsg(requireContext(),"Your transaction has been submitted" )
                    WalletWithdrawMoney.setText("")
                }
            }
        }
    }

    private fun saveAllWallets(jsonArray: JSONArray) {
        val jsonString = jsonArray.toString()
        val sharedPreferences =
            activity?.getSharedPreferences(Constants.WALLET, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString(Constants.WALLET_List, jsonString)
        editor?.apply()
    }
}