package com.application.transdoc.elements.cars.newcar

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
import com.application.transdoc.databinding.DialogFragmentAddNewCarBinding
import com.application.transdoc.elements.ElementsUtil
import com.application.transdoc.firestore.Car
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import java.util.*
import kotlin.collections.HashMap

class NewCarFragment(datasource: CollectionReference?) : DialogFragment() {

    private lateinit var binding: DialogFragmentAddNewCarBinding
    private val dataSource = datasource
    private lateinit var addButton: Button
    private lateinit var close: ImageView
    private lateinit var carNumber: EditText
    private lateinit var carType: EditText
    private lateinit var carItp: EditText
    private lateinit var carAssurance: EditText
    private lateinit var myCalendar: Calendar


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView: View =
            inflater.inflate(R.layout.dialog_fragment_add_new_car, container, false)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_fragment_add_new_car,
            container,
            false
        )

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val elementsUtil = ElementsUtil()
        addButton = view.findViewById(R.id.add_button)
        close = view.findViewById(R.id.close_dialog_fragment)
        carNumber = view.findViewById(R.id.number)
        carType = view.findViewById(R.id.type)
        carItp = view.findViewById(R.id.itp)
        carAssurance = view.findViewById(R.id.assurance)

        addButton.setOnClickListener {
            if (validateNewCar()) {
                onClickAddNewCar()
            }
        }
        close.setOnClickListener { dismiss() }

        myCalendar = Calendar.getInstance()

        carItp.setOnClickListener() {
            context?.let { it1 ->
                elementsUtil.setDate(it1, carItp)
            }
        }

        carAssurance.setOnClickListener() {
            context?.let { it1 ->
                elementsUtil.setDate(it1, carAssurance)
            }
        }
    }

    private fun createData(): HashMap<String, String> {
        return hashMapOf(
            "number" to carNumber.text.toString(),
            "type" to carType.text.toString(),
            "itp" to carItp.text.toString(),
            "assurance" to carAssurance.text.toString()
        )
    }

    private fun onClickAddNewCar() {
        if (dataSource != null) {
            dataSource.get()
                .addOnSuccessListener { result ->
                    if (!checkIfCarExists(result)) {
                        addNewCar(createData())
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("TAG", "Error getting documents: ", exception)
                }
        }
    }

    private fun checkIfCarExists(
        result: QuerySnapshot
    ): Boolean {
        val cars = result.toObjects(Car::class.java)
        for (car in cars) {
            if ((car.number).equals((carNumber.text.toString()))) {
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

    private fun addNewCar(data: HashMap<String, String>) {
        if (dataSource != null) {
            dataSource.add(data)
                .addOnSuccessListener { documentReference ->
                    dataSource?.document(documentReference.id)
                        ?.update("carId", documentReference.id)
                        ?.addOnSuccessListener {
                            Log.d(
                                "NewCarDialogFragment",
                                "DocumentSnapshot successfully written!"
                            )
                        }
                        ?.addOnFailureListener { e ->
                            Log.w(
                                "NewCarDialogFragment",
                                "Error writing document",
                                e
                            )
                        }
                    Log.d(
                        "NewCarDialogFragment",
                        "DocumentSnapshot written with ID: ${documentReference.id}"
                    )
                    Toast.makeText(
                        context,
                        "The new car was added",
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
        if (carNumber.text.toString().isEmpty()) {
            carNumber.error = "Please write the number"
            return false
        } else if (carType.text.toString().isEmpty()) {
            carType.error = "Please write the type"
            return false
        } else if (carItp.text.toString().isEmpty()) {
            carItp.error = "Please expiracy date of the ITP"
            return false
        } else if (carAssurance.text.toString().isEmpty()) {
            carAssurance.error = "Please write the expiracy date of the assurance"
            return false
        }
        return true
    }

}