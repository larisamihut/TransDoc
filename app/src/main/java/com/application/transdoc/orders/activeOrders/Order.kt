package com.application.transdoc.orders.activeOrders

import com.application.transdoc.firestore.Element

class Order: Element {
    var describedGoods: String? = null
    var loading: String? = null
    var receivedOrder: String?= null
    var unloading: String? = null
    var contact: String? = null
    var orderId: String? = null
    var pdfDoc: String? = null
}