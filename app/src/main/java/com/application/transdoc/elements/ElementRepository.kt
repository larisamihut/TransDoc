package com.application.transdoc.elements

import com.application.transdoc.firestore.Database
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference

interface ElementRepository {

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
}