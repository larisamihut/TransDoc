package com.application.transdoc.elements.drivers

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.application.transdoc.R
import com.application.transdoc.firestore.Database
import com.application.transdoc.firestore.ReceivedOrder
import com.application.transdoc.orders.activeOrders.Order
import com.application.transdoc.orders.activeOrders.OrdersRepository
import com.application.transdoc.orders.activeOrders.OrdersViewModel
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView

class OrdersAdapter(var view: View) : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {

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
        return ViewHolder.from(parent, dataSource, repository, view)
    }

    class ViewHolder private constructor(
        itemView: View,
        val dataSource: CollectionReference?,
        val repository: OrdersRepository,
        fragmentView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val fragmentView = fragmentView
        val code: TextView = itemView.findViewById(R.id.code)
        val client: TextView = itemView.findViewById(R.id.client)
        val pdf: CircleImageView = itemView.findViewById(R.id.pdf)
        val close_button: ImageView = itemView.findViewById(R.id.edit_button)
        val cashed_button: ImageView = itemView.findViewById(R.id.cashed_button)

        fun bind(item: Order) {
            code.setText(item.orderId)
            item.receivedOrder?.let { setAdapterField(it, client, "receivedOrder") }

            close_button.setOnClickListener() {
                deleteOrder("documents", code.text.toString())
                showSnackBar("The order was deleted")
            }

            cashed_button.setOnClickListener() {
                moveDocumentToAnotherCollection(code.text.toString(), "cashed")
                showSnackBar("The order is complete")
            }

            pdf.setOnClickListener() {
                openPdf(item)
            }
        }

        private fun openPdf(item: Order){
            val storageReference: StorageReference = FirebaseStorage.getInstance()
                .getReferenceFromUrl("gs://transdoc-98601.appspot.com/pdfDocs/"+ Database().getCurrentUserID()+"/")
                .child(item.orderId.toString()+".pdf")
            storageReference.downloadUrl.addOnSuccessListener(object : OnSuccessListener<Uri>{
                override fun onSuccess(uri: Uri?) {
                    val i = Intent(Intent.ACTION_VIEW)
                    i.setData(Uri.parse(uri.toString()))
                    itemView.context.startActivity(i)
                }
            })
        }

        fun showSnackBar(message: String){
            Snackbar.make(
                fragmentView,
                message,
                Snackbar.LENGTH_LONG
            ).show()
        }

        fun deleteOrder(collectionPath: String, documentId: String) {
            repository.deleteDocument(collectionPath, documentId)
        }

        fun moveDocumentToAnotherCollection(documentId: String, collectionPath: String) {
            val viewModel =
                OrdersViewModel()
            viewModel.repository = repository
            val dataSource = repository.getSpecificDocument("documents", documentId)
            dataSource.get().addOnSuccessListener {
                val data = it.toObject(Order::class.java)
                if (data != null) {
                    viewModel.addData(
                        data.contact.toString(),
                        data.describedGoods.toString(),
                        data.loading.toString(),
                        data.unloading.toString(),
                        data.receivedOrder.toString(),
                        data.orderId.toString(),
                        collectionPath
                    )
                }
                deleteOrder("documents", code.text.toString())
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

        companion object {
            fun from(
                parent: ViewGroup,
                dataSource: CollectionReference?,
                repository: OrdersRepository,
                fragmentView: View
            ): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val fragmentView = fragmentView
                val dataSource = dataSource
                val repository = repository
                val view = layoutInflater
                    .inflate(R.layout.list_item_order, parent, false)
                return ViewHolder(view, dataSource, repository, fragmentView)
            }
        }

    }
}

