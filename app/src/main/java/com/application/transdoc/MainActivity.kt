package com.application.transdoc

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.application.transdoc.authentication.LoginFragment
import com.application.transdoc.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController
    var navigationPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,
            com.application.transdoc.R.layout.activity_main)
        navController = this.findNavController(com.application.transdoc.R.id.nav_host_fragment)
        binding.lifecycleOwner = this
        initView()

    }

    private fun initView(){
        drawerLayout = binding.drawerLayout
        val toolbar = findViewById<Toolbar>(com.application.transdoc.R.id.toolbar_main)
        setSupportActionBar(toolbar)
        setUpDrawerLayout()

        navigationPosition = com.application.transdoc.R.id.ordersMainFragment
        navController.navigate(com.application.transdoc.R.id.ordersMainFragment)
        binding.navView.setCheckedItem(navigationPosition)
        toolbar.title = getString(com.application.transdoc.R.string.orders)

        binding.navView.setNavigationItemSelectedListener { item: MenuItem ->
            when (item.itemId){
                com.application.transdoc.R.id.ordersMainFragment ->{
                    toolbar.title = getString(com.application.transdoc.R.string.orders)
                    navigationPosition = com.application.transdoc.R.id.ordersMainFragment
                    navController.navigate(com.application.transdoc.R.id.ordersMainFragment)
                }
                com.application.transdoc.R.id.carsFragment ->{
                    toolbar.title = getString(com.application.transdoc.R.string.cars)
                    navigationPosition = com.application.transdoc.R.id.carsFragment
                    navController.navigate(com.application.transdoc.R.id.carsFragment)
                }
                com.application.transdoc.R.id.driversFragment ->{
                    toolbar.title = getString(com.application.transdoc.R.string.drivers)
                    navigationPosition = com.application.transdoc.R.id.driversFragment
                    navController.navigate(com.application.transdoc.R.id.driversFragment)
                }
                com.application.transdoc.R.id.companiesFragment ->{
                    toolbar.title = getString(com.application.transdoc.R.string.companies)
                    navigationPosition = com.application.transdoc.R.id.driversFragment
                    navController.navigate(com.application.transdoc.R.id.companiesFragment)
                }
                com.application.transdoc.R.id.signOut ->{
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this, LoginFragment::class.java)
                    startActivity(intent)
                }
                else-> {
                    print("x is neither 1 nor 2")
                }
            }
            item.isChecked
            drawerLayout.closeDrawers()
            true
        }
    }

    private fun setUpDrawerLayout() {
        val toolbar = findViewById<Toolbar>(com.application.transdoc.R.id.toolbar_main)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, com.application.transdoc.R.string.navigation_drawer_open, com.application.transdoc.R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onBackPressed() {
        val toolbar = findViewById<Toolbar>(com.application.transdoc.R.id.toolbar_main)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        if (navigationPosition == com.application.transdoc.R.id.ordersMainFragment) {
            finish()
        } else {
            navigationPosition = com.application.transdoc.R.id.ordersMainFragment
            navController.navigate(com.application.transdoc.R.id.ordersMainFragment)
            binding.navView.setCheckedItem(navigationPosition)
            toolbar.title = getString(com.application.transdoc.R.string.orders)
        }
    }
}