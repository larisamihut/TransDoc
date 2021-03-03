package com.application.transdoc.elements.companies

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.application.transdoc.elements.drivers.DriversViewModel
import com.google.firebase.firestore.DocumentReference

class CompaniesViewModelFactory(
    private val dataSource: DocumentReference?,
    private val activity: FragmentActivity,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CompaniesViewModel::class.java)) {
            return CompaniesViewModel(dataSource, activity, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}