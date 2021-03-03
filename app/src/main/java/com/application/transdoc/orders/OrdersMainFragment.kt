package com.application.transdoc.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.application.transdoc.R
import com.application.transdoc.databinding.FragmentMainOrdersBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener


class OrdersMainFragment : Fragment() {

    private lateinit var binding: FragmentMainOrdersBinding
    private lateinit var navController: NavController
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: androidx.viewpager.widget.PagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_orders, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        viewPager = binding.viewPager
        tabLayout = binding.tabBar

        pagerAdapter = activity?.supportFragmentManager?.let {
            PagerAdapter(tabLayout.tabCount,
                it
            )
        }!!
        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

}