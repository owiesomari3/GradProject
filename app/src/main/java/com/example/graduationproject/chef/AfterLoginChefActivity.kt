package com.example.graduationproject.chef


import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import com.example.graduationproject.ButtonSheetFragment
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityAfterLoginChefBinding
import com.example.graduationproject.databinding.ButtonsheetlayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView

class AfterLoginChefActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    @SuppressLint("MissingInflatedId", "ResourceType")
    private lateinit var drawerLayout: DrawerLayout
    lateinit var binding: ActivityAfterLoginChefBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAfterLoginChefBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        binding.fab.setOnClickListener {
            ButtonSheetFragment()
        }
        setSupportActionBar(binding.toolbarChef)
    }

    class ButtonSheetFragment : BottomSheetDialogFragment() {
        lateinit var binding: ButtonsheetlayoutBinding

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.buttonsheetlayout, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            val reqestCode = 1
            super.onViewCreated(view, savedInstanceState)
            binding.camera.setOnClickListener {
                val intent = Intent()
                intent.action = MediaStore.ACTION_IMAGE_CAPTURE

                startActivityForResult(intent, reqestCode)

            }
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            val capturedImage: Bitmap?
            if (resultCode == requestCode && requestCode == RESULT_OK)
                capturedImage = data?.extras?.get("data") as Bitmap
            //image_food.setImageBitmap(capturedImage)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.num_account_chef -> {}
            R.id.favorite_in_your_menue -> {}
            R.id.evaluation_chef -> {}
            R.id.your_menu_chef -> {}
            R.id.location_chef -> {}
            R.id.nav_settings_chef -> {}
            R.id.nav_share_chef -> {
                lateinit var googlePlay: Intent
                googlePlay.action = Intent.ACTION_SEND
                googlePlay.putExtra(
                    Intent.EXTRA_TEXT,
                    "Download the application link below::https://play.google.com/store/apps/details?id=com.example.graduationproject"
                )
                googlePlay.type = "text/plain"
                startActivity(Intent.createChooser(googlePlay, "Choose the app you want to share with"))
            }

            R.id.nav_logout_chef -> {}
            R.id.send_us_an_email -> {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("mailto:" + "Warmth@team.com" + "?subject=" + "ChefWarmth")
                    )
                )
            }

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
