package com.example.mitraartapp

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import java.time.format.DateTimeFormatter

class ECPAdapter(private val mECPs: MutableList<ECP>) : RecyclerView.Adapter<ECPAdapter.ViewHolder>()
{
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val ECPtypeTextView = itemView.findViewById<TextView>(R.id.typeECPTextView)
        val ECPnameTextView = itemView.findViewById<TextView>(R.id.nameECPTextView)
        val ECPdirectorTextView = itemView.findViewById<TextView>(R.id.directorECPTextView)
        val ECPactsTextView = itemView.findViewById<TextView>(R.id.actsECPTextView)
    }

    // ... constructor and member variables
    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ECPAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.item_ecp, parent, false)
        // Return a new holder instance
        return ViewHolder(contactView)
    }

    // Involves populating data into the item through holder
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(viewHolder: ECPAdapter.ViewHolder, position: Int) {
        // Get the data model based on position
        val ecp: ECP = mECPs.get(position)
        // Set item views based on your views and data model
        val typeTextView = viewHolder.ECPtypeTextView
        typeTextView.setText(ecp.type)
        val nameTextView = viewHolder.ECPnameTextView
        nameTextView.setText(ecp.name)
        val directorTextView = viewHolder.ECPdirectorTextView
        if (ecp.director != null) {
            directorTextView.visibility = View.VISIBLE
            directorTextView.setText(ecp.director)
        }
        else directorTextView.visibility = View.INVISIBLE
        val actsTextView = viewHolder.ECPactsTextView
        /*val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        var dateFormat = ecp.activationStart.toString()
        val formatted1 = dateFormat.format(formatter)
        dateFormat = ecp.activationEnd.toString()
        val formatted2 = dateFormat.format(formatter)*/
        actsTextView.setText("Действительно с " + ecp.activationStart.toString() + " по " + ecp.activationEnd.toString())
    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return mECPs.size
    }

    fun removeItem(pos: Int){
        mECPs.removeAt(pos)
        notifyItemRemoved(pos)
        notifyItemRangeChanged(pos, mECPs.size)
    }

    fun restoreItem(ecp: ECP, pos: Int) {
        mECPs.add(pos, ecp)
        notifyItemInserted(pos)
    }
}