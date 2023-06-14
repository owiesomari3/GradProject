package com.example.graduationproject

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.graduationproject.chef.HomeFragmentChef
import com.example.graduationproject.databinding.FragmentSettingAccountBinding
import com.example.graduationproject.hungry.HomeFragmentHungry
import org.json.JSONArray

class SettingAccountFragment : Fragment() {

    lateinit var binding: FragmentSettingAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editTextEmail.setText(CacheManager.getCurrentUser())
        binding.buttonSave.setOnClickListener {
            val allUsers = Storage.getAllUsers(requireContext())
            allUsers?.let {
                if (binding.editTextnewPassword.text.toString() ==
                    binding.editTextConfirmPassword.text.toString()
                ) {
                    for (i in 0 until allUsers.length()) {
                        val jsonObject = allUsers.getJSONObject(i)
                        if (jsonObject.getString(Constants.Email) == CacheManager.getCurrentUser()) {
                            val pass = jsonObject.getString(Constants.PASSWORD)
                            if (pass == binding.editTextOldPassword.text.toString()) {
                                jsonObject.put(
                                    Constants.PASSWORD,
                                    binding.editTextnewPassword.text.toString()
                                )

                                jsonObject.put(
                                    Constants.Email,
                                    binding.editTextEmail.text.toString()
                                )
                                saveAllUsersList(allUsers)
                            }
                        }
                    }
                    replaceFragment(HomeFragmentHungry())
                } else binding.incorrectPassword.visibility = View.VISIBLE
            }
        }
    }

    private fun saveAllUsersList(jsonArray: JSONArray) {
        val sharedPreferences =
            context?.getSharedPreferences(Constants.INFO_USERS_SHARED_PREFS, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString(Constants.USER_LIST, jsonArray.toString())
        editor?.apply()
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction: FragmentTransaction? = fragmentManager?.beginTransaction()
        fragmentTransaction?.add(R.id.frame_layout_hungry, fragment)
        fragmentTransaction?.commit()
    }
}