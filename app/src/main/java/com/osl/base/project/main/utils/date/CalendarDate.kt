package com.osl.base.project.main.utils.date

import timber.log.Timber.Forest.d
import java.text.SimpleDateFormat
import java.util.*
fun getCalendarTimestamp(date: Long): String {
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).format(date)
}

/**현재 날짜 */
fun getCalendarTodayDate(): Long {
    return GregorianCalendar.getInstance().let {
        it.timeInMillis
    }
}

/** 현재 월 가져오기*/
fun getMonth(): String {
    val cal = GregorianCalendar.getInstance()
    return (cal.get(Calendar.MONTH) + 1).toString()
}

/** 현재 월 첫 일*/
fun firstDay(): Int {
    return GregorianCalendar.getInstance().let {
        it.set(Calendar.DATE, 1)
        it.get(Calendar.DAY_OF_WEEK)
    }
}

/** 현재 월 마지막 일*/
fun lastDay(): Int {
    return GregorianCalendar.getInstance().let {
        it.getActualMaximum(Calendar.DAY_OF_MONTH)
    }
}

/** 현재 일*/
fun currentDay(): Int {
    return GregorianCalendar.getInstance().let {
        it.get(Calendar.DAY_OF_MONTH)
    }
}

/** 현재 요일*/
fun currentDayOfWeek(): Int {
    return GregorianCalendar.getInstance().let {
        it.get(Calendar.DAY_OF_WEEK)
    }
}

/** 현재 주의 금요일*/
fun isWeekFriday(): Int {
    return GregorianCalendar.getInstance().let {
        it.add(Calendar.DAY_OF_MONTH, (6 - it.get(Calendar.DAY_OF_WEEK)))
        it.get(Calendar.DAY_OF_MONTH)
    }
}

/** 다음주의 금요일*/
fun isNextDayOfWeek(): Int {
    return GregorianCalendar.getInstance().let {
        it.add(Calendar.WEEK_OF_YEAR, +1)
        it.add(Calendar.DAY_OF_MONTH, (6 - it.get(Calendar.DAY_OF_WEEK)))
        it.get(Calendar.DAY_OF_MONTH)
    }
}

fun isWeekOrNextWeekFriday(): Int {
    return when {
        currentDay() < isWeekFriday() -> {
            isWeekFriday()
        }
        else -> {
            isNextDayOfWeek()
        }
    }
}

/** 2개월 마지막일 */
fun getTwoMonthEnd(): String {
    val twoMonth = GregorianCalendar.getInstance().let {
        it.add(Calendar.MONTH, +2)
        it[Calendar.DATE] = (it.getActualMaximum(Calendar.DATE))
        it[Calendar.HOUR_OF_DAY] = 23
        it[Calendar.MINUTE] = 59
        it[Calendar.SECOND] = 59
        it[Calendar.MILLISECOND] = 999
        it.timeInMillis
    }
    d("calendar getCalendarDialogTimestamp : ${getCalendarDialogTimestamp(twoMonth)}")
    return getCalendarDialogTimestamp(twoMonth)
}

fun getCalendarDialogTimestamp(date: Long): String {
    return SimpleDateFormat("yyyy.MM.dd", Locale.KOREA).format(date)
}

/** 선택한 일*/
fun selectDay(selectDay: Int?): Long {
    val selectedDay = GregorianCalendar.getInstance().let {
        it.set(Calendar.DATE, selectDay ?: 1)
        it.timeInMillis
    }
    d("calendar getTimestamp : ${ChartDateUtils().getTimestamp(selectedDay)}")
    return selectedDay
}

fun getHomeTodayFormat(): String {
    return SimpleDateFormat("MM.dd (E)", Locale.KOREA).format(ChartDateUtils().getTodayDate())
}

fun getMoveSelectedButtonFormat(date: Long): String {
    return SimpleDateFormat("MM월dd일 선택하기", Locale.KOREA).format(date)
}

fun getMovingOneFormat(date: Long): String {
    return SimpleDateFormat("MM월dd일 E요일", Locale.KOREA).format(date)
}

fun dayOfTheWeek(): String {
    return when (currentDayOfWeek()) {
        1 -> {
            "일"
        }
        2 -> {
            "월"
        }
        3 -> {
            "화"
        }
        4 -> {
            "수"
        }
        5 -> {
            "목"
        }
        6 -> {
            "금"
        }
        7 -> {
            "토"
        }
        else -> {
            "일"
        }
    }
}
