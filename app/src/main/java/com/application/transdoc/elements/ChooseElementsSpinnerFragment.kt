package com.application.transdoc.elements

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.application.transdoc.R
import com.application.transdoc.databinding.DialogFragmentChooseElementsBinding
import com.application.transdoc.firestore.Car
import com.google.firebase.firestore.CollectionReference

class ChooseElementsSpinnerFragment(val dataSource: CollectionReference?) : DialogFragment() {

    private lateinit var binding: DialogFragmentChooseElementsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fragment_choose_elements, container, false)

        val carsNames: MutableList<String> = mutableListOf()

        if (dataSource != null) {
            dataSource.addSnapshotListener { data, error ->
                if (error != null) {
                    Log.d("AddNewOrderCarFragment", "Error getting documents: ", error)
                } else {
                    if (data != null) {
                        val cars = data.toObjects(Car::class.java)
                        cars.forEach{
                            it.number?.let { it1 -> carsNames.add(it1) }
                        }
                    } else {
                        Log.d("AddNewOrderCarFragment", "No data retrieved", error)
                    }
                }
            }
        }

        val adapter = activity?.let { ArrayAdapter<String>(it,android.R.layout.simple_expandable_list_item_1, carsNames) }
        binding.lisView.adapter = adapter
        binding.searchBar.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                adapter!!.filter.filter(s)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                adapter!!.filter.filter(s)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter!!.filter.filter(s)
            }
        })

        binding.lisView.setOnItemClickListener(object : AdapterView.OnItemClickListener{
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (adapter != null) {
                    adapter.getItem(position)
                    dialog?.dismiss()
                }
            }

        })
        return binding.root
    }

}