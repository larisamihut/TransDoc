package com.application.transdoc.orders.newOrder.contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.application.transdoc.R
import com.application.transdoc.databinding.FragmentAddNewOrderContactBinding
import com.application.transdoc.firestore.Car
import com.application.transdoc.firestore.Company
import com.application.transdoc.firestore.Driver


class ContactFragment : Fragment() {
    lateinit var binding: FragmentAddNewOrderContactBinding
    private lateinit var navController: NavController
    private lateinit var viewModel: ContactViewModel
    val args: ContactFragmentArgs by navArgs()
    var cars: MutableList<Car>? = null
    var drivers: MutableList<Driver>? = null
    var companies: MutableList<Company>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_add_new_order_contact,
                container,
                false
            )
        viewModel = ViewModelProvider(this).get(ContactViewModel::class.java)
        viewModel.repository = ContactRepository()

        context?.let { viewModel.setAdapter(Car::class.java, "cars", it, binding, this) }
        context?.let { viewModel.setAdapter(Driver::class.java, "drivers", it, binding,this) }
        context?.let { viewModel.setAdapter(Company::class.java, "companies", it, binding, this) }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        var company: String? = null
        var car: String? = null
        var driver: String? = null

        binding.company.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var text = parent?.getItemAtPosition(position).toString()
                company = companies?.get(position)?.companyId.toString()
                Toast.makeText(context, text, LENGTH_LONG).show()
            }
        }

        binding.car.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var text = parent?.getItemAtPosition(position).toString()
                car = cars?.get(position)?.carId.toString()
                Toast.makeText(context, text, LENGTH_LONG).show()
            }
        }

        binding.driver.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                var text = parent?.getItemAtPosition(position).toString()
                driver = drivers?.get(position)?.driverId.toString()
                Toast.makeText(context, text, LENGTH_LONG).show()
            }
        }

        binding.nextButton.setOnClickListener() {
            val documentId = args.documentId
            val action =
                ContactFragmentDirections.actionAddNewOrderContactFragmentToAddNewOrderDescribeGoodsFragment(
                    documentId
                )
            viewModel.addData(company,driver,car,documentId)
            navController.navigate(action)
        }
    }

    companion object {
        private val TAG = ContactFragment::class.qualifiedName
    }
}

