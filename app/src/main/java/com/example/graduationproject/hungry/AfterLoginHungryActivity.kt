package com.example.graduationproject.hungry

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.graduationproject.*
import com.example.graduationproject.databinding.ActivityAfterLoginHungryBinding
import org.json.JSONArray

class AfterLoginHungryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAfterLoginHungryBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingInflatedId", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAfterLoginHungryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleNavigationView()
        binding.apply {
            toolbarHungry.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.add_to_the_cart -> {
                        replaceFragment(CartFragment())
                        true
                    }

                    R.id.search -> {
                        replaceFragment(SearchFragment())
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
                findViewById<ImageView>(R.id.cancel).setOnClickListener {
                    binding.drawerLayoutHungry.closeDrawer(GravityCompat.START)
                }
            }

            val toggle = ActionBarDrawerToggle(
                this@AfterLoginHungryActivity,
                binding.drawerLayoutHungry,
                null,
                R.string.open_nav,
                R.string.close_nav
            )
            binding.drawerLayoutHungry.addDrawerListener(toggle)
            toggle.syncState()
        }

        replaceFragment(HomeFragmentHungry())
        binding.bottomNavigationViewHungry.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> replaceFragment(HomeFragmentHungry())
                R.id.offers_hungry -> replaceFragment(OffersHungryFragment())
            }
            true
        }
    }

    private fun sendEmail(subject: String = "HungryWarmth", body: String = "", recipientEmail: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipientEmail))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
         startActivity(intent)
    }

    private fun handleNavigationView() {
        binding.navView.apply {
            setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.send_a_complaint_via_email -> {
                        sendEmail(recipientEmail = "support@warth.com")
                    }

                    R.id.nav_settings_hungry -> {
                        binding.drawerLayoutHungry.closeDrawer(GravityCompat.START)
                        replaceFragment(SettingAccountFragment())
                    }

                    R.id.home -> {}

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

                    }

                    R.id.nav_logout_hungry -> {
                        checkAndSaveRememberMe()
                        finish()
                    }

                    else -> {}
                }
                true
            }
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

    private fun checkAndSaveRememberMe() {
        val jsonArray = Storage.getAlleRememberMe(this) ?: JSONArray()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val email = jsonObject.get(Constants.Email)
            if (email == CacheManager.getCurrentUser())
                jsonObject.put(Constants.REMEMBER_ME, "false")
        }
        Storage.saveRememberMe(this, jsonArray)
    }
}
