package com.application.transdoc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.application.transdoc.databinding.ActivityMainBinding

class NavigationActivity: AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,
            R.layout.activity_main)

        drawerLayout = binding.drawerLayout

        navController = this.findNavController(R.id.nav_host_fragment)

        /*NavigationUI.setupWithNavController(binding.navView, navController)

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)*/
    }


    override fun onSupportNavigateUp(): Boolean {
        navController = findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }
}