package com.example.line_generator.bottombar.list

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
import com.example.line_generator.R.id
import com.example.line_generator.R.layout
import com.example.line_generator.data.trick.TrickViewState


class ListTrickAdapter(
    private var trickModificationListener: TrickModificationListener? = null,
    private var mDragStartListener: OnStartDragListener? = null
) : RecyclerView.Adapter<ListTrickAdapter.TrickHolder>(), ItemTouchHelperAdapter {

    companion object {
        const val KEY_POSITION = "KEY_POSITION"
    }

    private var draggingItem: TrickViewState? = null
    private var targetItem: TrickViewState? = null

    private val trickItemDiffer by lazy {
        AsyncListDiffer<TrickViewState>(
            // Überschreiben des ListUpdateCallbacks
            // Wofür ist Offset gut?
            this,
            trickCallback
        )
    }

    private val trickCallback = object : DiffUtil.ItemCallback<TrickViewState>() {
        override fun areItemsTheSame(oldItem: TrickViewState, newItem: TrickViewState): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TrickViewState, newItem: TrickViewState): Boolean {
            return oldItem == newItem
        }

        // getChangePayload wird aufgerufen wenn: areItemsTheSame = true | areContentsTheSame = false
        // getChangedPayload vergleich properties der ViewHolder und binded nur diese neu
        // Übergeben wird Any? / Irgendeine Property -> Diese wird an onBindViewHolder (Payload) weitergereicht
        override fun getChangePayload(oldItem: TrickViewState, newItem: TrickViewState): Any? {

            // Bundle -> container of key/value pairs
            // mapping from String values to various Parcelable types
            // implements an interface called Parcelable

            var diffBundle = Bundle()
            if (oldItem.position != newItem.position) {
                diffBundle.putInt(KEY_POSITION, newItem.position)
            }
            if (diffBundle.size() == 0) return null
            return diffBundle
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrickHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(layout.list_item_trick, parent, false)
        val viewHolder = TrickHolder(itemView)
        return viewHolder
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: TrickHolder, position: Int) {
        // Wenn der payload leer ist/ andere Elemente geändert wurden, als in payload abgefangen
        // dann bind den gesamten viewHolder neu
        val item = getTrickItem(position)
        val name = item?.name
        val userCreatedName = item?.userCreatedName
        val difficulty = item?.difficulty.toString().toLowerCase().capitalize()
        val direction =
            "${item?.directionIn?.name?.toLowerCase()?.capitalize()}" +
            " to " +
            "${item?.directionOut?.name?.drop(3)?.toLowerCase()?.capitalize()}"


        if (userCreatedName != null)
            holder.title?.text = userCreatedName
        if (name != null)
            holder.title?.setText(name)

        holder.subTitleDifficulty?.text = difficulty
        holder.subTitleDirection?.text = direction

        holder.count?.text = item!!.position.toString() //String.format("%02d", position + 1)
        holder.handle?.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) mDragStartListener?.onStartDrag(holder)
            false
        }
    }

    // suppress warning - warning cause no optimation for blind people
    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: TrickHolder, position: Int, payloads: List<Any>) {
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
                }
            }
        }

        // WAS IST WENN IM payload ETWAS DRINNE IST + ANDERE ELEMENTE HABEN SICH VERÄNDERT
    }

    inner class TrickHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView? = itemView.findViewById(id.list_item_trick__title)
        val subTitleDifficulty: TextView? = itemView.findViewById(id.list_item_trick__difficulty)
        val subTitleDirection: TextView? = itemView.findViewById(id.list_item_trick__direction)
        val count: TextView? = itemView.findViewById(id.list_item_trick__count)
        val handle: ImageView? = itemView.findViewById(id.list_item_trick__handle)
    }

    // Anstatt als Class als Object anlegen
    // Direkte Implementierung des Interfaces bei Erstellung des Adapters


    private fun getTrickItem(position: Int): TrickViewState? {
        return trickItemDiffer.currentList.getOrNull(position)
    }

    fun onTrickItemsUpdate(newData: List<TrickViewState>) {
        // submitList schönere Version von notifyData
        // submitList überprüft, welche Elemente vertauscht wurden, und passt nur diese an
        // ruft die passenden notify methoden auf, ohne gesammte liste anzupassen
        trickItemDiffer.submitList(newData)
        // notifyDataSetChanged()
        // Notify any registered observers that data has changed
        // Forcing any observers to assume that all existing items and structure may no longer be valid
        // LayoutManagers will be forced to fully rebind and relayout all visible views
    }

    override fun getItemCount(): Int {
        return trickItemDiffer.currentList.size
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (draggingItem == null) draggingItem = getTrickItem(fromPosition)
        targetItem = getTrickItem(toPosition)

        // Update Adapter list
        val list = trickItemDiffer.currentList.toMutableList()
        val removed = list.removeAt(fromPosition)
        list.add(toPosition, removed)
        trickItemDiffer.submitList(list)
        Log.d(ListTrickAdapter::class.java.name.toString(), "toAdapterPosition: $toPosition fromAdapterPosition: $fromPosition")
    }

    override fun onItemDismiss(position: Int) {
        getTrickItem(position)?.let { trickModificationListener?.onItemRemove(it) }
    }

    override fun dragFinished(fromIndex: Int, toIndex: Int) {
        trickModificationListener?.onItemMove(fromIndex, toIndex)
        draggingItem = null
        targetItem = null
    }

    interface TrickModificationListener {
        fun onItemRemove(trickItem: TrickViewState)
        fun onItemMove(fromIndex: Int, toIndex: Int)
    }

    interface OnStartDragListener {
        fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
    }
}
