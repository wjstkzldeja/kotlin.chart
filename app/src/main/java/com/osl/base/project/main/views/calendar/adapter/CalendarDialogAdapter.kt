package com.osl.base.project.main.views.calendar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import com.osl.base.project.main.databinding.ItemCalendarBinding
import com.osl.base.project.main.entity.CalendarDialogVo
import com.osl.base.project.main.views.calendar.CalendarViewModel
import com.osl.base.project.osl.views.DataBoundListAdapter
import com.osl.base.project.osl.BR

class CalendarDialogAdapter(
  private val viewModel: CalendarViewModel
) : DataBoundListAdapter<CalendarDialogVo>(
  object : DiffUtil.ItemCallback<CalendarDialogVo>() {
    override fun areItemsTheSame(oldItem: CalendarDialogVo, newItem: CalendarDialogVo) =
      oldItem.listIndex == newItem.listIndex

    override fun areContentsTheSame(oldItem: CalendarDialogVo, newItem: CalendarDialogVo) =
      oldItem.listIndex == newItem.listIndex
  }
) {
  override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
    return ItemCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false).apply {
      setVariable(BR.viewModel, this@CalendarDialogAdapter.viewModel)
    }
  }

  override fun bind(binding: ViewDataBinding, item: CalendarDialogVo, position: Int) {
    binding.setVariable(BR.item, item)
  }
}
