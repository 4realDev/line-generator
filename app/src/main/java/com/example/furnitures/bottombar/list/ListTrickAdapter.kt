package com.example.furnitures.bottombar.list

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.furnitures.trick.FurnitureViewState


class ListTrickAdapter(
    private var furnitureModificationListener: FurnitureModificationListener? = null,
    private var mDragStartListener: OnStartDragListener? = null
) : RecyclerView.Adapter<ListTrickAdapter.FurnitureHolder>(), ItemTouchHelperAdapter {

    private var draggingItem: FurnitureViewState? = null
    private var targetItem: FurnitureViewState? = null

    private val furnitureItemDiffer by lazy {
        AsyncListDiffer<FurnitureViewState>(
            // Überschreiben des ListUpdateCallbacks
            // Wofür ist Offset gut?
            this,
            furnitureCallback
        )
    }

    private val furnitureCallback = object : DiffUtil.ItemCallback<FurnitureViewState>() {
        override fun areItemsTheSame(oldItem: FurnitureViewState, newItem: FurnitureViewState): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FurnitureViewState, newItem: FurnitureViewState): Boolean {
            return oldItem == newItem
        }

        // getChangePayload wird aufgerufen wenn: areItemsTheSame = true | areContentsTheSame = false
        // getChangedPayload vergleich properties der ViewHolder und binded nur diese neu
        // Übergeben wird Any? / Irgendeine Property -> Diese wird an onBindViewHolder (Payload) weitergereicht
        override fun getChangePayload(oldItem: FurnitureViewState, newItem: FurnitureViewState): Any? {

            // Bundle -> container of key/value pairs
            // mapping from String values to various Parcelable types
            // implements an interface called Parcelable

            var diffBundle = Bundle()
            if (oldItem.position != newItem.position) {
                diffBundle.putInt(KEY_POSITION, newItem.position)
            }
            if (oldItem.name != newItem.name && newItem.name != null) {
                diffBundle.putInt(KEY_TITLE, newItem.name)
            }
            if (diffBundle.size() == 0) return null
            return diffBundle
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FurnitureHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(com.example.furnitures.R.layout.list_item_trick, parent, false)
        val viewHolder = FurnitureHolder(itemView)
        return viewHolder
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: FurnitureHolder, position: Int) {
        // Wenn der payload leer ist/ andere Elemente geändert wurden, als in payload abgefangen
        // dann bind den gesamten viewHolder neu
        val item = getFurnitureItem(position)
        val name = item?.name
        val userCreatedName = item?.userCreatedName

        if (userCreatedName != null)
            holder.title?.text = userCreatedName
        if (name != null)
            holder.title?.setText(name)

        holder.count?.text = item!!.position.toString() //String.format("%02d", position + 1)
        holder.handle?.setOnTouchListener { handle, event ->
            if (event.action == MotionEvent.ACTION_DOWN) mDragStartListener?.onStartDrag(holder)
            false
        }
    }

    // suppress warning - warning cause no optimation for blind people
    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: FurnitureHolder, position: Int, payloads: List<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        }
        // Wenn im payload was drinne ist
        // bind nur den payload neu
        else {
            val o = payloads[0] as Bundle
            for (key in o.keySet()) {
                if (key == KEY_POSITION) {
                    holder.count?.text = o.getInt(KEY_POSITION).toString()
                } else if (key == KEY_TITLE) {
                    holder.title?.text = o.getInt(KEY_TITLE).toString()
                }
            }
        }

        // WAS IST WENN IM payload ETWAS DRINNE IST + ANDERE ELEMENTE HABEN SICH VERÄNDERT
    }

    inner class FurnitureHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView? = itemView.findViewById(com.example.furnitures.R.id.list_item_furniture__title)
        val count: TextView? = itemView.findViewById(com.example.furnitures.R.id.list_item_furniture_count)
        val handle: ImageView? = itemView.findViewById(com.example.furnitures.R.id.list_item_furniture_handle)
    }

    // Anstatt als Class als Object anlegen
    // Direkte Implementierung des Interfaces bei Erstellung des Adapters


    private fun getFurnitureItem(position: Int): FurnitureViewState? {
        return furnitureItemDiffer.currentList.getOrNull(position)
    }

    fun onFurnitureItemsUpdate(newData: List<FurnitureViewState>) {
        // submitList schönere Version von notifyData
        // submitList überprüft, welche Elemente vertauscht wurden, und passt nur diese an
        // ruft die passenden notify methoden auf, ohne gesammte liste anzupassen
        furnitureItemDiffer.submitList(newData)
        // notifyDataSetChanged()
        // Notify any registered observers that data has changed
        // Forcing any observers to assume that all existing items and structure may no longer be valid
        // LayoutManagers will be forced to fully rebind and relayout all visible views
    }

    override fun getItemCount(): Int {
        return furnitureItemDiffer.currentList.size
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (draggingItem == null) draggingItem = getFurnitureItem(fromPosition)
        targetItem = getFurnitureItem(toPosition)

        // Update Adapter list
        val list = furnitureItemDiffer.currentList.toMutableList()
        val removed = list.removeAt(fromPosition)
        list.add(toPosition, removed)
        furnitureItemDiffer.submitList(list)
        Log.d(ListTrickAdapter::class.java.name.toString(), "toAdapterPosition: $toPosition fromAdapterPosition: $fromPosition")
    }

    override fun onItemDismiss(position: Int) {
        getFurnitureItem(position)?.let { furnitureModificationListener?.onItemRemove(it) }
    }

    override fun dragFinished(fromIndex: Int, toIndex: Int) {
        furnitureModificationListener?.onItemMove(fromIndex, toIndex)
        draggingItem = null
        targetItem = null
    }

    interface FurnitureModificationListener {
        fun onItemRemove(furnitureItem: FurnitureViewState)
        fun onItemMove(fromIndex: Int, toIndex: Int)
    }

    interface OnStartDragListener {
        fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
    }

    companion object {
        const val KEY_POSITION = "KEY_POSITION"
        const val KEY_TITLE = "KEY_NAME"
    }
}
