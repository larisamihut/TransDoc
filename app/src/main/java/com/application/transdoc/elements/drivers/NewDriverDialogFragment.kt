package com.application.transdoc.elements.drivers

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import com.application.transdoc.databinding.PopupAddNewDriverBinding
import com.application.transdoc.firestore.Driver
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap


class NewDriverDialogFragment(datasource: CollectionReference?) : DialogFragment() {

    private lateinit var binding: PopupAddNewDriverBinding
    private val dataSource = datasource
    private lateinit var addButton: Button
    private lateinit var driverName: EditText
    private lateinit var driverPhone: EditText
    private lateinit var image: CircleImageView
    private lateinit var close: ImageView
    val PICKFILE_RESULT_CODE = 1
    private var fileUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootView: View = inflater.inflate(R.layout.popup_add_new_driver, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.popup_add_new_driver, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addButton = view.findViewById(R.id.add_button)
        driverName = view.findViewById(R.id.driver_name)
        driverPhone = view.findViewById(R.id.driver_phone)
        image = view.findViewById(R.id.driver_image)
        close = view.findViewById(R.id.close_dialog_fragment)

        addButton.setOnClickListener {
            if (validateNewDriver()) {
                onClickAddNewDriver()
            }
        }
        image.setOnClickListener { openFileChooser() }
        close.setOnClickListener { dismiss() }
    }

    private fun openFileChooser() {
        var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
        chooseFile.type = "image/*"
        chooseFile = Intent.createChooser(chooseFile, "Choose a file")
        startActivityForResult(chooseFile, PICKFILE_RESULT_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PICKFILE_RESULT_CODE -> if (resultCode === -1) {
                fileUri = data?.data
                try {
                    val bitmap: Bitmap =
                        MediaStore.Images.Media.getBitmap(context?.getContentResolver(), fileUri)
                    image.setImageBitmap(bitmap)
                    dataSource?.document()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun uploadImage(): String {
        val storage = FirebaseStorage.getInstance()
        val storageReference = storage.getReference()

        val photoId = UUID.randomUUID().toString()
        // Defining the child of storageReference
        val ref: StorageReference = storageReference.child(
            "driversProfile/"
                    + photoId
        )

        ref.putFile(fileUri!!)
            .addOnSuccessListener { // Image uploaded successfully
                Toast
                    .makeText(
                        context,
                        "Photo was succesfully imported",
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }
            .addOnFailureListener { e -> // Error, Image not uploaded
                Toast
                    .makeText(
                        context,
                        "Failed to upload photo" + e.message,
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }
        return photoId
    }

    private fun createData(): HashMap<String, String> {
        if (fileUri == null) {
            return hashMapOf(
                "name" to driverName.text.toString(),
                "phone" to driverPhone.text.toString(),
                "driverId" to "",
                "image" to ""
            )
        } else {
            return hashMapOf(
                "name" to driverName.text.toString(),
                "phone" to driverPhone.text.toString(),
                "driverId" to "",
                "image" to uploadImage()
            )
        }
    }

    private fun onClickAddNewDriver() {
        if (dataSource != null) {
            dataSource.get()
                .addOnSuccessListener { result ->
                    if (!checkIfPhoneExists(result)) {
                        addNewDriver(createData())
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("TAG", "Error getting documents: ", exception)
                }
        }
    }

    private fun checkIfPhoneExists(
        result: QuerySnapshot
    ): Boolean {
        val drivers = result.toObjects(Driver::class.java)
        for (driver in drivers) {
            if ((driver.phone).equals((driverPhone.text.toString()))) {
                Toast.makeText(
                    context,
                    "The driver already exists",
                    Toast.LENGTH_LONG
                )
                    .show()
                return true
            }
        }
        return false
    }

    private fun addNewDriver(data: HashMap<String, String>) {
        if (dataSource != null) {
            dataSource.add(data)
                .addOnSuccessListener { documentReference ->
                    dataSource.document(documentReference.id)
                        .update("driverId", documentReference.id)
                        .addOnSuccessListener {
                            Log.d(
                                "NewDriverDialogFragment",
                                "DocumentSnapshot successfully written!"
                            )
                        }
                        .addOnFailureListener { e ->
                            Log.w(
                                "NewDriverDialogFragment",
                                "Error writing document",
                                e
                            )
                        }
                    Log.d(
                        "NewDriverDialogFragment",
                        "DocumentSnapshot written with ID: ${documentReference.id}"
                    )
                    Toast.makeText(
                        context,
                        "The new driver was added",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
                .addOnFailureListener { e ->
                    Log.w(
                        "NewDriverDialogFragment",
                        "Error adding document",
                        e
                    )
                }
        }
    }

    private fun validateNewDriver(): Boolean {
        if (driverName.text.toString().isEmpty()) {
            driverName.error = "Please write the name"
            return false
        } else if (driverPhone.text.toString().isEmpty()) {
            driverPhone.error = "Please write the phone number"
            return false
        }
        return true
    }

}