package com.application.transdoc.orders.newOrder.decribeGoods

import android.widget.EditText
import androidx.lifecycle.ViewModel
import com.application.transdoc.orders.newOrder.NewOrderRepository
import com.application.transdoc.orders.newOrder.NewOrderViewModel

class DescribedGoodsViewModel: ViewModel(), NewOrderViewModel {

    lateinit var repository: NewOrderRepository

    private fun createData(
        type: EditText,
        weight: EditText,
        unit: EditText,
        money: EditText,
        deadline: EditText
    ): HashMap<String, String?> {
        return hashMapOf(
            "type" to type.text.toString(),
            "weight" to weight.text.toString(),
            "unit" to unit.text.toString(),
            "deadline" to deadline.text.toString(),
            "money" to money.text.toString()
        )
    }

    fun addData(
        type: EditText,
        weight: EditText,
        unit: EditText,
        money: EditText,
        deadline: EditText,
        document: String
    ) {
        repository.addNewElement(createData(type, weight, unit, money, deadline), "describedGoods", document, "describedGoodid")
    }
}