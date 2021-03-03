package com.application.transdoc.orders.newOrder.loading

import android.util.Log
import com.application.transdoc.firestore.Database
import com.application.transdoc.orders.newOrder.NewOrderRepository
import com.google.firebase.firestore.CollectionReference

class LoadingRepository() : NewOrderRepository() {


    companion object {
        private val TAG = LoadingRepository::class.qualifiedName
    }

}