package com.application.transdoc.orders.activeOrders

import android.util.Log
import com.application.transdoc.elements.ElementRepository

class OrdersRepository: ElementRepository {

    companion object {
        private val TAG = OrdersRepository::class.qualifiedName
    }

    fun addNewOrder(
        data: HashMap<String, String?>,
        collectionPath: String,
        documentPath: String
    ) {
        val dataSource = getDataSource(collectionPath)
        dataSource.document(documentPath).set(data).addOnSuccessListener { documentReference ->
            Log.d(
                "TAG",
                "DocumentSnapshot written with ID"
            )
        }
    }

    fun deleteDocument(collectionPath: String, documentUid: String) {
        getDataSource(collectionPath).document(documentUid).delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

}