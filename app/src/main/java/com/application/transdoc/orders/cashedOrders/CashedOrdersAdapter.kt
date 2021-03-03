package com.application.transdoc.elements.drivers

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.application.transdoc.R
import com.application.transdoc.firestore.Database
import com.application.transdoc.firestore.ReceivedOrder
import com.application.transdoc.orders.activeOrders.Order
import com.application.transdoc.orders.activeOrders.OrdersRepository
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView


class CashedOrdersAdapter() : RecyclerView.Adapter<CashedOrdersAdapter.ViewHolder>() {

    var data = listOf<Order>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var dataSource: CollectionReference? = null
    var repository =
        OrdersRepository()

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, dataSource, repository)
    }

    class ViewHolder private constructor(
        itemView: View,
        val dataSource: CollectionReference?,
        val repository: OrdersRepository,
    ) : RecyclerView.ViewHolder(itemView) {
        val code: TextView = itemView.findViewById(R.id.code)
        val client: TextView = itemView.findViewById(R.id.client)
        val pdf: CircleImageView = itemView.findViewById(R.id.pdf)

        fun bind(item: Order) {
            code.setText(item.orderId)
            item.receivedOrder?.let { setAdapterField(it, client, "receivedOrder") }

            pdf.setOnClickListener() {
                openPdf(item)
            }
        }

        fun setAdapterField(documentId: String, textView: TextView, collectionPath: String) {
            val dataSource = OrdersRepository()
                .getSpecificDocument(collectionPath, documentId)
            dataSource.get().addOnSuccessListener {
                val data = it.toObject(ReceivedOrder::class.java)
                val value = data?.company
                textView.setText(value)
            }
        }

        private fun openPdf(item: Order){
            val storageReference: StorageReference = FirebaseStorage.getInstance()
                .getReferenceFromUrl("gs://transdoc-98601.appspot.com/pdfDocs/"+ Database().getCurrentUserID()+"/")
                .child(item.orderId.toString()+".pdf")
            storageReference.downloadUrl.addOnSuccessListener(object : OnSuccessListener<Uri> {
                override fun onSuccess(uri: Uri?) {
                    val i = Intent(Intent.ACTION_VIEW)
                    i.setData(Uri.parse(uri.toString()))
                    itemView.context.startActivity(i)
                }
            })
        }

        companion object {
            fun from(
                parent: ViewGroup,
                dataSource: CollectionReference?,
                repository: OrdersRepository,

                ): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val dataSource = dataSource
                val repository = repository
                val view = layoutInflater
                    .inflate(R.layout.list_item_cashed, parent, false)
                return ViewHolder(view, dataSource, repository)
            }
        }

    }
}

