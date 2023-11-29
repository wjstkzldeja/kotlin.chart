package com.osl.base.project.main.views.calendar

import android.os.Bundle
import com.osl.base.project.main.R
import com.osl.base.project.main.databinding.FragmentCalendarBinding
import com.osl.base.project.main.views.*
import com.osl.base.project.main.views.calendar.adapter.CalendarDialogAdapter
import com.osl.base.project.main.views.container.main.ActMainViewModel
import com.osl.base.project.osl.views.OslFragment

class CalendarFragment : OslFragment<FragmentCalendarBinding, CalendarViewModel>() {
    override val layoutRes = R.layout.fragment_calendar
    override val destinationId = R.id.calendarFragment
    override val viewModelClass = CalendarViewModel::class.java
    override val actViewModelClass = ActMainViewModel::class.java

    override fun initViews(savedInstanceState: Bundle?) {
        ui.viewDataBinding.viewModel = viewModel
        initRecyclerView()
    }

    override fun addObservers() {
        viewModel.calendarListRefresh.observeEvent {
            ui.viewDataBinding.calendarDialogAdapter?.notifyDataSetChanged()
        }
    }

    private fun initRecyclerView() {
        ui.viewDataBinding.calendarDialogAdapter = CalendarDialogAdapter(viewModel)
    }
}