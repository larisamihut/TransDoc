package com.application.transdoc.orders.pdf

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import com.application.transdoc.R
import com.application.transdoc.firestore.*
import com.application.transdoc.orders.activeOrders.Order
import com.application.transdoc.orders.newOrder.contact.Contact
import com.application.transdoc.orders.newOrder.document.DocumentRepository
import com.application.transdoc.orders.newOrder.loading.Loading
import com.itextpdf.text.*
import com.itextpdf.text.Element
import com.itextpdf.text.pdf.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class PdfGenerator(
    var documentId: String,
    var context: Context?
) {

    private val repository = DocumentRepository()

    companion object {
        val TAG = "PdfGenerator"
    }

    private fun createDocument(uid: String, context: Context?, pdfData: PdfData) {
        if (pdfData.counter.equals(6)) {
            val outputStream = ByteArrayOutputStream()
            val document = Document(PageSize.A4)

            try {
                PdfWriter.getInstance(document, outputStream)
            } catch (e: DocumentException) {
                e.printStackTrace()
            }

            val docWriter = PdfWriter.getInstance(document, outputStream)
            document.open()

            val cb: PdfContentByte = docWriter.directContent

            val drawable = context!!.resources.getDrawable(R.drawable.alumial_logo)
            val bitmapDrawable = drawable as BitmapDrawable
            val bitmap = bitmapDrawable.bitmap
            val drawablestream = ByteArrayOutputStream()
            bitmap.compress(
                CompressFormat.JPEG,
                100,
                drawablestream
            )

            //use the compression format of your need
            val inputStream = ByteArrayInputStream(drawablestream.toByteArray())
            val bmp = BitmapFactory.decodeStream(inputStream)
            val stream = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val companyLogo: Image = Image.getInstance(stream.toByteArray())
            companyLogo.setAbsolutePosition(50f, 610f)
            companyLogo.scalePercent(80f)
            document.add(companyLogo)

            val companyNameWidths = floatArrayOf(2f)
            val companyName = PdfPTable(companyNameWidths)
            companyName.totalWidth = 500f
            createNoBorderCell(companyName, "Alumial CARGO SRL", Font(boldFont(), 20f))
            companyName.writeSelectedRows(
                0,
                -1,
                document.leftMargin(),
                780f,
                docWriter.directContent
            )

            //customer data
           /* createHeadings(cb, 350f, 715f, "Contact Person", boldFont(), 8f);
            createHeadings(cb, 430f, 715f, "Company Name", simpleFont(), 8f);
            createHeadings(cb, 350f, 700f, "Mobile Phone", boldFont(), 8f);
            createHeadings(cb, 430f, 700f, "Company Name", simpleFont(), 8f);
            createHeadings(cb, 350f, 685f, "E-mail", boldFont(), 8f);
            createHeadings(cb, 430f, 685f, "Company Name", simpleFont(), 8f);
            createHeadings(cb, 350f, 670f, "Address", boldFont(), 8f);
            createHeadings(cb, 430f, 670f, "Company Name", simpleFont(), 8f);
            createHeadings(cb, 350f, 655f, "Bank", boldFont(), 8f);
            createHeadings(cb, 430f, 655f, "Company Name", simpleFont(), 8f);
            createHeadings(cb, 350f, 640f, "IBAN", boldFont(), 8f);
            createHeadings(cb, 430f, 640f, "Company Name", simpleFont(), 8f);*/

            val orderWidths = floatArrayOf(2f)
            val order = PdfPTable(orderWidths)
            order.totalWidth = 500f
            createNoBorderCell(
                order,
                "Order " + uid + " " + pdfData.orderDate,
                Font(boldFont(), 20f)
            )
            order.writeSelectedRows(0, -1, document.leftMargin(), 560f, docWriter.directContent)

            val columnWidths = floatArrayOf(2f, 2f)
            val table = PdfPTable(columnWidths)
            table.totalWidth = 500f

            createContentForCell(table, "Company", Font(boldFont(), 10f))
            createContentForCell(table, pdfData.client, Font(simpleFont(), 10f))

            createContentForCell(table, "Code", Font(boldFont(), 10f))
            createContentForCell(table, pdfData.clientCode, Font(simpleFont(), 10f))

            createContentForCell(table, "Contact Person", Font(boldFont(), 10f))
            createContentForCell(table, pdfData.clientContactPerson, Font(simpleFont(), 10f))

            createContentForCell(table, "E-mail", Font(boldFont(), 10f))
            createContentForCell(table, pdfData.clientEmail, Font(simpleFont(), 10f))

            createContentForCell(table, "Type of the car", Font(boldFont(), 10f))
            createContentForCell(table, pdfData.carType, Font(simpleFont(), 10f))

            createContentForCell(table, "Car registration number", Font(boldFont(), 10f))
            createContentForCell(table, pdfData.carNr, Font(simpleFont(), 10f))

            createContentForCell(table, "Name/ Phone driver", Font(boldFont(), 10f))
            createContentForCell(
                table,
                pdfData.drivername + " / " + pdfData.driverPhone,
                Font(simpleFont(), 10f)
            )


            createContentForCell(table, "Merchendise description", Font(boldFont(), 10f))
            createContentForCell(table, pdfData.goodsDescription, Font(simpleFont(), 10f))

            createContentForCell(table, "Merchendise weight", Font(boldFont(), 10f))
            createContentForCell(
                table,
                pdfData.goodsWeight + " " + pdfData.goodsUnit,
                Font(simpleFont(), 10f)
            )

            createContentForCell(table, "Cost of the transport", Font(boldFont(), 15f))
            createContentForCell(table, pdfData.cost + " EURO + TVA", Font(boldFont(), 15f))

            createCenterContentForCell(
                table,
                "Address for loading",
                Font(boldFont(), 10f, Font.BOLD, BaseColor.GREEN)
            )
            createCenterContentForCell(
                table,
                "Address for unloading",
                Font(boldFont(), 10f, Font.BOLD, BaseColor.RED)
            )

            createCenterContentForCell(
                table,
                pdfData.loadingAddress.toString(),
                Font(simpleFont(), 10f)
            )
            createCenterContentForCell(
                table,
                "Spain",
                Font(simpleFont(), 10f)
            )

            //   table.headerRows = 1
            table.writeSelectedRows(0, -1, document.leftMargin(), 525f, docWriter.directContent)

            createHeadings(cb, 400f, 150f, "I agree terms and conditions", simpleFont(), 10f);
            createHeadings(cb, 450f, 135f, pdfData.client.toString(), boldFont(), 10f);
            createHeadings(cb, 150f, 135f, "Alumial CARGO SRL", boldFont(), 10f);

            document.close()

            repository.savePdfToFireBaseStorage(uid, outputStream)
            repository.updateField(
                documentId,
                repository.createFieldDocument(uid, "pdfDoc")
            )
        }
    }

    fun retrieveDataForPdfFromDatabase(uid: String) {
        var pdfData = PdfData()
        repository.getSpecificDocument("documents", uid).get().addOnSuccessListener {
            val data = it.toObject(Order::class.java)
            if (data != null) {
                repository.getSpecificDocument("receivedOrder", data.receivedOrder.toString()).get()
                    .addOnSuccessListener {
                        val receivedOrder = it.toObject(ReceivedOrder::class.java)
                        if (receivedOrder != null) {
                            pdfData.client = receivedOrder.company.toString()
                            pdfData.orderDate = receivedOrder.date.toString()
                            pdfData.cost = receivedOrder.money.toString()
                            pdfData.counter = pdfData.counter.inc()
                            createDocument(documentId, context, pdfData)
                        }
                    }
                repository.getSpecificDocument("loading", data.loading.toString()).get()
                    .addOnSuccessListener {
                        val loading = it.toObject(Loading::class.java)
                        if (loading != null) {
                            pdfData.loadingAddress = loading.address.toString()
                            pdfData.loadingDate = loading.deadline.toString()
                            pdfData.counter = pdfData.counter.inc()
                            createDocument(documentId, context, pdfData)
                        }
                    }
                /*repository.getSpecificDocument("unloading", data.unloading.toString()).get()
                    .addOnSuccessListener {
                        val unloading = it.toObject(Loading::class.java)
                        if (unloading != null) {
                            pdfData.unloadingAddress = unloading.address.toString()
                            pdfData.unloadingDate = unloading.deadline.toString()
                            pdfData.counter = pdfData.counter.inc()
                            createDocument(documentId, context, pdfData)
                        }
                    }*/
                repository.getSpecificDocument("describedGoods", data.describedGoods.toString())
                    .get().addOnSuccessListener {
                        val describedGoods = it.toObject(DescribedOrder::class.java)
                        if (describedGoods != null) {
                            pdfData.goodsDescription = describedGoods.type.toString()
                            pdfData.goodsWeight = describedGoods.weight.toString()
                            pdfData.goodsUnit = describedGoods.unit.toString()
                            pdfData.counter = pdfData.counter.inc()
                            createDocument(documentId, context, pdfData)
                        }
                    }
                repository.getSpecificDocument("contact", data.contact.toString()).get()
                    .addOnSuccessListener {
                        val contact = it.toObject(Contact::class.java)
                        if (contact != null) {
                            repository.getSpecificDocument("cars", contact.car.toString())
                                .get().addOnSuccessListener {
                                    val car = it.toObject(Car::class.java)
                                    if (car != null) {
                                        pdfData.carNr = car.number.toString()
                                        pdfData.carType = car.type.toString()
                                        pdfData.counter = pdfData.counter.inc()
                                        createDocument(documentId, context, pdfData)
                                    }
                                }
                            repository.getSpecificDocument(
                                "companies",
                                contact.company.toString()
                            )
                                .get().addOnSuccessListener {
                                    val company = it.toObject(Company::class.java)
                                    if (company != null) {
                                        pdfData.client = company.name.toString()
                                        pdfData.clientCode = company.cui.toString()
                                        pdfData.clientContactPerson = company.contact.toString()
                                        pdfData.clientEmail = company.nr.toString()
                                        pdfData.counter = pdfData.counter.inc()
                                        createDocument(documentId, context, pdfData)
                                    }
                                }
                            repository.getSpecificDocument("drivers", contact.driver.toString())
                                .get().addOnSuccessListener {
                                    val driver = it.toObject(Driver::class.java)
                                    if (driver != null) {
                                        pdfData.driverPhone = driver.phone.toString()
                                        pdfData.drivername = driver.name.toString()
                                        pdfData.counter = pdfData.counter.inc()
                                        createDocument(documentId, context, pdfData)
                                    }
                                }
                        }
                    }
            }
        }
    }

    fun createContentForCell(table: PdfPTable, field: String?, font: Font) {
        var cell = PdfPCell(Phrase(field, font))
        //    cell.horizontalAlignment = Element.ALIGN_LEFT
        table.addCell(cell)
    }

    fun createCenterContentForCell(table: PdfPTable, field: String, font: Font) {
        var cell = PdfPCell(Phrase(field, font))
        cell.horizontalAlignment = Element.ALIGN_CENTER
        table.addCell(cell)
    }

    fun createNoBorderCell(table: PdfPTable, field: String, font: Font) {
        var cell = PdfPCell(Phrase(field, font))
        cell.horizontalAlignment = Element.ALIGN_CENTER
        cell.border = Rectangle.NO_BORDER
        table.addCell(cell)
    }

    private fun createHeadings(
        cb: PdfContentByte,
        x: Float,
        y: Float,
        text: String,
        font: BaseFont?,
        size: Float
    ) {
        cb.beginText()
        cb.setFontAndSize(font, size)
        cb.setTextMatrix(x, y)
        cb.showText(text.trim { it <= ' ' })
        cb.endText()
    }

    private fun boldFont(): BaseFont {
        return BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED)
    }

    private fun simpleFont(): BaseFont {
        return BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED)
    }
}