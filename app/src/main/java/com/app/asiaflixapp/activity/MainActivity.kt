package com.app.asiaflixapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.viewpager.widget.ViewPager
import com.app.asiaflixapp.R
import com.app.asiaflixapp.adapter.ViewPagerAdapter
import com.app.asiaflixapp.databinding.ActivityMainBinding
import com.app.asiaflixapp.fragment.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
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
}