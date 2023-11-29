package com.osl.base.project.main.utils.date

import java.text.SimpleDateFormat
import java.util.*

class ChartDateUtils {
    fun getTimestamp(date: Long): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA).format(date)
    }

    /** 00AM~24PM 텍스트 포맷*/
    fun chartGetTimestamp(date: Long): String {
        return SimpleDateFormat("H a", Locale.ENGLISH).format(date)
    }

    /** 00:00AM~24:00PM 텍스트 포맷*/
    fun getHHMMaGetTimestamp(date: Long): String {
        return SimpleDateFormat("HH:mm a", Locale.ENGLISH).format(date)
    }

    /** 일~토 텍스트 포맷*/
    fun chartGetTimestampDayOfWeek(date: Long): String {
//        return SimpleDateFormat("dd(EE)", Locale.KOREA).format(date)
        return SimpleDateFormat("EE", Locale.KOREA).format(date)
    }

    fun chartGetTimestampTimeMin(date: Long): String {
        return SimpleDateFormat("HH:mm", Locale.KOREA).format(date)
    }

    /**현재 날짜 */
    fun getTodayDate(): Long {
        return GregorianCalendar.getInstance().timeInMillis
    }

    /** 지정한 날짜로 0시,0분,0초 timeInMillis 만드는 함수*/
    fun getZeroSelectTimeToSecMin(time: Long): Long {
        return GregorianCalendar.getInstance().apply {
            timeInMillis = time
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }.timeInMillis
    }

    /** 지정한 날짜로 0분,0초 timeInMillis 만드는 함수
     * time 값 없으면 현재 날짜로*/
    fun getZeroSecMinDate(time: Long?): Long {
        return GregorianCalendar.getInstance().apply {
            timeInMillis = time ?: getTodayDate()
            set(Calendar.HOUR_OF_DAY,0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }.timeInMillis
    }

    fun getMaxDate(time: Long?): Long {
        return GregorianCalendar.getInstance().apply {
            timeInMillis = time ?: getTodayDate()
            set(Calendar.HOUR_OF_DAY,23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND,999)
        }.timeInMillis
    }

    /** 지정한 날짜로 N시,0분,0초 timeInMillis 만드는 함수
     * time 값 없으면 현재 날짜로*/
    fun getToHourZeroSecMinDate(time: Long?, hour: Int): Long {
        return GregorianCalendar.getInstance().apply {
            timeInMillis = time ?: getTodayDate()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }.timeInMillis
    }

    /** 지정한 날짜로 N시,0분,0초/ 몇일전 timeInMillis 만드는 함수 테스트용
     * DateDiff 이전 날짜로 만드는거*/
    fun getToHourZeroSecMinDateTwo(time: Long?, hour: Int, DateDiff:Int): Long {
        return GregorianCalendar.getInstance().apply {
            timeInMillis = time ?: getTodayDate()
            add(Calendar.DATE, DateDiff)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }.timeInMillis
    }

    /** 현재 주의 금요일*/
    fun isWeekFriday(): Int {
        return GregorianCalendar.getInstance().let {
            it.add(Calendar.DAY_OF_MONTH, (6 - it.get(Calendar.DAY_OF_WEEK)))
            it.get(Calendar.DAY_OF_MONTH)
        }
    }

    /** 현재 날짜 기준 일~토 구하는 함수
     * dayOfWeek 1(일)~7(토)*/
    fun getCurrentDayOfWeek(dayOfWeek: Int): Long {
        return GregorianCalendar.getInstance().apply {
            timeInMillis = getTodayDate()
            add(Calendar.DAY_OF_MONTH, (dayOfWeek - get(Calendar.DAY_OF_WEEK)))
            get(Calendar.DAY_OF_MONTH)
        }.timeInMillis
    }

    /** 현재 요일*/
    fun currentDayOfWeek(): Int {
        return GregorianCalendar.getInstance().get(Calendar.DAY_OF_WEEK)
    }
}

