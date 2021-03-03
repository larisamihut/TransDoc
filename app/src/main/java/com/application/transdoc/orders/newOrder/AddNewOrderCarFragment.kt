package com.application.transdoc.orders.newOrder

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.application.transdoc.R
import com.application.transdoc.databinding.FragmentAddNewOrderCarBinding
import com.application.transdoc.elements.ElementsUtil
import com.application.transdoc.firestore.Car

import com.application.transdoc.firestore.Database
import com.google.firebase.firestore.CollectionReference


class AddNewOrderCarFragment : Fragment() {
    private lateinit var binding: FragmentAddNewOrderCarBinding
    private lateinit var navController: NavController
    private var cars: MutableList<Car> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_new_order_car, container, false)
        val dataSource = getCurrentUser()?.let {
            Database().getCollectionRefference("admins").document(it).collection("cars")
        }

        val carsNames: MutableList<String> = mutableListOf()

        dataSource?.addSnapshotListener { data, error ->
            if (error != null) {
                Log.d("AddNewOrderCarFragment", "Error getting documents: ", error)
            } else if (data != null) {
                cars = data.toObjects(Car::class.java)
                cars.forEach {
                    it.number?.let { it1 -> carsNames.add(it1) }
                }
            } else {
                Log.d("AddNewOrderCarFragment", "No data retrieved", error)
            }
        }

        val adapter = activity?.let {
            ArrayAdapter<String>(
                it,
                android.R.layout.simple_expandable_list_item_1,
                carsNames
            )
        }
        binding.listView.adapter = adapter
        binding.searchBar.setOnClickListener() {
            binding.listView.isVisible = true
            binding.document.isVisible = false
            binding.buttonNext.isVisible = false
        }



        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                adapter!!.filter.filter(s)
                binding.document.isVisible = false
                binding.buttonNext.isVisible = false
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                adapter!!.filter.filter(s)
                binding.document.isVisible = false
                binding.buttonNext.isVisible = false

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter!!.filter.filter(s)
                binding.document.isVisible = false
                binding.buttonNext.isVisible = false
            }
        })

        binding.listView.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (adapter != null) {
                    adapter.getItem(position)
                    val car = cars[position]
                    setOrderCar(car)
                    binding.listItemCar.saveButton.setOnClickListener() {
                        if (dataSource != null) {
                            updateFirestore(dataSource, setData(), car.carId!!)
                            setOrderCarEdit()
                            binding.buttonNext.isVisible = true
                        }
                    }
                    binding.listView.isVisible = false
                }
            }
        })

        binding.listItemCar.editButton.setOnClickListener() {
            editText()
        }

        val elementsUtil = ElementsUtil()

        binding.listItemCar.itpEdit.setOnClickListener(){
            context?.let { it1 -> elementsUtil.setDate(context = it1, car = binding.listItemCar.itpEdit) }
        }

        binding.listItemCar.assuranceEdit.setOnClickListener(){
            context?.let { it1 -> elementsUtil.setDate(context = it1, car = binding.listItemCar.assuranceEdit) }
        }

        return binding.root
    }

    private fun setOrderCar(car: Car) {
        binding.document.isVisible = true
        binding.buttonNext.isVisible = true
        binding.listItemCar.number.setText(car.number.toString())
        binding.listItemCar.itp.setText(car.itp.toString())
        binding.listItemCar.assurance.setText(car.assurance.toString())
        binding.listItemCar.type.setText(car.type.toString())
    }

    private fun setOrderCarEdit(){
        binding.buttonNext.isVisible = false
        binding.document.isVisible = true
        binding.listItemCar.number.setText(binding.listItemCar.numberEdit.text)
        binding.listItemCar.itp.setText(binding.listItemCar.itpEdit.text)
        binding.listItemCar.assurance.setText(binding.listItemCar.assuranceEdit.text)
        binding.listItemCar.type.setText(binding.listItemCar.typeEdit.text)
    }

    fun editText() {
        binding.buttonNext.isVisible = false
        binding.listItemCar.number.setVisibility(View.GONE)
        binding.listItemCar.type.setVisibility(View.GONE)
        binding.listItemCar.itp.setVisibility(View.GONE)
        binding.listItemCar.assurance.setVisibility(View.GONE)
        binding.listItemCar.numberEdit.setVisibility(View.VISIBLE)
        binding.listItemCar.typeEdit.setVisibility(View.VISIBLE)
        binding.listItemCar.itpEdit.setVisibility(View.VISIBLE)
        binding.listItemCar.assuranceEdit.setVisibility(View.VISIBLE)
        binding.listItemCar.numberEdit.setText(binding.listItemCar.number.text)
        binding.listItemCar.typeEdit.setText(binding.listItemCar.type.text)
        binding.listItemCar.itpEdit.setText(binding.listItemCar.itp.text)
        binding.listItemCar.assuranceEdit.setText(binding.listItemCar.assurance.text)
        binding.listItemCar.editButton.setVisibility(View.GONE)
        binding.listItemCar.saveButton.setVisibility(View.VISIBLE)
    }

    fun setData(): HashMap<String, String> {
        val data = hashMapOf(
            "number" to binding.listItemCar.numberEdit.text.toString(),
            "type" to binding.listItemCar.typeEdit.text.toString(),
            "itp" to binding.listItemCar.itpEdit.text.toString(),
            "assurance" to binding.listItemCar.assuranceEdit.text.toString()
        )

        binding.listItemCar.saveButton.setVisibility(View.GONE)
        binding.listItemCar.editButton.setVisibility(View.VISIBLE)
        binding.listItemCar.number.setVisibility(View.VISIBLE)
        binding.listItemCar.type.setVisibility(View.VISIBLE)
        binding.listItemCar.itp.setVisibility(View.VISIBLE)
        binding.listItemCar.assuranceEdit.setVisibility(View.VISIBLE)
        binding.listItemCar.numberEdit.setVisibility(View.INVISIBLE)
        binding.listItemCar.typeEdit.setVisibility(View.GONE)
        binding.listItemCar.itpEdit.setVisibility(View.INVISIBLE)
        binding.listItemCar.assuranceEdit.setVisibility(View.GONE)

        return data
    }

    fun updateFirestore(
        dataSource: CollectionReference?,
        data: Map<String, String>,
        uid: String
    ) {
        dataSource?.document(uid)?.update(data)
            ?.addOnSuccessListener {
                Log.d(
                    "CarsAdapter",
                    "DocumentSnapshot successfully written!"
                )
            }
            ?.addOnFailureListener { e -> Log.w("CarsAdapter", "Error writing document", e) }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()

        binding.buttonNext.setOnClickListener(){

        }
    }

    private fun getCurrentUser(): String? {
        return FirebaseAuth.getInstance().uid
    }

}