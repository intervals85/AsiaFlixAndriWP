package com.app.asiaflixapp.activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.GravityCompat
import androidx.viewpager.widget.ViewPager
import com.app.asiaflixapp.R
import com.app.asiaflixapp.adapter.ViewPagerAdapter
import com.app.asiaflixapp.databinding.ActivityMainBinding
import com.app.asiaflixapp.fragment.HomeFragment
import com.app.asiaflixapp.fragment.LibraryFragment
import com.app.asiaflixapp.fragment.PopularFragment
import com.app.asiaflixapp.fragment.StarFragment
import com.app.asiaflixapp.helper.Utils
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        initBotNav()
        initNavDraw()
        subscribeToPushService()
        checkNotificationPermission()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkNotificationPermission() {
        val permission = Manifest.permission.POST_NOTIFICATIONS
        Dexter.withContext(this)
            .withPermission(permission)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {

                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    Utils.toast(this@MainActivity, "you had denied permission this app wont work feature properly")
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).check()
    }

    private fun subscribeToPushService() {
        try {
            FirebaseApp.initializeApp(this)
            FirebaseMessaging.getInstance().subscribeToTopic(packageName)
            Log.d("AndroidBash", "Subscribed")
            //  Toast.makeText(MainActivity.this, "Subscribed", Toast.LENGTH_SHORT).show();
        } catch (i: IllegalArgumentException) {
            i.message
        }
    }
    private fun initNavDraw() {
        val drawerToggle = object : ActionBarDrawerToggle(
            this,
            binding. drawerLayout,
            binding.  toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        ) {
            override fun onDrawerClosed(view: View) {
                super.onDrawerClosed(view)
             //   binding.toolbar.setNavigationIcon(R.drawable.ic_burger)
               // getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.ic_burger)
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
             //   binding.toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
                //getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)

            }
        }

        drawerToggle.isDrawerIndicatorEnabled = true
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
       // binding.toolbar.setNavigationIcon(R.drawable.ic_burger)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_burger)
        binding .navigationView.setNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.nav_privacy_policy->{
                    startActivity(Intent(this, PrivacyActivity::class.java))
                }
                R.id.nav_rate_App->{
                    val intent = Intent(  Intent.ACTION_VIEW)
                    intent.data= Uri.parse("https://play.google.com/store/apps/developer?id=$packageName")
                    startActivity(intent)
                }
                R.id.nav_share_app->{
                    val text = "I just watch some movie, you can download here  https://play.google.com/store/apps/developer?id=$packageName "
                    val b = Intent(
                        Intent.ACTION_SEND
                    )
                    b.type = "text/plain"
                    b.putExtra(
                        Intent.EXTRA_TEXT, text
                    )
                    startActivity(
                        Intent.createChooser(
                            b,
                            "Share Via"
                        )
                    )
                }
                R.id.nav_more_app->{
                    val intent = Intent(  Intent.ACTION_VIEW)
                    intent.data= Uri.parse(resources.getString(R.string.more_app_link))
                    startActivity(intent)
                }
            }
            binding. drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

    }

    private fun initBotNav() {
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.AddFragment(HomeFragment())
        viewPagerAdapter.AddFragment(PopularFragment())
        viewPagerAdapter.AddFragment(StarFragment())
        viewPagerAdapter.AddFragment(LibraryFragment())
        binding.viewPager.adapter = viewPagerAdapter

        binding.botNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> binding.viewPager.currentItem = 0
                R.id.popular -> binding.viewPager.currentItem = 1
                R.id.star -> binding.viewPager.currentItem = 2
                R.id.library -> binding.viewPager.currentItem = 3
            }
            false
        }

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                binding.botNav.menu.getItem(position).isChecked = true

            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
    }

    private fun initToolbar() {
        binding.toolbar.title = ""
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
               binding. drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        val searchView = menu?.findItem(R.id.acion_search)?.actionView as SearchView
        searchView.setOnQueryTextFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(p0: View?, p1: Boolean) {
                if (p1) binding.linear.visibility = View.GONE
                else binding.linear.visibility = View.VISIBLE
            }

        })
        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                intent.putExtra("link", "search?type=movies&keyword=$query")
                startActivity(intent)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return true
            }


        })
        return true
    }


    var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            val homeIntent = Intent(Intent.ACTION_MAIN)
            homeIntent.addCategory(Intent.CATEGORY_HOME)
            homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(homeIntent)
            return
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Tap Once Again To Close..", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
}