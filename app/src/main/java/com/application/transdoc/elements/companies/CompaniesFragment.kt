package com.application.transdoc.elements.companies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.application.transdoc.R
import com.application.transdoc.databinding.FragmentCompaniesBinding
import com.application.transdoc.databinding.FragmentDriversBinding
import com.application.transdoc.firestore.Database
import com.google.firebase.auth.FirebaseAuth

class CompaniesFragment : Fragment() {
    private lateinit var binding: FragmentCompaniesBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_companies, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = getCurrentUser().uid?.let {
            Database().getCollectionRefference("admins").document(
                it)
        }
        val viewModelFactory =
            CompaniesViewModelFactory(dataSource, this.requireActivity(), application)
        val companiesViewModel =
            ViewModelProvider(this, viewModelFactory).get(CompaniesViewModel::class.java)
        binding.companiesViewModel = companiesViewModel

        val adapter = CompaniesAdapter()
        binding.recyclerView.adapter = adapter
        companiesViewModel.setDataToAdapter(adapter)

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