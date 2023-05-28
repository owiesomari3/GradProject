package com.example.graduationproject.hungry

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.graduationproject.R
import com.example.graduationproject.chef.OrdersChefFragment
import com.example.graduationproject.chef.WalletChefFragment
import com.example.graduationproject.databinding.ActivityAfterLoginHungryBinding

class AfterLoginHungryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAfterLoginHungryBinding

    @SuppressLint("MissingInflatedId", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAfterLoginHungryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            menuClick.setOnClickListener {
                if (drawerLayoutHungry.isDrawerOpen(GravityCompat.START))
                    drawerLayoutHungry.closeDrawer(GravityCompat.START)
                else drawerLayoutHungry.openDrawer(GravityCompat.START)
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

    @SuppressLint("SetTextI18n")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        val menuItem = menu?.findItem(R.id.notification_main_bar)
        val actionView = menuItem?.actionView
        if (actionView is TextView) {
            actionView.text = "Action View"
           // actionView.setText("")///DB
        }

        menu?.findItem(R.id.notification_main_bar)?.setOnMenuItemClickListener {
            // Handle menu item 1 click
            true
        }

        menu?.findItem(R.id.add_to_the_cart)?.setOnMenuItemClickListener {
            // Handle menu item 2 click
            true
        }

        return true
    }


}