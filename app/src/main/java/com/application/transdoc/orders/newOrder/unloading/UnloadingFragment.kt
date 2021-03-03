package com.application.transdoc.orders.newOrder.unloading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.application.transdoc.R
import com.application.transdoc.databinding.FragmentAddNewOrderLoadingBinding
import com.application.transdoc.elements.ElementsUtil
import com.application.transdoc.orders.newOrder.contact.ContactFragmentArgs
import com.application.transdoc.orders.newOrder.loading.LoadingFragment
import com.application.transdoc.orders.pdf.PdfGenerator

class UnloadingFragment : Fragment() {

    companion object {
        const val TAG = "AddNewOrderUnloadingFra"
        fun newInstance() = LoadingFragment()
    }


    private lateinit var viewModel: UnloadingViewModel
    private lateinit var binding: FragmentAddNewOrderLoadingBinding
    private lateinit var navController: NavController
    private lateinit var util: ElementsUtil
    private lateinit var pdfGenerator: PdfGenerator
    private lateinit var documentId: String
    val args: ContactFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        documentId = args.documentId

        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_add_new_order_loading,
                container,
                false
            )
        util = ElementsUtil()


        binding.driverImage.setImageResource(R.drawable.ic_baseline_cloud_download_24)
        binding.loading.setText(getString(R.string.describe_unloading_proccess))
        binding.nextButton.setText(getString(R.string.generate_order))

        viewModel = ViewModelProvider(this).get(UnloadingViewModel::class.java)
        viewModel.repository = UnloadingRepository()
        pdfGenerator = PdfGenerator(documentId, context)
        /**/
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        binding.nextButton.setOnClickListener() {
            if ((viewModel.validateData(binding.name)) && (viewModel.validateData(binding.address)) && (viewModel.validateData(
                    binding.timeSlot
                )) && (viewModel.validateData(binding.date))
            ) {
                viewModel.addData(
                    binding.name,
                    binding.address,
                    binding.timeSlot,
                    binding.date,
                    "unloading",
                    documentId
                )
                pdfGenerator.retrieveDataForPdfFromDatabase(documentId)
            }
            navController.navigate(R.id.action_global_ordersMainFragment)
        }

        binding.date.setOnClickListener() {
            context?.let { it1 ->
                util.setDate(it1, binding.date)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}