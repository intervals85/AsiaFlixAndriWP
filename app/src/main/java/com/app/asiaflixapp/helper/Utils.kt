package com.app.asiaflixapp.helper

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.ColorRes
import com.app.asiaflixapp.databinding.TagItemBinding
import com.bumptech.glide.Glide
import com.veinhorn.tagview.TagView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object Utils {

    val base_url = "https://watchasian.la/"
    fun setSystemBarColor(act: Activity, @ColorRes color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = act.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.setStatusBarColor(act.resources.getColor(color))
        }
    }
    val dateyyyyMMddHHmmss: String
        get() {
            val simpleDate = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
            val cal = Calendar.getInstance()
            val current = cal.time
            return simpleDate.format(current)
        }
    val dateyyyyMMdd: String
        get() {
            val simpleDate = SimpleDateFormat("yyyy-MM-dd")
            val cal = Calendar.getInstance()
            val current = cal.time
            return simpleDate.format(current)
        }
    val dateddMMyyyy: String
        get() {
            val simpleDate = SimpleDateFormat("ddMMyyyy")
            val cal = Calendar.getInstance()
            val current = cal.time
            return simpleDate.format(current)
        }
    val dateddMMyy: String
        get() {
            val simpleDate = SimpleDateFormat("ddMMyy")
            val cal = Calendar.getInstance()
            val current = cal.time
            return simpleDate.format(current)
        }
    val hour: String
        get() {
            val simpleDate = SimpleDateFormat("HH:mm:ss")
            val cal = Calendar.getInstance()
            val current = cal.time
            return simpleDate.format(current)
        }
    val hours: String
        get() {
            val simpleDate = SimpleDateFormat("HHmmss")
            val cal = Calendar.getInstance()
            val current = cal.time
            return simpleDate.format(current)
        }
    val datehour: String
        get() {
            val simpleDateFormat = SimpleDateFormat("yyyyMMddHHmmss")
            val cal = Calendar.getInstance()
            val current = cal.time
            return simpleDateFormat.format(current)
        }

    val month: String
        get() {
            val simpleDateFormat = SimpleDateFormat("MM")
            val cal = Calendar.getInstance()
            val current = cal.time
            return simpleDateFormat.format(current)
        }

    val year: String
        get() {
            val simpleDateFormat = SimpleDateFormat("yyyy")
            val cal = Calendar.getInstance()
            val current = cal.time
            return simpleDateFormat.format(current)
        }

    fun changeDateToYYYYmmddHHmmssToddMMYYYY(value: String):String{
        val sf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        try {
            val date = sf.parse(value)

            val dateFormater = SimpleDateFormat("dd-MM-yyyy")
            return dateFormater.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return ""

    }

    fun convertToyyyMMdd(date: Long): String? {
        val dateFormater = SimpleDateFormat("yyyy-MM-dd")
        return dateFormater.format(date)
    }
    fun toast(context: Context?, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    fun loadImage(context: Context, url :String, image:ImageView) {
        Glide.with(context).load(url).into(image)
    }


    fun showDateDialog(context: Context, edtDate: EditText)  {
        val calendar: Calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(   context,
            { view, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val dateLongInput = calendar.timeInMillis
                val value  = convertToyyyMMdd(dateLongInput).toString()
                edtDate.setText(value)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    fun tagView( text: String, layoutInflater: LayoutInflater): FrameLayout {
        val binding = TagItemBinding.inflate(layoutInflater)
        binding.tagView.text= text
        return  binding.root

    }


}