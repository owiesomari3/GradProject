package com.example.graduationproject.hungry

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityAfterLoginHungryBinding
import com.google.android.material.navigation.NavigationView
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

class AfterLoginHungryActivity: AppCompatActivity() ,NavigationView.OnNavigationItemSelectedListener{
    private lateinit var binding:ActivityAfterLoginHungryBinding
    private lateinit var drawerLayout: DrawerLayout
    @SuppressLint("MissingInflatedId", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAfterLoginHungryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
           R.id.offers_hungry   ->{}
           R.id.Notification_hungry   ->{}
           R.id. home  ->{}
           R.id.send_a_complaint_via_email  ->{
               startActivity(Intent(Intent.ACTION_VIEW,
                   Uri.parse("mail:"+"Warmth@team.com"+"?subject="+"HungryWarmth")))
           }
            R.id.nav_settings_hungry->{}
            R.id.nav_share_hungry->{
                lateinit var googlePlay:Intent
                googlePlay.action=Intent.ACTION_SEND
                googlePlay.putExtra(Intent.EXTRA_TEXT,
                    "Download the application link below::https://play.google.com/store/apps/details?id=com.example.graduationproject")
                googlePlay.type="text/plain"
                startActivity(Intent.createChooser(googlePlay,"Choose the app you want to share with"))
            }
            R.id.nav_logout_hungry->{}
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}