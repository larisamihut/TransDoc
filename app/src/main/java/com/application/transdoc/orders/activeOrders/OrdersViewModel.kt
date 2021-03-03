package com.application.transdoc.orders.activeOrders

import android.util.Log
import androidx.lifecycle.ViewModel
import com.application.transdoc.elements.drivers.OrdersAdapter

class OrdersViewModel  () : ViewModel() {

    lateinit var repository: OrdersRepository

    fun setDataToAdapter(adapter: OrdersAdapter, collectionPath: String){
        val dataSource = repository.getDataSource(collectionPath)
        if (dataSource != null) {
            dataSource.addSnapshotListener { data, error ->
                if (error != null) {
                    Log.d(TAG, "Error getting documents: ", error)
                } else {
                    if (data != null) {
                        val orders = data.toObjects(Order::class.java)
                        adapter.data = orders
                        adapter.dataSource = dataSource
                    } else {
                        Log.d(TAG, "No data retrieved", error)
                    }
                }
            }
        }
    }

    fun createData(
        contact: String?,
        describedGoods: String?,
        loading: String?,
        unloading: String?,
        receivedOrder: String?,
        orderId: String?
    ): HashMap<String, String?> {
        return hashMapOf(
            "contact" to contact,
            "describedGoods" to describedGoods,
            "loading" to loading,
            "unloading" to unloading,
            "receivedOrder" to receivedOrder,
            "orderId" to orderId,
        )
    }

    fun addData(
        contact: String?,
        describedGoods: String?,
        loading: String?,
        unloading: String?,
        receivedOrder: String?,
        orderId: String?,
        collectionPath: String,
    ) {
        repository.addNewOrder(createData(contact, describedGoods, loading, unloading, receivedOrder, orderId), collectionPath, orderId.toString())
    }

    companion object {
        private val TAG = OrdersViewModel::class.qualifiedName
    }
}