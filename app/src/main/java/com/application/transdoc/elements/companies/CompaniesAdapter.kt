package com.application.transdoc.elements.companies

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
import com.application.transdoc.firestore.Company
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference


class CompaniesAdapter() : RecyclerView.Adapter<CompaniesAdapter.ViewHolder>() {

    var data = listOf<Company>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var dataSource: CollectionReference? = null

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        var uid = item.companyId
        uid?.let { holder.bind(item, it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, dataSource)
    }

    class ViewHolder private constructor(
        itemView: View,
        dataSource: CollectionReference?
    ) : RecyclerView.ViewHolder(itemView) {
        val dataSource = dataSource
        val edit_button: ImageView = itemView.findViewById(R.id.edit_button)
        val save_button: ImageView = itemView.findViewById(R.id.save_button)
        val companyName: TextView = itemView.findViewById(R.id.name)
        val companyContact: TextView = itemView.findViewById(R.id.contact)
        val companyNr: TextView = itemView.findViewById(R.id.nr)
        val companyCui: TextView = itemView.findViewById(R.id.cui)
        val companyContactEdit: EditText = itemView.findViewById(R.id.contact_edit)
        val companyNrEdit: EditText = itemView.findViewById(R.id.nr_edit)
        val companyCuiEdit: EditText = itemView.findViewById(R.id.cui_edit)
        val companyNameEdit: EditText = itemView.findViewById(R.id.name_edit)

        fun bind(item: Company, uid: String) {
            companyName.setText(item.name)
            companyContact.setText(item.contact)
            companyNr.setText(item.nr)
            companyCui.setText(item.cui)

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
            edit_button.setVisibility(View.GONE)
            save_button.setVisibility(View.VISIBLE)
            companyName.setVisibility(View.GONE)
            companyContact.setVisibility(View.GONE)
            companyNr.setVisibility(View.GONE)
            companyCui.setVisibility(View.GONE)
            companyNameEdit.setVisibility(View.VISIBLE)
            companyContactEdit.setVisibility(View.VISIBLE)
            companyNrEdit.setVisibility(View.VISIBLE)
            companyCuiEdit.setVisibility(View.VISIBLE)
            companyNameEdit.setText(companyName.text)
            companyContactEdit.setText(companyContact.text)
            companyNrEdit.setText(companyNr.text)
            companyCuiEdit.setText(companyCui.text)
        }

        fun setData(): HashMap<String, String> {
            val data = hashMapOf(
                "name" to companyNameEdit.text.toString(),
                "contact" to companyContactEdit.text.toString(),
                "nr" to companyNrEdit.text.toString(),
                "cui" to companyCuiEdit.text.toString()
            )

            save_button.setVisibility(View.GONE)
            edit_button.setVisibility(View.VISIBLE)
            companyName.setVisibility(View.VISIBLE)
            companyContact.setVisibility(View.VISIBLE)
            companyNr.setVisibility(View.VISIBLE)
            companyCui.setVisibility(View.VISIBLE)
            companyNameEdit.setVisibility(View.INVISIBLE)
            companyContactEdit.setVisibility(View.GONE)
            companyNrEdit.setVisibility(View.INVISIBLE)
            companyCuiEdit.setVisibility(View.GONE)

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
                        "CompaniesAdapter",
                        "DocumentSnapshot successfully written!"
                    )
                }
                ?.addOnFailureListener { e -> Log.w("CompaniesAdapter", "Error writing document", e) }
        }

        companion object {
            fun from(parent: ViewGroup, dataSource: CollectionReference?): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val dataSource = dataSource
                val view = layoutInflater
                    .inflate(R.layout.list_item_company, parent, false)
                return ViewHolder(view, dataSource)
            }
        }

    }
}