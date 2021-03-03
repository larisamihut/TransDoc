package com.application.transdoc.orders.activeOrders

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
import com.application.transdoc.databinding.FragmentOrdersBinding
import com.application.transdoc.elements.drivers.OrdersAdapter

class OrdersFragment : Fragment() {

    private lateinit var binding: FragmentOrdersBinding
    private lateinit var navController: NavController
    private lateinit var viewModel: OrdersViewModel

    companion object {
        private val TAG = OrdersFragment::class.qualifiedName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        val newOrderButton = view.findViewById<Button>(R.id.add_button)
        newOrderButton.setOnClickListener(){

            navController.navigate(R.id.action_ordersMainFragment_to_new_order_navigation)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(OrdersViewModel::class.java)
        viewModel.repository =
            OrdersRepository()
        binding.ordersViewModel = viewModel

        val adapter = view?.let { OrdersAdapter(it) }
        binding.recyclerView.adapter = adapter
        if (adapter != null) {
            viewModel.setDataToAdapter(adapter,"documents")
        }
    }

}