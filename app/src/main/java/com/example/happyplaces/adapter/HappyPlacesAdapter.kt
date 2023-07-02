package com.happyplaces.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.happyplaces.R
import com.example.happyplaces.activities.AddHappyPlaceActivtiy
import com.example.happyplaces.activities.MainActivity
import com.example.happyplaces.databinding.ActivityMainBinding
import com.example.happyplaces.databinding.ItemHappyPlaceBinding
import com.example.happyplaces.models.HappyPlaceModel


import com.happyplaces.database.DatabaseHandler


open class HappyPlacesAdapter(
    private val context: Context,
    private var list: ArrayList<HappyPlaceModel>
) : RecyclerView.Adapter<HappyPlacesAdapter.ViewHolder>() {
    private var onClickListener:OnClickListener? = null
    inner class ViewHolder(val binding: ItemHappyPlaceBinding) : RecyclerView.ViewHolder(binding.root)





    /**
     * Inflates the item views which is designed in xml layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {



        val binding = ItemHappyPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)

    }
    fun setOnClickListener(onClickListener:OnClickListener){
        this.onClickListener=onClickListener
    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]


        with(holder) {
            with(model) {


                binding.ivPlaceImage.setImageURI(Uri.parse(model.image))

                binding.tvTitle.text = model.title
                binding.tvDescription.text = model.description
                holder.itemView.setOnClickListener{
                    if(onClickListener != null){
                        onClickListener!!.onClick(position,model)
                    }
                }
            }


        }
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return list.size
    }
    interface OnClickListener{
        fun onClick(position: Int, model: HappyPlaceModel)
    }
    fun removeAt(position: Int){
        val dbHandler=DatabaseHandler(context)
        val isDeleted = dbHandler.deleteHappyPlace(list[position])
        if(isDeleted > 0){
            list.removeAt(position)
            notifyItemRemoved(position)
        }

    }




    fun notifyEditItem(activity: Activity,position: Int,requestCode:Int){
        val intent=Intent(context,AddHappyPlaceActivtiy::class.java)
        intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS,list[position])
        activity.startActivityForResult(intent,requestCode)
        notifyItemChanged(position)
    }



    /**
     * A function to edit the added happy place detail and pass the existing details through intent.
     */

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}