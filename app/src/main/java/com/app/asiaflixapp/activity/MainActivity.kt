package com.app.asiaflixapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.viewpager.widget.ViewPager
import com.app.asiaflixapp.R
import com.app.asiaflixapp.adapter.ViewPagerAdapter
import com.app.asiaflixapp.databinding.ActivityMainBinding
import com.app.asiaflixapp.fragment.HomeFragment
import com.app.asiaflixapp.fragment.KshowFragment
import com.app.asiaflixapp.fragment.LibraryFragment
import com.app.asiaflixapp.fragment.PopularFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding :ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolbar()
        val  viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.AddFragment(HomeFragment())
        viewPagerAdapter.AddFragment(KshowFragment())
        viewPagerAdapter.AddFragment(PopularFragment())
        viewPagerAdapter.AddFragment(LibraryFragment())
        binding.viewPager.adapter = viewPagerAdapter

        binding.botNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> binding.viewPager.currentItem = 0
                R.id.kshow -> binding.viewPager.currentItem = 1
                R.id.popular -> binding.viewPager.currentItem = 2
                R.id.library -> binding.viewPager.currentItem = 3
            }
            false
        }

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
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
                 if (p1) binding.linear.visibility =View.GONE
                else binding.linear.visibility =View.VISIBLE
            }

        })
        return true
    }
}