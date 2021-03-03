package com.application.transdoc.orders.newOrder.unloading

import android.widget.EditText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.application.transdoc.orders.newOrder.NewOrderRepository
import com.application.transdoc.orders.newOrder.NewOrderViewModel
import com.application.transdoc.orders.pdf.PdfGenerator

class UnloadingViewModel: NewOrderViewModel, ViewModel() {

    lateinit var repository: NewOrderRepository


    fun createData(
        name: EditText,
        address: EditText,
        time: EditText,
        date: EditText,
    ): HashMap<String, String?> {
        return hashMapOf(
            "name" to name.text.toString(),
            "address" to address.text.toString(),
            "unit" to time.text.toString(),
            "deadline" to date.text.toString(),
        )
    }

    fun addData(
        name: EditText,
        address: EditText,
        time: EditText,
        date: EditText,
        collectionPath: String,
        document: String
    ) {
        repository.addNewElement(createData(name, address, time, date), collectionPath, document, "loadId")
    }
}