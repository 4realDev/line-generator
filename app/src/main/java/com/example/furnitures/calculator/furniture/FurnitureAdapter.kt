package com.example.furnitures.calculator.furniture

import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.furnitures.R

/**
 * Copyright (c) 2017 fluidmobile GmbH. All rights reserved.
 */
class FurnitureAdapter(private val furnitureClickListener: FurnitureClickListener) :  ListAdapter<FurnitureViewState, FurnitureAdapter.FurnitureHolder>(DiffCallback())  {

    private var furnitureList = emptyList<FurnitureViewState>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FurnitureHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_furniture, parent, false)
        val viewHolder = FurnitureHolder(itemView)
        viewHolder.itemView.setOnClickListener {
            val item = getItem(viewHolder.adapterPosition)
            furnitureClickListener.onFurnitureClicked(item)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: FurnitureHolder, position: Int) {
        val item = getItem(position)
        holder.image?.setImageResource(item.drawableResId)
        holder.title?.setText(item.name)
        val context = holder.itemView.context
        holder.image?.background = ContextCompat.getDrawable(context, if (item.isSelected) R.drawable.background_circle_red else R.drawable.background_furniture)
        holder.image?.drawable?.let {
            DrawableCompat.setTint(it, ContextCompat.getColor(context, if (item.isSelected) R.color.white else R.color.grey05))
        }
    }

    fun setData(furnitureList: List<FurnitureViewState>) {
        this.furnitureList = furnitureList
    }

    interface FurnitureClickListener {
        fun onFurnitureClicked(furniture: FurnitureViewState)
    }

    inner class FurnitureHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView? = itemView.findViewById(R.id.list_item_furniture__image)
        val title: TextView? = itemView.findViewById(R.id.list_item_furniture__title)
    }

    class DiffCallback: DiffUtil.ItemCallback<FurnitureViewState>(){
        override fun areItemsTheSame(oldItem: FurnitureViewState, newItem: FurnitureViewState): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FurnitureViewState, newItem: FurnitureViewState): Boolean {
            return oldItem == newItem
        }

    }
}
