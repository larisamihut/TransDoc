package com.application.transdoc.orders.newOrder.loading

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.application.transdoc.R
import com.application.transdoc.databinding.FragmentAddNewOrderLoadingBinding
import com.application.transdoc.elements.ElementsUtil

class LoadingFragment : Fragment() {

    companion object {
        const val TAG = "AddNewOrderLoadingFra"
    }

    private lateinit var viewModel: LoadingViewModel
    private lateinit var navController: NavController
    private lateinit var binding: FragmentAddNewOrderLoadingBinding
    private lateinit var util: ElementsUtil
    val args: LoadingFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_add_new_order_loading,
                container,
                false
            )
        util = ElementsUtil()


        binding.driverImage.setImageResource(R.drawable.ic_baseline_cloud_upload_24)
        binding.loading.setText(getString(R.string.describe_loading_proccess))

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        binding.nextButton.setOnClickListener() {
            val documentId = args.documentId
            if ((viewModel.validateData(binding.name)) && (viewModel.validateData(binding.address)) && (viewModel.validateData(
                    binding.timeSlot
                )) && (viewModel.validateData(binding.date))
            ) {
                viewModel.addData(
                    binding.name,
                    binding.address,
                    binding.timeSlot,
                    binding.date,
                    "loading",
                    documentId
                )

                val action =
                    LoadingFragmentDirections.actionAddNewOrderLoadingFragmentToAddNewOrderUnloadingFragment(
                        documentId
                    )
                navController.navigate(action)
            }
        }

        binding.date.setOnClickListener() {
            context?.let { it1 ->
                util.setDate(it1, binding.date)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoadingViewModel::class.java)
        viewModel.repository = LoadingRepository()
    }

}