package com.application.transdoc.orders.newOrder.decribeGoods

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.application.transdoc.R
import com.application.transdoc.databinding.FragmentAddNewOrderDescribeGoodsBinding
import com.application.transdoc.orders.newOrder.contact.ContactFragmentArgs

class DescribedGoodsFragment : Fragment() {

    companion object {
        private val TAG = DescribedGoodsFragment::class.qualifiedName
    }

    private lateinit var binding: FragmentAddNewOrderDescribeGoodsBinding
    private lateinit var navController: NavController
    private lateinit var viewModel: DescribedGoodsViewModel
    val args: ContactFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_new_order_describe_goods,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        binding.nextButton.setOnClickListener {
            val documentId = args.documentId
            if ((viewModel.validateData(binding.amountOfMoney)) && (viewModel.validateData(binding.deadline)) && (viewModel.validateData(
                    binding.type
                )) && (viewModel.validateData(binding.weight)) && (viewModel.validateData(binding.unit))
            ) {

                viewModel.addData(
                    binding.type,
                    binding.deadline,
                    binding.weight,
                    binding.unit,
                    binding.amountOfMoney,
                    documentId
                )

                val action =
                    DescribedGoodsFragmentDirections.actionAddNewOrderDescribeGoodsFragmentToAddNewOrderLoadingFragment(
                        documentId
                    )
                navController.navigate(action)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DescribedGoodsViewModel::class.java)
        viewModel.repository = DescribedGoodsRepository()
    }

}