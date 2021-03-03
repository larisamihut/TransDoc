package com.application.transdoc.orders.cashedOrders

import android.util.Log
import androidx.lifecycle.ViewModel
import com.application.transdoc.elements.drivers.CashedOrdersAdapter
import com.application.transdoc.orders.activeOrders.Order
import com.application.transdoc.orders.activeOrders.OrdersRepository
import com.application.transdoc.orders.activeOrders.OrdersViewModel

class CashedOrdersViewModel: ViewModel() {

    lateinit var repository: OrdersRepository

    fun setDataToAdapter(adapter: CashedOrdersAdapter, collectionPath: String){
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