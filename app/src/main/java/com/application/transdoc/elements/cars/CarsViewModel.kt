package com.application.transdoc.elements.cars

import android.app.Application
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.application.transdoc.elements.cars.newcar.NewCarFragment
import com.application.transdoc.firestore.Car
import com.google.firebase.firestore.DocumentReference

class CarsViewModel (
    dataSource: DocumentReference?,
    val activity: FragmentActivity,
    val application: Application
) : ViewModel() {

    val dataSource = dataSource?.collection("cars")

    fun setDataToAdapter(adapter: CarsAdapter){
        if (dataSource != null) {
            dataSource.addSnapshotListener { data, error ->
                if (error != null) {
                    Log.d(TAG, "Error getting documents: ", error)
                } else {
                    if (data != null) {
                        val cars = data.toObjects(Car::class.java)
                        adapter.data = cars
                        adapter.dataSource = dataSource
                    } else {
                        Log.d(TAG, "No data retrieved", error)
                    }
                }
            }
        }
    }

    fun addNewCar(){
        var newCarDialog = NewCarFragment(dataSource)
        activity.supportFragmentManager.let {
            newCarDialog.show(it, "NewCarFragment") }
    }


    companion object {
        private val TAG = CarsViewModel::class.qualifiedName
    }
}