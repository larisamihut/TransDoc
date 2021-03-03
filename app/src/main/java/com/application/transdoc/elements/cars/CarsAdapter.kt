package com.application.transdoc.elements.cars

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
import com.application.transdoc.elements.ElementsUtil
import com.application.transdoc.firestore.Car
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.IOException
import java.lang.Exception

class CarsAdapter(dataSource: DocumentReference?) : RecyclerView.Adapter<CarsAdapter.ViewHolder>() {

    var data = listOf<Car>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var dataSource: CollectionReference? = null

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        val uid = item.carId
        uid?.let { holder.bind(item, it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, dataSource)
    }

    class ViewHolder private constructor(
        itemView: View,
        val dataSource: CollectionReference?
    ) : RecyclerView.ViewHolder(itemView) {
        private val carNumber: TextView = itemView.findViewById(R.id.number)
        val carType: TextView = itemView.findViewById(R.id.type)
        val carItp: TextView = itemView.findViewById(R.id.itp)
        val carAssurance: TextView = itemView.findViewById(R.id.assurance)
        val carNumberEdit: EditText = itemView.findViewById(R.id.number_edit)
        val carTypeEdit: EditText = itemView.findViewById(R.id.type_edit)
        val carItpEdit: EditText = itemView.findViewById(R.id.itp_edit)
        val carAssuranceEdit: EditText = itemView.findViewById(R.id.assurance_edit)
        val edit_button: ImageView = itemView.findViewById(R.id.edit_button)
        val save_button: ImageView = itemView.findViewById(R.id.save_button)
        val elementsUtil : ElementsUtil = ElementsUtil()

        fun bind(item: Car, uid: String) {
            carNumber.setText(item.number)
            carType.setText(item.type)
            carItp.setText(item.itp)
            carAssurance.setText(item.assurance)

            edit_button.setOnClickListener() {
                editText()
            }

            save_button.setOnClickListener() {
                if (dataSource != null) {
                    updateFirestore(dataSource, setData(), uid)
                }
            }

            carItpEdit.setOnClickListener(){
                elementsUtil.setDate(context = itemView.context, car = carItpEdit)
            }

            carAssuranceEdit.setOnClickListener(){
                elementsUtil.setDate(context = itemView.context, car = carAssuranceEdit)
            }
        }

        fun editText() {
            carNumber.setVisibility(View.GONE)
            carType.setVisibility(View.GONE)
            carItp.setVisibility(View.GONE)
            carAssurance.setVisibility(View.GONE)
            carNumberEdit.setVisibility(View.VISIBLE)
            carTypeEdit.setVisibility(View.VISIBLE)
            carItpEdit.setVisibility(View.VISIBLE)
            carAssuranceEdit.setVisibility(View.VISIBLE)
            carNumberEdit.setText(carNumber.text)
            carTypeEdit.setText(carType.text)
            carItpEdit.setText(carItp.text)
            carAssuranceEdit.setText(carAssurance.text)
            edit_button.setVisibility(View.GONE)
            save_button.setVisibility(View.VISIBLE)
        }

        fun setData(): HashMap<String, String> {
            val data = hashMapOf(
                "number" to carNumberEdit.text.toString(),
                "type" to carTypeEdit.text.toString(),
                "itp" to carItpEdit.text.toString(),
                "assurance" to carAssuranceEdit.text.toString()
            )

            save_button.setVisibility(View.GONE)
            edit_button.setVisibility(View.VISIBLE)
            carNumber.setVisibility(View.VISIBLE)
            carType.setVisibility(View.VISIBLE)
            carItp.setVisibility(View.VISIBLE)
            carAssurance.setVisibility(View.VISIBLE)
            carNumberEdit.setVisibility(View.INVISIBLE)
            carTypeEdit.setVisibility(View.GONE)
            carItpEdit.setVisibility(View.INVISIBLE)
            carAssuranceEdit.setVisibility(View.GONE)

            return data
        }

        fun updateFirestore(
            dataSource: CollectionReference?,
            data: Map<String, String>,
            uid: String
        ) {
            dataSource?.document(uid)?.update(data)
                ?.addOnSuccessListener {
                    Log.d(
                        "CarsAdapter",
                        "DocumentSnapshot successfully written!"
                    )
                }
                ?.addOnFailureListener { e -> Log.w("CarsAdapter", "Error writing document", e) }
        }

        companion object {
            fun from(parent: ViewGroup, dataSource: CollectionReference?): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val dataSource = dataSource
                val view = layoutInflater
                    .inflate(R.layout.list_item_car, parent, false)
                return ViewHolder(view, dataSource)
            }
        }

    }
}