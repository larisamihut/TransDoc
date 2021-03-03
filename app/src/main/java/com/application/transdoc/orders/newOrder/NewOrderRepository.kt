package com.application.transdoc.orders.newOrder

import android.util.Log
import com.application.transdoc.firestore.Database
import com.application.transdoc.orders.newOrder.document.DocumentRepository
import com.application.transdoc.orders.pdf.PdfGenerator
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

open class NewOrderRepository {

    companion object {
        private val TAG = NewOrderRepository::class.qualifiedName
    }

    fun getDataSource(collectionPath: String): CollectionReference {
        val database = Database()
        return database.getCurrentUserID()?.let {
            database.getCollectionRefference("admins").document(
                it
            ).collection(collectionPath)
        }!!
    }


    fun getSpecificDocument(collectionPath: String, documentUid: String): DocumentReference {
        return getDataSource(collectionPath).document(documentUid)
    }

    fun deleteDocument(collectionPath: String, documentUid: String) {
        getDataSource(collectionPath).document(documentUid).delete()
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
    }

    open fun addNewElement(
        data: HashMap<String, String?>,
        collectionPath: String,
        document: String,
        uidField: String
    ) {
        val dataSource = getDataSource(collectionPath)
        dataSource.add(data).addOnSuccessListener { documentReference ->
            val uid = documentReference.id
            val documentRepository = DocumentRepository()
            documentRepository.updateField(
                document,
                documentRepository.createFieldDocument(uid, collectionPath)
            )
            dataSource.document(documentReference.id)
                .update(uidField, documentReference.id)
            Log.d(
                "TAG",
                "DocumentSnapshot written with ID"
            )
        }
    }
}
