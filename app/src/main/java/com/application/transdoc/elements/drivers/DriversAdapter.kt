package com.application.transdoc.elements.drivers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.application.transdoc.R
import com.application.transdoc.firestore.Driver
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File
import java.io.IOException
import java.lang.Exception

class DriversAdapter(dataSource: DocumentReference?) : RecyclerView.Adapter<DriversAdapter.ViewHolder>() {

    var data = listOf<Driver>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var dataSource: CollectionReference? = null

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        val uid = item.driverId
        uid?.let { holder.bind(item, it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent,dataSource)
    }

    class ViewHolder private constructor(
        itemView: View,
        val dataSource: CollectionReference?
    ) : RecyclerView.ViewHolder(itemView) {
        val driverName: TextView = itemView.findViewById(R.id.driver_name)
        val driverPhone: TextView = itemView.findViewById(R.id.driver_phone)
        val driverNameEdit: EditText = itemView.findViewById(R.id.driver_name_edit)
        val driverPhoneEdit: EditText = itemView.findViewById(R.id.driver_phone_edit)
        val driverImage: CircleImageView = itemView.findViewById(R.id.driver_image)
        val edit_button: ImageView = itemView.findViewById(R.id.edit_button)
        val save_button: ImageView = itemView.findViewById(R.id.save_button)

        fun bind(item: Driver, uid: String) {
            driverName.setText(item.name)
            driverPhone.setText(item.phone)
            if (item.image.toString().length != 0) {
                setDriverImage(item)
            }

            edit_button.setOnClickListener() {
                editText()
            }

            save_button.setOnClickListener() {
                if (dataSource != null) {
                    updateFirestore(dataSource, setData(), uid)
                }
            }

        }

        fun editText() {
            driverName.setVisibility(View.GONE)
            driverPhone.setVisibility(View.GONE)
            driverNameEdit.setVisibility(View.VISIBLE)
            driverPhoneEdit.setVisibility(View.VISIBLE)
            driverNameEdit.setText(driverName.text)
            driverPhoneEdit.setText(driverPhone.text)
            edit_button.setVisibility(View.GONE)
            save_button.setVisibility(View.VISIBLE)
        }

        fun setData(): HashMap<String, String> {
            val data = hashMapOf(
                "name" to driverNameEdit.text.toString(),
                "phone" to driverPhoneEdit.text.toString()
            )

            save_button.setVisibility(View.GONE)
            edit_button.setVisibility(View.VISIBLE)
            driverName.setVisibility(View.VISIBLE)
            driverPhone.setVisibility(View.VISIBLE)
            driverNameEdit.setVisibility(View.GONE)
            driverPhoneEdit.setVisibility(View.GONE)

            return data
        }

        fun updateFirestore(
            dataSource: CollectionReference?,
            data: Map<String, String>,
            uid: String
        ) {
            dataSource?.document(uid)?.update(data)
                ?.addOnSuccessListener { Log.d("DriversAdapter", "DocumentSnapshot successfully written!") }
                ?.addOnFailureListener { e -> Log.w("DriversAdapter", "Error writing document", e) }

        }

        private fun setDriverImage(item: Driver) {
            val storageReference: StorageReference = FirebaseStorage.getInstance()
                .getReferenceFromUrl("gs://transdoc-98601.appspot.com/driversProfile/")
                .child(item.image.toString())
            try {
                val file: File = File.createTempFile("image", "jpeg")
                storageReference.getFile(file).addOnSuccessListener(object :
                    OnSuccessListener<FileDownloadTask.TaskSnapshot> {
                    override fun onSuccess(fileDownloadTask: FileDownloadTask.TaskSnapshot?) {
                        val bitmap: Bitmap = BitmapFactory.decodeFile(file.absolutePath)
                        driverImage.setImageBitmap(bitmap)
                    }
                }).addOnFailureListener(object : OnFailureListener {
                    override fun onFailure(exception: Exception) {
                        Log.d("DriversAdapter", "Error getting documents: ", exception)
                    }
                })
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        companion object {
            fun from(parent: ViewGroup, dataSource: CollectionReference?): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val dataSource = dataSource
                val view = layoutInflater
                    .inflate(R.layout.list_item_driver, parent, false)
                return ViewHolder(view, dataSource)
            }
        }

    }

}

