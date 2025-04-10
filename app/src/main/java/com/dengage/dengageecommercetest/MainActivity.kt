package com.dengage.dengageecommercetest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.dengage.dengageecommercetest.data.CartManager
import com.dengage.sdk.Dengage

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private val homeFragment = HomeFragment()
    private val categoryFragment = CategoryListFragment()
    private val cartFragment = CartFragment()
    private val profileFragment = ProfileFragment()


    fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commitAllowingStateLoss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Dengage.requestNotificationPermission(this)

        val prefs = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val isLoggedIn = prefs.getBoolean("isLoggedIn", false)
        if (!isLoggedIn) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            return
        }


        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Set default fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, homeFragment)
            .commit()

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            clearBackStack()
            when(item.itemId) {
                R.id.nav_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, homeFragment)
                        .commit()
                    true
                }
                R.id.nav_categories -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, categoryFragment)
                        .commit()
                    true
                }
                R.id.nav_cart -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, cartFragment)
                        .commit()
                    true
                }
                R.id.nav_profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, profileFragment)
                        .commit()
                    true
                }
                else -> false
            }
        }
        updateCartBadge(CartManager.getTotalItemCount())

    }

    fun clearBackStack() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }


    // In MainActivity or wherever you can access bottomNavigationView:
    fun updateCartBadge(itemCount: Int) {
        val badge = bottomNavigationView.getOrCreateBadge(R.id.nav_cart)
        if (itemCount > 0) {
            badge.isVisible = true
            badge.number = itemCount
        } else {
            badge.isVisible = false
        }
    }

    fun selectCategoryTab() {
        bottomNavigationView.selectedItemId = R.id.nav_categories
    }

}
