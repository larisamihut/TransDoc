package com.application.transdoc.orders.newOrder

import android.widget.EditText
import com.application.transdoc.orders.newOrder.document.DocumentRepository

interface NewOrderViewModel {

    fun validateData(editText: EditText): Boolean {
        return if (editText.text.toString().isEmpty()) {
            editText.error = "Please complete the field"
            false
        }else true
    }

    private fun createFieldDocument(value: String, field: String): HashMap<String, String>{
        return hashMapOf(
            field to value
        )
    }

    fun addDocumentField(value: String, field: String){
        val documentRepository = DocumentRepository()
        documentRepository.addField(value, createFieldDocument(value,field))
    }

}