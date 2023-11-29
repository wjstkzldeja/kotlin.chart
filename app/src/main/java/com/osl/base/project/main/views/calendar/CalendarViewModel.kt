package com.osl.base.project.main.views.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.osl.base.project.main.entity.CalendarDialogVo
import com.osl.base.project.main.utils.date.*
import com.osl.base.project.main.views.*
import com.osl.base.project.osl.utils.Event
import com.osl.base.project.osl.views.OslViewModel
import timber.log.Timber.Forest.d

class CalendarViewModel(
) : OslViewModel() {
    private val _calendarItem = MutableLiveData<List<CalendarDialogVo>>(calendarDialogList())
    val calendarItem: LiveData<List<CalendarDialogVo>> get() = _calendarItem

    private val _calendarListRefresh = MutableLiveData<Event<Unit>>()
    val calendarListRefresh: LiveData<Event<Unit>> get() = _calendarListRefresh

    /** 달력 만드는 함수, 월별로 선택하는거면 나중에 수정이 필요
     * 1. 현재 월 첫일, 마지막일 값 가져와서 리스트 만든다
     * 2. (firstDay()-1) = 인덱스 번호라 -1을 해준다
     * 3. 0~firstDay() 까지는 null 입력
     * 4. 0~lastDay() 까지 값입력
     * */
    fun calendarDialogList(): ArrayList<CalendarDialogVo> {
        d("calendar test getMonth : ${getMonth()}")
        d("calendar test firstDay : ${firstDay()}")
        d("calendar test lastDay : ${lastDay()}")
        d("calendar test currentDay : ${currentDay()}")
        d("calendar test currentDayOfWeek : ${currentDayOfWeek()}")
        d("calendar test isWeekFriday : ${isWeekFriday()}")
        d("calendar test currentDayOfWeekTest : ${isNextDayOfWeek()}")
        val calendarList = ArrayList<CalendarDialogVo>()
        (0 until (firstDay() - 1)).forEach { i ->
            calendarList.add(CalendarDialogVo(listIndex = null))
        }
        (0 until lastDay()).forEach { i ->
            calendarList.add(CalendarDialogVo(listIndex = (i + 1).toString()))
        }
        return calendarList
    }

    fun onSelectDay(vo: CalendarDialogVo?) {
        d("calendar day : ${vo}")
        if(vo?.listIndex==null) return
        val selectedDay = selectDay((vo.listIndex)?.toInt())
        _calendarItem.value?.forEach {
            it.isSelected = false
        }
        _calendarItem.value?.find { it.listIndex == vo.listIndex }?.isSelected = (!(vo.isSelected?:false))
        _calendarListRefresh.value = Event(Unit)
    }
}