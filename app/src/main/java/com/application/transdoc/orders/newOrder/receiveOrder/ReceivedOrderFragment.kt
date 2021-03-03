package com.application.transdoc.orders.newOrder.receiveOrder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.application.transdoc.R
import com.application.transdoc.databinding.FragmentAddNewOrderReceiveOrderBinding
import com.application.transdoc.elements.ElementsUtil
import com.application.transdoc.orders.newOrder.contact.ContactFragmentArgs
import java.util.*

class ReceivedOrderFragment : Fragment() {

    companion object {
        private val TAG = ReceivedOrderFragment::class.qualifiedName
    }

    private lateinit var binding: FragmentAddNewOrderReceiveOrderBinding
    private lateinit var navController: NavController
    private lateinit var viewModel: ReceivedOrderViewModel
    private lateinit var myCalendar: Calendar
    private lateinit var elementsUtil: ElementsUtil
    val args: ContactFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                TODO("Not yet implemented")
            }
        } )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_new_order_receive_order,
            container,
            false
        )
        myCalendar = Calendar.getInstance()
        elementsUtil = ElementsUtil()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ReceivedOrderViewModel::class.java)
        viewModel.repository = ReceiveOrderRepository()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        binding.receiveDate.setOnClickListener() {
            context?.let { it1 ->
                elementsUtil.setDate(it1, binding.receiveDate)
            }
        }

        binding.nextButton.setOnClickListener {
            if ((viewModel.validateData(binding.code)) && (viewModel.validateData(binding.company)) && (viewModel.validateData(
                    binding.amountOfMoney
                )) && (viewModel.validateData(binding.deadline)) && (viewModel.validateData(binding.receiveDate))
            ) {
                viewModel.addData(
                    binding.code,
                    binding.deadline,
                    binding.company,
                    binding.receiveDate,
                    binding.amountOfMoney
                )

                val action =
                    ReceivedOrderFragmentDirections.actionAddNewOrderReceiveOrderFragmentToAddNewOrderContactFragment(
                        binding.code.text.toString()
                    )
                navController.navigate(action)
            }
        }
    }
}