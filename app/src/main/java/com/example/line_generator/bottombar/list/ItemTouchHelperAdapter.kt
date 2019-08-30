package com.example.line_generator.bottombar.list

interface ItemTouchHelperAdapter {

    fun onItemMove(fromPosition: Int, toPosition: Int)

    fun onItemDismiss(position: Int)

    fun dragFinished(fromIndex: Int, toIndex: Int)

}