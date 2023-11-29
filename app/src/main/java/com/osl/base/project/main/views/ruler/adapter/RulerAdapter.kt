package com.osl.base.project.main.views.ruler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.osl.base.project.main.databinding.ItemRuleBinding
import com.osl.base.project.osl.views.DataBoundListAdapter
import com.osl.base.project.osl.BR

open class RulerAdapter : DataBoundListAdapter<Int>(
  object : DiffUtil.ItemCallback<Int>() {
    override fun areItemsTheSame(oldItem: Int, newItem: Int) = oldItem == newItem
    override fun areContentsTheSame(oldItem: Int, newItem: Int) = oldItem == newItem
  }
) {

  override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
    parent.clipChildren = false
    return ItemRuleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
  }

  override fun bind(binding: ViewDataBinding, item: Int, position: Int) {
    val isFifth = (item % 10 == 0)
    binding.setVariable(BR.isFifth, isFifth)
    when {
      item % 10 == 0 -> (item / 10).toString()
      isFifth -> String.format("%.1f", item / 10f)
      else -> ""
    }.let {
      binding.setVariable(BR.weightValue, it)
    }
  }
}