package com.example.graduationproject.hungry

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.graduationproject.CacheManager
import com.example.graduationproject.Constants
import com.example.graduationproject.R
import com.example.graduationproject.Storage
import com.example.graduationproject.chef.OrdersChefFragment
import com.example.graduationproject.chef.WalletChefFragment
import com.example.graduationproject.databinding.ActivityAfterLoginHungryBinding
import org.json.JSONArray

class AfterLoginHungryActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingInflatedId", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityAfterLoginHungryBinding = ActivityAfterLoginHungryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            toolbarHungry.setOnMenuItemClickListener {menuItem ->
                when(menuItem.itemId){
                    R.id.add_to_the_cart -> {
                        replaceFragment(CartFragment())
                        true
                    }
                    else -> false
                }
            }

            menuClick.setOnClickListener {
                if (drawerLayoutHungry.isDrawerOpen(GravityCompat.START))
                    drawerLayoutHungry.closeDrawer(GravityCompat.START)
                else drawerLayoutHungry.openDrawer(GravityCompat.START)
            }

            navView.getHeaderView(0).apply {
                findViewById<TextView>(R.id.user_email).text = CacheManager.getCurrentUser()
                findViewById<TextView>(R.id.username).text = getUserName()
            }

            navView.setNavigationItemSelectedListener { menuItem ->
                drawerLayoutHungry.closeDrawer(GravityCompat.START)
                when (menuItem.itemId) {
                    R.id.offers_hungry -> {
                        false
                    }

                    R.id.Notification_hungry -> {
                        false
                    }

                    R.id.home -> {
                        false
                    }

                    R.id.send_a_complaint_via_email -> {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("mail:" + "Warmth@team.com" + "?subject=" + "HungryWarmth")
                            )
                        )
                        true
                    }

                    R.id.nav_settings_hungry -> {
                        false
                    }

                    R.id.nav_share_hungry -> {
                        val googlePlay = Intent()
                        googlePlay.apply {
                            action = Intent.ACTION_SEND
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "Download the application link below::https://play.google.com/store/apps/details?id=com.example.graduationproject"
                            )
                            type = "text/plain"
                            startActivity(
                                Intent.createChooser(
                                    googlePlay,
                                    "Choose the app you want to share with"
                                )
                            )

                        }
                        true
                    }

                    R.id.nav_logout_hungry -> {
                        false
                    }
                    R.id.cancel -> {
                        drawerLayoutHungry.closeDrawer(GravityCompat.START)
                        true
                    }

                    else -> false
                }

            }
        }

        replaceFragment(HomeFragmentHungry())
        binding.bottomNavigationViewHungry.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home-> replaceFragment(HomeFragmentHungry())
                R.id.order_hungry -> replaceFragment(OrdersChefFragment())
                R.id.search_hungry -> replaceFragment(WalletChefFragment())
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.frame_layout_hungry, fragment)
        fragmentTransaction.commit()
    }

    private fun getUserName(): String {
        var userName = ""
        val allUsers = Storage.getAllUsers(this) ?: JSONArray()
        for (i in 0 until allUsers.length()) {
            val json = allUsers.getJSONObject(i)
            if (json.getString(Constants.Email) == CacheManager.getCurrentUser()) {
                userName = json.getString(Constants.FULL_NAME)
            }
        }
        return userName
    }
}
