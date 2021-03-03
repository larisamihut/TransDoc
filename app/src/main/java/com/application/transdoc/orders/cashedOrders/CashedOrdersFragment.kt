package com.application.transdoc.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.application.transdoc.R
import com.application.transdoc.databinding.FragmentCashedOrdersBinding
import com.application.transdoc.elements.drivers.CashedOrdersAdapter
import com.application.transdoc.orders.activeOrders.OrdersRepository
import com.application.transdoc.orders.cashedOrders.CashedOrdersViewModel

class CashedOrdersFragment : Fragment() {

    private lateinit var binding: FragmentCashedOrdersBinding
    private lateinit var navController: NavController
    private lateinit var viewModel: CashedOrdersViewModel

    companion object {
        private val TAG = CashedOrdersFragment::class.qualifiedName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cashed_orders, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        val newOrderButton = view.findViewById<Button>(R.id.add_button)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CashedOrdersViewModel::class.java)
        viewModel.repository =
            OrdersRepository()

        val adapter = CashedOrdersAdapter()
        binding.recyclerView.adapter = adapter
        if (adapter != null) {
            viewModel.setDataToAdapter(adapter, "cashed")
        }
    }

}