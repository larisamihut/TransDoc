package com.application.transdoc.elements.drivers

import android.app.Application
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.application.transdoc.firestore.Driver
import com.google.firebase.firestore.DocumentReference


class DriversViewModel(
    dataSource: DocumentReference?,
    val activity: FragmentActivity,
    val application: Application
) : ViewModel() {

    val dataSource = dataSource?.collection("drivers")

    fun setDataToAdapter(adapter: DriversAdapter){
        if (dataSource != null) {
            dataSource.addSnapshotListener { data, error ->
                if (error != null) {
                    Log.d(TAG, "Error getting documents: ", error)
                } else {
                    if (data != null) {
                        val drivers = data.toObjects(Driver::class.java)
                        adapter.data = drivers
                        adapter.dataSource = dataSource
                    } else {
                        Log.d(TAG, "No data retrieved", error)
                    }
                }
            }
        }
    }

    fun addNewDriver(){
        val newDriverDialog = NewDriverDialogFragment(dataSource)
        activity.supportFragmentManager.let {
            newDriverDialog.show(it, "NewDriverFragment") }
    }


    companion object {
        private val TAG = DriversViewModel::class.qualifiedName
    }
}