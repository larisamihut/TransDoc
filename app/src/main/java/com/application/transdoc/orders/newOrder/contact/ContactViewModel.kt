package com.application.transdoc.orders.newOrder.contact

import android.content.Context
import androidx.annotation.NonNull
import androidx.lifecycle.ViewModel
import com.application.transdoc.databinding.FragmentAddNewOrderContactBinding
import com.application.transdoc.orders.newOrder.NewOrderViewModel
import com.application.transdoc.orders.newOrder.document.DocumentRepository


class ContactViewModel: ViewModel(), NewOrderViewModel {

    lateinit var repository: ContactRepository

    fun createData(
        company: String?,
        driver: String?,
        car: String?
    ): HashMap<String, String?> {
        return hashMapOf(
            "company" to company,
            "driver" to driver,
            "car" to car
        )
    }

    fun addData(
        company: String?,
        driver: String?,
        car: String?,
        document: String
    ) {
       repository.addNewElement(createData(company, driver, car),"contact", document, "contactId")
    }

    private fun createFieldDocument(uid: String): HashMap<String, String>{
        return hashMapOf(
            "contact" to uid
        )
    }

    override fun addDocumentField(value : String, uid: String){
        val documentRepository = DocumentRepository()
        documentRepository.updateField(value, createFieldDocument(uid))
    }


    fun <T>setAdapter(@NonNull clazz: Class<T>, collection: String, context: Context, binding: FragmentAddNewOrderContactBinding, fragment: ContactFragment){
        repository.getElements(clazz, collection, context, binding, fragment)
    }
}