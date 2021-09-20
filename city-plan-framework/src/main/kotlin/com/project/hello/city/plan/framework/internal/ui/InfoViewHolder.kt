package com.project.hello.city.plan.framework.internal.ui

import androidx.recyclerview.widget.RecyclerView
import com.project.hello.city.plan.framework.databinding.InfoRowItemBinding

internal class InfoViewHolder(private val viewBinding: InfoRowItemBinding) :
    RecyclerView.ViewHolder(viewBinding.root) {

    fun setupView(selectionListener: () -> Unit) {
        viewBinding.root.setOnClickListener {
            selectionListener.invoke()
        }
    }
}