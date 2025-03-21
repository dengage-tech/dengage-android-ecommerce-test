package com.dengage.dengageecommercetest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private val categoryFragment = CategoryListFragment()
    private val cartFragment = CartFragment()
    private val profileFragment = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Set default fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, categoryFragment)
            .commit()

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
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

}
