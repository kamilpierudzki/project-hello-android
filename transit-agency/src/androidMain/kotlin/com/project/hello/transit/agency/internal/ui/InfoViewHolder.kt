package com.project.hello.transit.agency.internal.ui

import androidx.recyclerview.widget.RecyclerView
import com.project.hello.transit.agency.databinding.InfoRowItemBinding

internal class InfoViewHolder(private val viewBinding: InfoRowItemBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {

    fun setupView(selectionListener: () -> Unit) {
        viewBinding.root.setOnClickListener {
            selectionListener.invoke()
        }
    }
}