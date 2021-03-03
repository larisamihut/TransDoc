package com.application.transdoc.orders.newOrder.receiveOrder

import android.widget.EditText
import androidx.lifecycle.ViewModel
import com.application.transdoc.orders.newOrder.NewOrderViewModel

class ReceivedOrderViewModel : ViewModel(), NewOrderViewModel {

    lateinit var repository: ReceiveOrderRepository

    fun createData(
        code: EditText,
        deadline: EditText,
        company: EditText,
        date: EditText,
        money: EditText
    ): HashMap<String, String?> {
        return hashMapOf(
            "code" to code.text.toString(),
            "date" to date.text.toString(),
            "company" to company.text.toString(),
            "deadline" to deadline.text.toString(),
            "money" to money.text.toString()
        )
    }

    fun addData(
        code: EditText,
        deadline: EditText,
        company: EditText,
        date: EditText,
        money: EditText,
    ) {
        repository.addNewElement(createData(code, deadline, company, date, money), "receivedOrder", code.text.toString())
    }

}