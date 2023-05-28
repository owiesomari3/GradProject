package com.example.graduationproject.hungry

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.CacheManager
import com.example.graduationproject.R
import com.example.graduationproject.enums.UserType

class CustomAdapterFood(private val foodList: ArrayList<DataFood>) :
    RecyclerView.Adapter<CustomAdapterFood.ViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_home, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: DataFood = foodList[position]
        holder.apply {
            try {
                val decodedBytes = Base64.decode(data.image, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                myImage.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (CacheManager.getUserType() == UserType.CHEF) {
                rateTv.visibility = View.GONE
                foodNameTv.visibility = View.GONE
                priceTv.visibility = View.GONE

                rateEditText.visibility = View.VISIBLE
                foodNameEditText.visibility = View.VISIBLE
                priceEditText.visibility = View.VISIBLE
            }

            rateEditText.setText(data.rate.toString())
            foodNameEditText.setText(data.familiar_name)
            priceEditText.setText(data.price)
            rateTv.text = (data.rate.toString())
            foodNameTv.text = (data.familiar_name)
            priceTv.text = (data.price)
        }
    }

    override fun getItemCount() = foodList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var foodNameEditText: EditText
        var foodNameTv: TextView
        var priceEditText: EditText
        var priceTv: TextView
        var rateEditText: EditText
        var rateTv: TextView
        var myImage: ImageView

        init {
            foodNameEditText = itemView.findViewById(R.id.edit_familiar_name)
            foodNameTv = itemView.findViewById(R.id.tv_familiar_name)
            rateEditText = itemView.findViewById(R.id.edit_rate)
            rateTv = itemView.findViewById(R.id.tv_rate)
            priceEditText = itemView.findViewById(R.id.edit_price)
            priceTv = itemView.findViewById(R.id.tv_price)
            myImage = itemView.findViewById(R.id.image_food)

            itemView.setOnClickListener {
                // val myIntent = Intent(itemView.context, AfterSelectFood::class.java)
                // itemView.context.startActivities(arrayOf(myIntent))

            }
        }
    }

}

