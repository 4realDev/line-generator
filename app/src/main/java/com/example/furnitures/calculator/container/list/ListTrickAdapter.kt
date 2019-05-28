package com.example.furnitures.calculator.container.selection

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.furnitures.R
import com.example.furnitures.calculator.trick.FurnitureViewState

/**
 * Copyright (c) 2017 fluidmobile GmbH. All rights reserved.
 */
class ListTrickAdapter :  ListAdapter<FurnitureViewState, ListTrickAdapter.FurnitureHolder>(DiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FurnitureHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_trick, parent, false)
        val viewHolder = FurnitureHolder(itemView)
        return viewHolder
    }

    override fun onBindViewHolder(holder: FurnitureHolder, position: Int) {
        val item = getItem(position)
        holder.title?.setText(item.name)
        holder.count?.text = String.format("%02d", position + 1)
    }

    inner class FurnitureHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView? = itemView.findViewById(R.id.list_item_furniture__title)
        val count: TextView? = itemView.findViewById(R.id.list_item_furniture_count)
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
