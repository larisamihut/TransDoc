package com.application.transdoc.firestore

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class Database {

    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var collectionReference: CollectionReference

    fun getCollectionRefference(collectionPath: String): CollectionReference {
        collectionReference = database.collection(collectionPath)
        return collectionReference
    }

    fun getCurrentUser() : FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    fun getCurrentUserID() : String? {
        return FirebaseAuth.getInstance().uid
    }
}