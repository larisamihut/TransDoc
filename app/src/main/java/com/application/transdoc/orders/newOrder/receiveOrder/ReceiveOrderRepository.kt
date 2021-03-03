package com.application.transdoc.orders.newOrder.receiveOrder

import android.util.Log
import com.application.transdoc.orders.newOrder.NewOrderRepository
import com.application.transdoc.orders.newOrder.document.DocumentRepository

class ReceiveOrderRepository() : NewOrderRepository() {

    companion object {
        private val TAG = ReceiveOrderRepository::class.qualifiedName
    }

    fun addNewElement(data: HashMap<String, String?>, collectionPath:String, code: String) {
        val dataSource = getDataSource(collectionPath)
        dataSource.add(data).addOnSuccessListener { documentReference ->
            val uid = documentReference.id
            val documentRepository = DocumentRepository()
            documentRepository.addField(code, documentRepository.createFieldDocument(uid, collectionPath))
            documentRepository.updateField(code, documentRepository.createFieldDocument(code,"orderId"))
            dataSource.document(documentReference.id)
                .update("receiveOrderId", documentReference.id)
            Log.d(
                TAG,
                "DocumentSnapshot written with ID"
            )
        }
    }
}