package com.example.mitraartapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mitraartapp.databinding.ItemLotBinding
import androidx.core.content.ContextCompat
import com.example.mitraartapp.R
import com.example.mitraartapp.Lot

class LotAdapter (private val mLots: List<Lot>) : RecyclerView.Adapter<LotAdapter.ViewHolder>()
{
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val lotPriceTextView = itemView.findViewById<TextView>(R.id.lotPriceTextView)
        val lotNameTextView = itemView.findViewById<TextView>(R.id.lotNameTextView)
        val lotAuthorTextView = itemView.findViewById<TextView>(R.id.lotAuthorTextView)
        val lotImageView = itemView.findViewById<ImageView>(R.id.lotImageView)
    }

    // ... constructor and member variables
    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LotAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.item_lot, parent, false)
        // Return a new holder instance
        return ViewHolder(contactView)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(viewHolder: LotAdapter.ViewHolder, position: Int) {
        // Get the data model based on position
        val lot: Lot = mLots.get(position)
        // Set item views based on your views and data model
        val priceTextView = viewHolder.lotPriceTextView
        priceTextView.setText(lot.price.toString())
        val nameTextView = viewHolder.lotNameTextView
        nameTextView.setText(lot.name)
        val authorTextView = viewHolder.lotAuthorTextView
        authorTextView.setText(lot.author)
        val imageView = viewHolder.lotImageView
        imageView.setImageResource(lot.imageRes)
    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return mLots.size
    }
}


/*class LotAdapter /*(private val mLots: List<Lot>)*/ : RecyclerView.Adapter<LotAdapter.LotViewHolder>()
{

    inner class LotViewHolder(val binding: ItemLotBinding): RecyclerView.ViewHolder(binding.root)
        /*val lotPriceTextView = itemView.findViewById<TextView>(R.id.lot_price)
        val lotNameTextView = itemView.findViewById<TextView>(R.id.lot_name)
        val lotAuthorTextView = itemView.findViewById<TextView>(R.id.lot_author)
        val lotImageView = itemView.findViewById<ImageView>(R.id.lot_image)*/

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LotViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLotBinding.inflate(inflater, parent, false)
        return LotViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LotViewHolder, position: Int) {
        // Get the data model based on position
        val lot: Lot = data[position]
        val context = holder.itemView.context

        with(holder.binding){
            lotPriceTextView.text = lot.price.toString()
            lotNameTextView.text = lot.name
            lotAuthorTextView.text = lot.author
            lotImageView.setImageResource(lot.imageRes)
        }
        // Set item views based on your views and data model
        /*val priceTextView = holder.lotPriceTextView
        priceTextView.setText(lot.price.toString())
        val nameTextView = holder.lotNameTextView
        nameTextView.setText(lot.name)
        val authorTextView = holder.lotAuthorTextView
        authorTextView.setText(lot.author)
        val imageView = holder.lotImageView
        imageView.setImageResource(lot.imageRes)*/

    }



    /*// Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val lotPriceTextView = itemView.findViewById<TextView>(R.id.lot_price)
        val lotNameTextView = itemView.findViewById<TextView>(R.id.lot_name)
        val lotAuthorTextView = itemView.findViewById<TextView>(R.id.lot_author)
        val lotImageView = itemView.findViewById<ImageView>(R.id.lot_image)
    }

    // ... constructor and member variables
    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LotAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val binding = ItemLotBinding.inflate(inflater, parent, false)
        // Inflate the custom layout
        val lotView = inflater.inflate(R.layout.item_lot, parent, false)
        // Return a new holder instance
        return ViewHolder(lotView)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(viewHolder: LotAdapter.ViewHolder, position: Int) {
        // Get the data model based on position
        val lot: Lot = mLots.get(position)
        // Set item views based on your views and data model
        val priceTextView = viewHolder.lotPriceTextView
        priceTextView.setText(lot.price.toString())
        val nameTextView = viewHolder.lotNameTextView
        priceTextView.setText(lot.name)
        val authorTextView = viewHolder.lotAuthorTextView
        priceTextView.setText(lot.author)
        val imageView = viewHolder.lotImageView
        imageView.setImageResource(lot.imageRes)

    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return mLots.size
    }*/
}*/
