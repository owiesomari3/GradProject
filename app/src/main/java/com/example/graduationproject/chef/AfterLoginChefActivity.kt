package com.example.graduationproject.chef

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.graduationproject.*
import com.example.graduationproject.databinding.ActivityAfterLoginChefBinding
import com.example.graduationproject.hungry.CartFragment
import com.example.graduationproject.hungry.SearchFragment
import org.json.JSONArray

class AfterLoginChefActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAfterLoginChefBinding
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingInflatedId", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAfterLoginChefBinding.inflate(layoutInflater)
        setContentView(binding.root)
        handleNavigationView()
        binding.apply {
            toolbarChef.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.order_history -> {
                        replaceFragment(OrderHistoryFragment())
                        true
                    }
                    else -> false
                }
            }




            menuClick.setOnClickListener {
                if (drawerLayoutChef.isDrawerOpen(GravityCompat.START))
                    drawerLayoutChef.closeDrawer(GravityCompat.START)
                else drawerLayoutChef.openDrawer(GravityCompat.START)
            }

            navView.getHeaderView(0).apply {
                findViewById<TextView>(R.id.user_email).text = CacheManager.getCurrentUser()
                findViewById<TextView>(R.id.username).text = getUserName()
                findViewById<ImageView>(R.id.cancel).setOnClickListener {
                    binding.drawerLayoutChef.closeDrawer(GravityCompat.START)
                }
            }

            val toggle = ActionBarDrawerToggle(
                this@AfterLoginChefActivity,
                binding.drawerLayoutChef,
                null,
                R.string.open_nav,
                R.string.close_nav
            )

            binding.drawerLayoutChef.addDrawerListener(toggle)
            toggle.syncState()



            fab.setOnClickListener {
                // Perform action when FAB is clicked
                showFabActions()
            }
        }

        replaceFragment(HomeFragmentChef())
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home_chef -> {
                    replaceFragment(HomeFragmentChef())
                }
                R.id.order_chefs -> {
                    replaceFragment(OrdersChefsFragment())
                }
                R.id.wallet_chef -> {
                    replaceFragment(WalletChefFragment())
                }
                R.id.offers -> {
                    replaceFragment(OffersChefsFragment())
                }
            }
            true
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun handleNavigationView() {
        binding.navView.apply {
            setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.nav_settings_chef -> {
                        binding.drawerLayoutChef.closeDrawer(GravityCompat.START)
                        replaceFragment(SettingAccChefFragment())
                        true
                    }



                    R.id.nav_share_chef -> {
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

                    R.id.send_us_an_email -> {
                        sendEmail(recipientEmail = "Warmth@team.com")
                        true
                    }

                    R.id.nav_logout_chef -> {
                        checkAndSaveRememberMe()
                        finish()
                        false
                    }

                    else -> false
                }
            }
        }
    }

    private fun sendEmail(
        subject: String = "ChefWarmth",
        body: String = "",
        recipientEmail: String
    ) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipientEmail))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        startActivity(intent)
    }

    @SuppressLint("ResourceType")
    private fun showFabActions() {
        val action1 = "Camera"
        val action2 = "Gallery"

        val items = arrayOf(action1, action2)

        AlertDialog.Builder(this)
            .setTitle("Choose from:")
            .setCancelable(true)
            .setItems(items) { _, which ->
                when (which) {
                    0 -> {
                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(arrayOf(Manifest.permission.CAMERA), 12)
                        } else {
                            camera()
                        }
                    }

                    1 -> {
                        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(
                                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                13
                            )
                        } else {
                            gallery()
                        }
                    }
                }
            }
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            12 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    camera()
                    replaceFragment(AddFoodFragment())
                } else {
                    Util.showToastMsg(this,"Camera permission denied")
                }
            }
            13 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    gallery()
                    replaceFragment(AddFoodFragment())
                } else {
                    Util.showToastMsg(this,"Gallery permission denied")
                }
            }
        }
    }

    private fun camera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 1)
    }

    private fun gallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 2)
    }

    @Deprecated("Deprecated in Java")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                1 -> {
                    val image = data?.extras?.get("data") as Bitmap
                    replaceFragment(AddFoodFragment(), image)
                }
                2 -> {
                    val imageUri = data?.data as Uri
                    replaceFragment(AddFoodFragment(), null, imageUri)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun replaceFragment(fragment: Fragment, image: Bitmap? = null, imageUri: Uri? = null) {
        image?.let {
            val data1 = Bundle()
            data1.putParcelable("image", image)
            fragment.arguments = data1
        }

        imageUri?.let{
            val data2 = Bundle()
            data2.putParcelable("imageUri", imageUri)
            fragment.arguments = data2
        }

        val fragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout_chef, fragment)
        fragmentTransaction.commit()
    }

    private  fun getUserName():String {
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