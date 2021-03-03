package com.application.transdoc.orders.newOrder.unloading

import android.util.Log
import com.application.transdoc.firestore.Database
import com.application.transdoc.orders.newOrder.NewOrderRepository
import com.google.firebase.firestore.CollectionReference

class UnloadingRepository(): NewOrderRepository() {

    private var database : Database = Database()

    companion object{
        private val TAG = UnloadingRepository::class.qualifiedName
    }

}