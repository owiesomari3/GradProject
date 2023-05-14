package com.example.graduationproject.hungry

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityAfterLoginHungryBinding

class AfterLoginHungryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAfterLoginHungryBinding

    @SuppressLint("MissingInflatedId", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAfterLoginHungryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.menuClick.setOnClickListener {
            if (binding.drawerLayoutHungry.isDrawerOpen(GravityCompat.START)) binding.drawerLayoutHungry.closeDrawer(GravityCompat.START)
            else binding.drawerLayoutHungry.openDrawer(GravityCompat.START)
        }

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            binding.drawerLayoutHungry.closeDrawer(GravityCompat.START)
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
                        startActivity(Intent.createChooser(googlePlay, "Choose the app you want to share with"))

                    }
                    true
                }

                R.id.nav_logout_hungry -> {
                    false
                }

                else -> false
            }
        }
    }
}