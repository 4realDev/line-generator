package com.example.furnitures.calculator.bottombar.selection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.furnitures.R
import com.example.furnitures.calculator.trick.FurnitureViewState

/**
 * Copyright (c) 2017 fluidmobile GmbH. All rights reserved.
 */
class SelectionAdapter(private val furnitureClickListener: FurnitureClickListener) :  ListAdapter<FurnitureViewState, SelectionAdapter.FurnitureHolder>(DiffCallback())  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FurnitureHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_select_trick, parent, false)
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
        // image darf nur einmal vorkommen, sonst werden andere items getriggert
        holder.image?.background = ContextCompat.getDrawable(context, if (item.isSelected) R.drawable.background_circle_red else R.drawable.background_furniture)
        // instantiate multiple Drawable objects from same image resource, they change properties for all
        // mutate() -> mutable drawable not shares its state with any other drawable
        holder.image?.drawable?.mutate()?.let {
            DrawableCompat.setTint(it, ContextCompat.getColor(context, if (item.isSelected) R.color.white else R.color.trick_golden))
        }
    }

    interface FurnitureClickListener {
        fun onFurnitureClicked(furniture: FurnitureViewState)
    }

    inner class FurnitureHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
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
