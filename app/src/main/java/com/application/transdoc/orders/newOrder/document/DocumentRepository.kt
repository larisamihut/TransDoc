package com.application.transdoc.orders.newOrder.document

import android.util.Log
import android.widget.EditText
import com.application.transdoc.firestore.Database
import com.application.transdoc.orders.newOrder.NewOrderRepository
import com.application.transdoc.orders.newOrder.unloading.UnloadingRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

class DocumentRepository: NewOrderRepository(){

    private var database : Database = Database()

    companion object{
        private val TAG = DocumentRepository::class.qualifiedName
    }

    fun getDataSource() : CollectionReference {
        return database.getCurrentUserID()?.let {
            database.getCollectionRefference("admins").document(
                it
            ).collection("documents")
        }!!
    }

    fun addNewElement(data: HashMap<String, String>){
        val dataSource = getDataSource()
        dataSource.add(data).addOnSuccessListener { documentReference ->
            Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
        }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    fun savePdfToFireBaseStorage(
        uid: String,
        outputStream: ByteArrayOutputStream
    ) {
        var storage = FirebaseStorage.getInstance();
        var storageReference = storage.reference;

        val uid = uid
        // Defining the child of storageReference
        val ref: StorageReference = storageReference.child(
            "pdfDocs/" + Database().getCurrentUserID().toString()+ "/" + uid + ".pdf"
        )
        val uploadTask = ref.putBytes(outputStream.toByteArray())
        uploadTask.addOnFailureListener {
            Log.i(TAG, "Couldn't upload the pdf")
        }.addOnSuccessListener { taskSnapshot ->
            Log.i(TAG, "Upload successfully the pdf")
        }
    }

    fun addField(document: String, data: HashMap<String, String>){
        val dataSource = getDataSource()
        dataSource.document(document).set(data as Map<String, Any>).addOnSuccessListener { documentReference ->
            Log.d(TAG, "DocumentSnapshot written with ID: ${document}")
        }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    fun updateField(document: String, data: HashMap<String, String>){
        val dataSource = getDataSource()
        dataSource.document(document).update(data as Map<String, Any>).addOnSuccessListener { documentReference ->
            Log.d(TAG, "DocumentSnapshot written with ID: ${document}")
        }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    fun createFieldDocument(value: String, field: String): HashMap<String, String> {
        return hashMapOf(
            field to value
        )
    }
}