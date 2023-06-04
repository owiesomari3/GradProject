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
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityAfterLoginChefBinding


class AfterLoginChefActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAfterLoginChefBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAfterLoginChefBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarChef)

        binding.apply {
            menuClick.setOnClickListener {
                if (drawerLayoutChef.isDrawerOpen(GravityCompat.START))
                    drawerLayoutChef.closeDrawer(GravityCompat.START)
                else drawerLayoutChef.openDrawer(GravityCompat.START)
            }
            navView.setNavigationItemSelectedListener { menuItem ->
                drawerLayoutChef.closeDrawer(GravityCompat.START)
                when (menuItem.itemId) {
                    R.id.num_account_chef -> {
                        false
                    }
                    R.id.favorite_in_your_menue -> {
                        false
                    }
                    R.id.evaluation_chef -> {
                        false
                    }
                    R.id.your_menu_chef -> {
                        false
                    }
                    R.id.location_chef -> {
                        false
                    }

                    R.id.nav_settings_chef -> {
                        false
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

                    R.id.nav_logout_chef -> {
                        false
                    }

                    R.id.send_us_an_email -> {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("mail:" + "Warmth@team.com" + "?subject=" + "HungryWarmth")
                            )
                        )
                        true
                    }

                    R.id.cancel -> {
                        binding.drawerLayoutChef.closeDrawer(GravityCompat.START)
                        true
                    }

                    else -> false
                }
            }

        }

        binding.apply {
            fab.setOnClickListener {
                // Perform action when FAB is clicked
                showFabActions()
            }
        }

        replaceFragment(HomeFragmentChef())
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    replaceFragment(HomeFragmentChef())
                }
                R.id.order_chefs -> {
                    replaceFragment(OrdersChefsFragment())
                }
                R.id.wallet_chef -> {
                    replaceFragment(WalletChefFragment())
                }
                R.id.search_chef -> {
                    replaceFragment(SearchFragment())
                }
            }
            true
        }

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
                    Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show()
                }
            }
            13 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    gallery()
                    replaceFragment(AddFoodFragment())
                } else {
                    Toast.makeText(this, "Gallery permission denied", Toast.LENGTH_LONG).show()
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
}