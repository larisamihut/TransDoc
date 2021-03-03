package com.application.transdoc.orders

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.application.transdoc.orders.activeOrders.OrdersFragment

class PagerAdapter(numOfTabs: Int, fm: FragmentManager): FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var numOfTabs :Int
    private var tabTitle = mutableListOf<String>("Active Orders","Completed Orders")

    init {
        this.numOfTabs = numOfTabs
    }

    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> return OrdersFragment()
            else -> return CashedOrdersFragment()
        }
    }

    override fun getCount(): Int {
       return numOfTabs
    }

    override fun getPageTitle(position: Int): CharSequence {
        return tabTitle[position]
    }
}