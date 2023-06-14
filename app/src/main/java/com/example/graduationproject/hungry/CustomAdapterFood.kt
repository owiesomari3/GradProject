package com.example.graduationproject.hungry

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.text.Html
import android.text.Layout
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.contains
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.*
import com.example.graduationproject.chef.OffersChefsFragment

class CustomAdapterFood(
    private val foodList: ArrayList<DataFood>,
    private val callback: ItemClickInterface,
    private val screenSource: String,
    private val  activity:AppCompatActivity ?=null

) : RecyclerView.Adapter<CustomAdapterFood.ViewHolder>() {

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

            if (data.offerPrice == "0" && screenSource == "chef") {
                addOffer.visibility = View.VISIBLE
                addOffer.setOnClickListener {
                    showDialogForOffer(itemView.context, data)
                }
            }


            if (screenSource == "offers_chef") {
                addOffer.visibility = View.VISIBLE
                addOffer.text = addOffer.context.getString(R.string.remove_from_offers)
                addOffer.setOnClickListener {
                    val allFoods = Storage.getAllFoods(addOffer.context)
                    if (allFoods != null) {
                        for (i in 0 until allFoods.length()) {
                            val offerObject = allFoods.getJSONObject(i)
                            val foodId = offerObject.getString(Constants.FOOD_ID)
                            if (foodId == data.foodId) {
                                offerObject.put(Constants.OFFER_PRICE, "0")
                                data.offerPrice = "0"
                            }
                        }

                        Storage.saveAllFoodsList(addOffer.context, allFoods)
                        Util.showToastMsg(
                            addOffer.context,
                            "Your offer has been deleted successfully"
                        )

                        replaceFragment(OffersChefsFragment())
                    }
                }
            }

            if (screenSource == "offers_chef" || screenSource == "offers_hungry") {
                priceEditText.setText(data.offerPrice)
                priceTv.text = (data.offerPrice)
            } else {
                priceEditText.setText(data.price)
                priceTv.text = (data.price)
            }
            foodNameTv.text = (data.familiar_name)

            container.setOnClickListener {
                callback.onItemClick(data)
            }

            if (screenSource == "offers_hungry") {
                offerPriceContainer.visibility = View.VISIBLE
                offerPriceValue.text = data.offerPrice
                val delPrice = "<del>Price:</del>"
                priceTitle.text = Html.fromHtml(delPrice, Html.FROM_HTML_MODE_COMPACT)
                val delPriceValue = "<del>${data.price}</del>"
                priceTv.text = Html.fromHtml(delPriceValue, Html.FROM_HTML_MODE_COMPACT)
            }
        }

    }

    @SuppressLint("InflateParams")
    private fun showDialogForOffer(context: Context, data: DataFood) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialogoffers, null)
        val alertDialogBuilder = AlertDialog.Builder(context).apply {
            setView(dialogView)
            setCancelable(false)
        }
        val alertDialog = alertDialogBuilder.create()
        dialogView.apply {
            val offerBtn = findViewById<Button>(R.id.ok_Button)
            val cancelBtn = findViewById<Button>(R.id.cancel_Button)
            val offerPrice = findViewById<EditText>(R.id.offer_price)
            alertDialog.apply {
                offerBtn?.setOnClickListener {
                    dismiss()
                    val allFoods = Storage.getAllFoods(context)
                    if (allFoods != null) {
                        for (i in 0 until allFoods.length()) {
                            val offerObject = allFoods.getJSONObject(i)
                            val foodId = offerObject.getString(Constants.FOOD_ID)
                            if (foodId == data.foodId) {
                                offerObject.put(Constants.OFFER_PRICE, offerPrice.text.toString())
                                data.offerPrice = offerPrice.text.toString()
                            }
                        }
                        Storage.saveAllFoodsList(context, allFoods)
                    }
                }
                cancelBtn?.setOnClickListener { dismiss() }
            }.show()
        }
    }

    override fun getItemCount() = foodList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var foodNameEditText: EditText
        var foodNameTv: TextView
        var priceEditText: EditText
        var priceTv: TextView
        var myImage: ImageView
        var container: CardView
        var addOffer: Button
        var offerPriceContainer: View
        var offerPriceTitle: TextView
        var offerPriceValue: TextView
        var priceTitle: TextView

        init {
            foodNameEditText = itemView.findViewById(R.id.edit_familiar_name)
            foodNameTv = itemView.findViewById(R.id.tv_familiar_name)
            priceEditText = itemView.findViewById(R.id.edit_price)
            priceTv = itemView.findViewById(R.id.tv_price)
            myImage = itemView.findViewById(R.id.image_food)
            container = itemView.findViewById(R.id.container_home)
            addOffer = itemView.findViewById(R.id.add_offer)
            offerPriceContainer = itemView.findViewById(R.id.offer_price_container)
            offerPriceTitle = itemView.findViewById(R.id.offer_price_title)
            offerPriceValue = itemView.findViewById(R.id.offer_price_value)
            priceTitle = itemView.findViewById(R.id.price_title)




        }

        fun replaceFragment(fragment: Fragment) {
            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction: FragmentTransaction? = fragmentManager?.beginTransaction()
            fragmentTransaction?.add(R.id.frame_layout_chef, fragment)
            fragmentTransaction?.commit()
        }
    }

    interface ItemClickInterface {
        fun onItemClick(data: DataFood)
    }
}

