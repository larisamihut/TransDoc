package com.application.transdoc.elements.companies

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.application.transdoc.R
import com.application.transdoc.databinding.DialogFragmentAddNewCompanyBinding
import com.application.transdoc.firestore.Company
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import kotlin.collections.HashMap

class NewCompanyDialogFragment(datasource: CollectionReference?) : DialogFragment() {

    private lateinit var binding: DialogFragmentAddNewCompanyBinding
    private val dataSource = datasource
    private lateinit var addButton: Button
    private lateinit var close: ImageView
    private lateinit var companyName: EditText
    private lateinit var companyContact: EditText
    private lateinit var companyNr: EditText
    private lateinit var companyCui: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView: View =
            inflater.inflate(R.layout.dialog_fragment_add_new_company, container, false)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_fragment_add_new_company,
            container,
            false
        )

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addButton = view.findViewById(R.id.add_button)
        close = view.findViewById(R.id.close_dialog_fragment)
        companyName = view.findViewById(R.id.name)
        companyContact = view.findViewById(R.id.contact)
        companyNr = view.findViewById(R.id.nr)
        companyCui = view.findViewById(R.id.cui)

        addButton.setOnClickListener {
            if (validateNewCar()) {
                onClickAddNewCompany()
            }
        }
        close.setOnClickListener { dismiss() }

    }

    private fun createData(): HashMap<String, String> {
        return hashMapOf(
            "name" to companyName.text.toString(),
            "contact" to companyContact.text.toString(),
            "nr" to companyNr.text.toString(),
            "cui" to companyCui.text.toString()
        )
    }

    private fun onClickAddNewCompany() {
        if (dataSource != null) {
            dataSource.get()
                .addOnSuccessListener { result ->
                    if (!checkIfCompanyExists(result)) {
                        addNewCompany(createData())
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("TAG", "Error getting documents: ", exception)
                }
        }
    }

    private fun checkIfCompanyExists(
        result: QuerySnapshot
    ): Boolean {
        val companies = result.toObjects(Company::class.java)
        for (company in companies) {
            if ((company.cui).equals((companyCui.text.toString()))) {
                Toast.makeText(
                    context,
                    "The car already exists",
                    Toast.LENGTH_LONG
                )
                    .show()
                return true
            }
        }
        return false
    }

    private fun addNewCompany(data: HashMap<String, String>) {
        if (dataSource != null) {
            dataSource.add(data)
                .addOnSuccessListener { documentReference ->
                    dataSource?.document(documentReference.id)
                        ?.update("companyId", documentReference.id)
                        ?.addOnSuccessListener {
                            Log.d(
                                "NewCarDialogFragment",
                                "DocumentSnapshot successfully written!"
                            )
                        }
                        ?.addOnFailureListener { e ->
                            Log.w(
                                "NewCompDialogFragment",
                                "Error writing document",
                                e
                            )
                        }
                    Log.d(
                        "NewCompDialogFragment",
                        "DocumentSnapshot written with ID: ${documentReference.id}"
                    )
                    Toast.makeText(
                        context,
                        "The new company was added",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
                .addOnFailureListener { e ->
                    Log.w(
                        "NewCarDialogFragment",
                        "Error adding document",
                        e
                    )
                }
        }
    }

    private fun validateNewCar(): Boolean {
        if (companyName.text.toString().isEmpty()) {
            companyName.error = "Please write the name"
            return false
        } else if (companyContact.text.toString().isEmpty()) {
            companyContact.error = "Please write the contact person"
            return false
        } else if (companyCui.text.toString().isEmpty()) {
            companyCui.error = "Please expiracy date of the CUI"
            return false
        } else if (companyNr.text.toString().isEmpty()) {
            companyNr.error = "Please write the Nr/OCR"
            return false
        }
        return true
    }

}