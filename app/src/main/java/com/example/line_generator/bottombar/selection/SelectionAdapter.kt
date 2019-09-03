package com.example.line_generator.bottombar.selection

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
import com.example.line_generator.R
import com.example.line_generator.extensions.pxFromDp
import com.example.line_generator.data.trick.HeaderViewState
import com.example.line_generator.data.trick.RowViewState
import com.example.line_generator.data.trick.TrickViewState

class SelectionAdapter(private val trickClickListener: TrickClickListener) : ListAdapter<RowViewState, SelectionAdapter.ViewHolder>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item is TrickViewState) R.layout.list_item_select_trick
        else R.layout.list_item_select_trick_header
    }

    fun isHeader(position: Int): Boolean {
        val item = getItem(position)
        return when (item) {
            is HeaderViewState -> true
            is TrickViewState -> false
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
                holder.bind(item as TrickViewState)
                holder.itemView.setOnClickListener {
                    trickClickListener.onTrickClicked(item)
                }
                holder.itemView.setOnLongClickListener {
                    trickClickListener.onTrickLongPressed(item)
                    true
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
            val image: ImageView? = itemView.findViewById(R.id.list_item_trick__image)
            val title: TextView? = itemView.findViewById(R.id.list_item_trick__title)

            fun bind(item: TrickViewState) {
                val trickContext: Context = itemView.context
                val name = item.name
                val userCreatedName = item.userCreatedName
                if (userCreatedName != null)
                    title?.text = userCreatedName
                else if(name != null)
                    title?.setText(name)
                image?.setImageResource(item.drawableResId)
                image?.background = getDrawable(itemView.context, R.drawable.background_trick_stroked_cicle)

                // Verhindern, das alle Tricks mit selber Image Resource gleichzeitig getriggert werden
                // instantiate multiple Drawable objects from same image resource change properties for all
                // mutate() -> mutable drawable not shares its state with any other drawable
                if (item.selected) {
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
            val headerTitle: TextView? = itemView.findViewById(R.id.list_item_trick__header_title)

            fun bind(item: HeaderViewState) {
                val headerContext: Context = itemView.context
                val headerView: View = itemView
                headerTitle?.text = item.category.toString()
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

    interface TrickClickListener {
        fun onTrickClicked(trickViewState: TrickViewState)
        fun onTrickLongPressed(trickViewState: TrickViewState)
    }

    class DiffCallback : DiffUtil.ItemCallback<RowViewState>() {
        // Gleiche Objekte?
        override fun areItemsTheSame(oldItem: RowViewState, newItem: RowViewState): Boolean {
            return oldItem.id == newItem.id
        }

        // Gleicher Inhalt?
        @SuppressLint("DiffUtilEquals") // ignore unnecessary error
        override fun areContentsTheSame(oldItem: RowViewState, newItem: RowViewState): Boolean {
            return if (oldItem is TrickViewState && newItem is TrickViewState)
                oldItem == newItem
            else if (oldItem is HeaderViewState && newItem is HeaderViewState)
                oldItem == newItem
            else false
        }
    }
}
