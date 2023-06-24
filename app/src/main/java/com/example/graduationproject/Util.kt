package com.example.graduationproject

import android.content.Context
import android.widget.Toast
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

object Util {

    fun currencyFormat(amount: String?): String {
        val currencyCode = "JOD"
        val pattern = "#,##0.000"
        val dec = DecimalFormat(pattern, DecimalFormatSymbols(Locale.US))
        val value = if (amount?.isEmpty() == true) "" else dec.format(amount?.toDouble())
        val symbol = Currency.getInstance(currencyCode).currencyCode
        return "$value $symbol"
    }

    fun showToastMsg(context: Context, msg: Any) {
        when (msg) {
            is String -> Toast.makeText(context, msg, Toast.LENGTH_LONG).show()

            is Int -> Toast.makeText(context, context.getText(msg), Toast.LENGTH_LONG).show()
        }
    }
}