package com.example.graduationproject.hungry

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.R

class CustomAdapterFood(private val foodList: ArrayList<DataFood>):RecyclerView.Adapter<CustomAdapterFood.ViewHolder>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v=LayoutInflater.from(parent.context).inflate(R.layout.recycler_home,null,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
       return foodList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data :DataFood=foodList[position]
        holder.txtDescription.text=data.description
        holder.txtNameFood.text=data.familiar_name
        holder.myImage.setImageResource(data.image)
        holder.txtPrice.text= data.price.toString()
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener{
            val myIntent=Intent(itemView.context,AfterSelectFood::class.java)
            itemView.context.startActivities(arrayOf(myIntent))
            }
        }
        val txtNameFood = itemView.findViewById(R.id.the_familiar_name) as TextView
        val txtDescription = itemView.findViewById(R.id.the_description) as TextView
        val txtPrice = itemView.findViewById(R.id.price) as TextView
        val myImage = itemView.findViewById(R.id.image_food) as ImageView
    }

}