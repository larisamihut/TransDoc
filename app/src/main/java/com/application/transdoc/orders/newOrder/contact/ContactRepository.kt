package com.application.transdoc.orders.newOrder.contact

import android.R
import android.content.Context
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.NonNull
import com.application.transdoc.databinding.FragmentAddNewOrderContactBinding
import com.application.transdoc.firestore.Car
import com.application.transdoc.firestore.Company
import com.application.transdoc.firestore.Database
import com.application.transdoc.firestore.Driver
import com.application.transdoc.orders.newOrder.NewOrderRepository
import com.google.firebase.firestore.QuerySnapshot

class ContactRepository : NewOrderRepository() {

    companion object {
        private val TAG = ContactRepository::class.qualifiedName
    }

    fun <T> getElements(
        @NonNull clazz: Class<T>, collection: String, context: Context,
        binding: FragmentAddNewOrderContactBinding, fragment: ContactFragment
    ) {
        val database = Database()
        val dataSource =
            database.getCurrentUserID()?.let {
                database.getCollectionRefference("admins").document(
                    it
                ).collection(collection)
            }

        dataSource?.addSnapshotListener { data, error ->
            if (error != null) {
                Log.d("", "Error getting documents: ", error)
            } else {
                if (data != null) {
                    getIdentifierList(clazz, data, context, binding, fragment)
                } else {
                    Log.d(TAG, "No data retrieved", error)
                }
            }
        }
    }

    private fun <T> getIdentifierList(
        @NonNull clazz: Class<T>,
        data: QuerySnapshot,
        context: Context,
        binding: FragmentAddNewOrderContactBinding,
        fragment: ContactFragment
    ) {
        var spinner: Spinner? = null
        val identifiers: MutableList<String> = mutableListOf()
        when (clazz) {
            Driver::class.java -> {
                fragment.drivers = data.toObjects(clazz)
                data.toObjects(clazz).forEach {
                    it.name?.let { it1 -> identifiers.add(it1) }
                    spinner = binding.driver
                }
            }
            Car::class.java -> {
                fragment.cars = data.toObjects(clazz)
                data.toObjects(clazz).forEach {
                    it.number?.let { it1 -> identifiers.add(it1) }
                    spinner = binding.car
                }
            }
            Company::class.java -> {
                fragment.companies = data.toObjects(clazz)
                data.toObjects(clazz).forEach {
                    it.name?.let { it1 -> identifiers.add(it1) }
                    spinner = binding.company
                }
            }
        }

        val adapter =
            context.let { it1 ->
                ArrayAdapter<String>(
                    it1,
                    R.layout.simple_spinner_item,
                    identifiers
                )
            }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.adapter = adapter
    }
}