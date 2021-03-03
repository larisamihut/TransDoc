package com.application.transdoc.elements

import android.app.DatePickerDialog
import android.content.Context
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.*

class ElementsUtil {

    val myCalendar = Calendar.getInstance()

    fun setDate(context: Context, car: EditText){
        DatePickerDialog(
            context, getDate(car), myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun getDate(car: EditText) : DatePickerDialog.OnDateSetListener {
        return DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel(car)
        }
    }

    private fun updateLabel(car : EditText){
        val myFormat = "dd/MM/yy"
        val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
        car.setText(sdf.format(myCalendar.getTime()))
    }

}