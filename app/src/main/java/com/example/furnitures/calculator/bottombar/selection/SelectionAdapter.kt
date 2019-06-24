package com.example.furnitures.calculator.bottombar.selection

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.furnitures.calculator.extensions.pxFromDp
import com.example.furnitures.calculator.trick.FurnitureViewState
import com.example.furnitures.calculator.trick.HeaderViewState
import com.example.furnitures.calculator.trick.RowViewState


// Add Headers between Trick Views
// Basic Idea: associate each layout with a separate class (HeaderRowType, TrickRowType)
// Connect all via common interface (RowType{ HEADER_ROW_TYPE, TRICK_ROW_TYPE })

class SelectionAdapter(private val furnitureClickListener: FurnitureClickListener) : ListAdapter<RowViewState, SelectionAdapter.ViewHolder>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item is FurnitureViewState) com.example.furnitures.R.layout.list_item_select_trick
        else com.example.furnitures.R.layout.list_item_select_trick_header
    }

    fun isHeader(position: Int): Boolean{
        val item = getItem(position)
        return when(item){
            is HeaderViewState -> true
            is FurnitureViewState -> false
            else -> throw IllegalStateException("RowType doesn't exist $item")
        }
    }

    // viewType bereits gegeben?
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(viewType, parent, false)
        return when (viewType) {
            com.example.furnitures.R.layout.list_item_select_trick -> ViewHolder.Trick(view)
            com.example.furnitures.R.layout.list_item_select_trick_header -> ViewHolder.Header(view)
            else -> throw IllegalArgumentException("Unknown viewType ($viewType)")
        }
    }

    // holder wird von onCreateViewHolder geliefert
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder.Trick -> {
                val item = getItem(position)
                holder.bind(item as FurnitureViewState)
                holder.itemView.setOnClickListener {
                    furnitureClickListener.onFurnitureClicked(item)
                }
            }

            is ViewHolder.Header -> {
                val item = getItem(position) as HeaderViewState
                holder.headerTitle?.text = item.furnitureCategory.toString()

                // ItemView wird im RecyclerView immer wieder verwendet! d.h. muss man je nach View die Eigenschaft wieder Entfernen
                if(item.id == "FIRST_HEADER_ID" && position == 0){
                    val headerView: View = holder.itemView
                    val headerContext: Context = holder.itemView.context
                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    params.setMargins(0, 0, 0, pxFromDp(headerContext, 26f).toInt())
                    headerView.layoutParams = params
                }
                else{
                    val headerView: View = holder.itemView
                    val headerContext: Context = holder.itemView.context
                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    params.setMargins(0, pxFromDp(headerContext, 26f).toInt(), 0, pxFromDp(headerContext, 26f).toInt())
                    headerView.layoutParams = params
                }
            }
        }
    }

    // erbt von RecyclerView.ViewHolder
    // sealed class ViewHolder welche die weiteren ViewHolder der einzelnen RecyclerView Items beinhaltet
    // weitere ViewHolder erben von ViewHolder
    // binding erst in onBindViewHolder um item zu übergeben
    // getItem nur über onBindViewHolder ansprechbar

    sealed class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        class Trick(itemView: View) : ViewHolder(itemView) {
            val image: ImageView? = itemView.findViewById(com.example.furnitures.R.id.list_item_furniture__image)
            val title: TextView? = itemView.findViewById(com.example.furnitures.R.id.list_item_furniture__title)

            fun bind(item: FurnitureViewState) {
                val name = item.name
                val userCreatedName = item.userCreatedName
                if (userCreatedName != null)
                    title?.text = userCreatedName
                if (name != null)
                    title?.setText(name)
                title?.setTextColor(if (item.isSelected) getColor(itemView.context, com.example.furnitures.R.color.white) else getColor(itemView.context, com.example.furnitures.R.color.colorSecondary))

                image?.setImageResource(item.drawableResId)
                // var drawable = image?.background
                // image darf nur einmal vorkommen, sonst werden andere items getriggert
                // image?.background = drawable
                image?.background = getDrawable(itemView.context, com.example.furnitures.R.drawable.background_furniture)

                image?.background?.setTint(if (item.isSelected) getColor(itemView.context, com.example.furnitures.R.color.colorAccent) else getColor(itemView.context, com.example.furnitures.R.color.colorSecondary))
                // instantiate multiple Drawable objects from same image resource, they change properties for all
                // mutate() -> mutable drawable not shares its state with any other drawable
                image?.drawable?.mutate()?.let { DrawableCompat.setTint(it, ContextCompat.getColor(itemView.context, if (item.isSelected) com.example.furnitures.R.color.colorAccent else com.example.furnitures.R.color.colorSecondary)) }
            }
        }

        class Header(itemView: View) : ViewHolder(itemView) {
            val headerTitle: TextView? = itemView.findViewById(com.example.furnitures.R.id.list_item_furniture__header_title)
        }
    }

    interface FurnitureClickListener {
        fun onFurnitureClicked(furniture: FurnitureViewState)
    }

    class DiffCallback : DiffUtil.ItemCallback<RowViewState>() {
        // Gleiche Objekte?
        override fun areItemsTheSame(oldItem: RowViewState, newItem: RowViewState): Boolean {
            return oldItem.id == newItem.id
        }

        // Gleicher Inhalt?
        override fun areContentsTheSame(oldItem: RowViewState, newItem: RowViewState): Boolean {
            return if(oldItem is FurnitureViewState && newItem is FurnitureViewState)
                oldItem as FurnitureViewState == newItem as FurnitureViewState
            else if(oldItem is HeaderViewState && newItem is HeaderViewState)
                oldItem as HeaderViewState == newItem as HeaderViewState
            else false
        }
    }
}
