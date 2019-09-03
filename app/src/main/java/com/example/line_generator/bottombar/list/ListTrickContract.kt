package com.example.line_generator.bottombar.list

import androidx.lifecycle.LiveData
import com.example.line_generator.data.trick.TrickViewState

class ListTrickContract {
    interface ViewModel {
        fun areTricksSelected(): Boolean
        fun getSelectedItems(): LiveData<List<TrickViewState>>
        fun removeTrickItem(trickViewState: TrickViewState)
        fun changeTrickItemPosition(fromPosition: Int, toPosition: Int)
    }
}