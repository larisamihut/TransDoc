package com.application.transdoc.elements.cars

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.application.transdoc.R
import com.application.transdoc.databinding.FragmentCarsBinding
import com.application.transdoc.firestore.Database
import com.google.firebase.auth.FirebaseAuth

class CarsFragment : Fragment() {
    private lateinit var binding: FragmentCarsBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cars, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = getCurrentUser().uid?.let {
            Database().getCollectionRefference("admins").document(
                it)
        }
        val viewModelFactory =
            CarsViewModelFactory(dataSource, this.requireActivity(), application)
        val carsViewModel =
            ViewModelProvider(this, viewModelFactory).get(CarsViewModel::class.java)
        binding.carsViewModel = carsViewModel

        val adapter = CarsAdapter(dataSource)
        binding.recyclerView.adapter = adapter
        carsViewModel.setDataToAdapter(adapter)

        binding.setLifecycleOwner(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
    }

    private fun getCurrentUser() : FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

}