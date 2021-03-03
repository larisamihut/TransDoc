package com.application.transdoc.elements.companies

import android.app.Application
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.application.transdoc.firestore.Company
import com.google.firebase.firestore.DocumentReference

class CompaniesViewModel(
    dataSource: DocumentReference?,
    val activity: FragmentActivity,
    val application: Application
) : ViewModel() {

    val dataSource = dataSource?.collection("companies")

    fun setDataToAdapter(adapter: CompaniesAdapter) {
        if (dataSource != null) {
            dataSource.addSnapshotListener { data, error ->
                if (error != null) {
                    Log.d(TAG, "Error getting documents: ", error)
                } else {
                    if (data != null) {
                        val companies = data.toObjects(Company::class.java)
                        adapter.data = companies
                        adapter.dataSource = dataSource
                    } else {
                        Log.d(TAG, "No data retrieved", error)
                    }
                }
            }
        }
    }
    fun addNewCompany(){
        val newCompanyDialog = NewCompanyDialogFragment(dataSource)
        activity.supportFragmentManager.let {
            newCompanyDialog.show(it, "NewCompanyFragment") }
    }

    companion object {
        private val TAG = CompaniesViewModel::class.qualifiedName
    }
}