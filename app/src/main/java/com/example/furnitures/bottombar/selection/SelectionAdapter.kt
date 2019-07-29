package com.example.furnitures.bottombar.selection

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.furnitures.R
import com.example.furnitures.extensions.pxFromDp
import com.example.furnitures.trick.FurnitureViewState
import com.example.furnitures.trick.HeaderViewState
import com.example.furnitures.trick.RowViewState

class SelectionAdapter(private val furnitureClickListener: FurnitureClickListener) : ListAdapter<RowViewState, SelectionAdapter.ViewHolder>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item is FurnitureViewState) R.layout.list_item_select_trick
        else R.layout.list_item_select_trick_header
    }

    fun isHeader(position: Int): Boolean {
        val item = getItem(position)
        return when (item) {
            is HeaderViewState -> true
            is FurnitureViewState -> false
            else -> throw IllegalStateException("RowType doesn't exist $item")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.list_item_select_trick -> ViewHolder.Trick(view)
            R.layout.list_item_select_trick_header -> ViewHolder.Header(view)
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
                val item = getItem(position)
                holder.bind(item as HeaderViewState)
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
            val image: ImageView? = itemView.findViewById(R.id.list_item_furniture__image)
            val title: TextView? = itemView.findViewById(R.id.list_item_furniture__title)

            fun bind(item: FurnitureViewState) {
                val trickContext: Context = itemView.context
                val name = item.name
                val userCreatedName = item.userCreatedName
                if (userCreatedName != null)
                    title?.text = userCreatedName
                if (name != null)
                    title?.setText(name)
                image?.setImageResource(item.drawableResId)
                image?.background = getDrawable(itemView.context, R.drawable.background_trick_stroked_cicle)

                // Verhindern, das alle Furnitures mit selber Image Resource gleichzeitig getriggert werden
                // instantiate multiple Drawable objects from same image resource change properties for all
                // mutate() -> mutable drawable not shares its state with any other drawable
                if (item.isSelected) {
                    title?.setTextColor(getColor(trickContext, R.color.white))
                    image?.background?.setTint(getColor(trickContext, R.color.colorAccent))
                    image?.drawable?.mutate().let { DrawableCompat.setTint(it!!, getColor(trickContext, R.color.colorAccent)) }
                } else {
                    title?.setTextColor(getColor(trickContext, R.color.colorSecondary))
                    image?.background?.setTint(getColor(trickContext, R.color.colorSecondary))
                    image?.drawable?.mutate().let { DrawableCompat.setTint(it!!, getColor(trickContext, R.color.colorSecondary)) }
                }
            }
        }

        class Header(itemView: View) : ViewHolder(itemView) {
            val headerTitle: TextView? = itemView.findViewById(R.id.list_item_furniture__header_title)

            fun bind(item: HeaderViewState) {
                val headerContext: Context = itemView.context
                val headerView: View = itemView
                headerTitle?.text = item.furnitureCategory.toString()
//                headerTitle?.setTextColor(if (item.selected) getColor(headerContext, R.color.white) else getColor(headerContext, R.color.colorSecondary))
                val headerParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                // ItemView wird im RecyclerView immer wieder verwendet! d.h. muss man je nach View die Eigenschaft wieder Entfernen
                if (item.id == "FIRST_HEADER_ID") {
                    headerParams.setMargins(0, 0, 0, pxFromDp(headerContext, 26f).toInt())
                    headerView.layoutParams = headerParams
                } else {
                    headerParams.setMargins(0, pxFromDp(headerContext, 26f).toInt(), 0, pxFromDp(headerContext, 26f).toInt())
                    headerView.layoutParams = headerParams
                }
            }
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
        @SuppressLint("DiffUtilEquals") // ignore unnecessary error
        override fun areContentsTheSame(oldItem: RowViewState, newItem: RowViewState): Boolean {
            return if (oldItem is FurnitureViewState && newItem is FurnitureViewState)
                oldItem == newItem
            else if (oldItem is HeaderViewState && newItem is HeaderViewState)
                oldItem == newItem
            else false
        }
    }
}
