package com.osl.base.project.main.utils.chart

import com.osl.base.project.main.utils.date.ChartDateUtils
import timber.log.Timber.Forest.d

class ChartEngineUtils {

    /** 공용 사용 가능
     *  y축 Axis 리스트
     * yAxisMin : 최소
     * yAxisMax : 최대
     * valueGap : 간격 10단위,20단위...50단위*/
    fun createYAxisList(yAxisMin: Int, yAxisMax: Int, rangeStep: Int): ArrayList<Int> {
        val yAxisRange = IntRange(yAxisMin, yAxisMax).step(rangeStep)
//        d("MockData createYAxisList yAxisRange : ${yAxisRange}")
        return yAxisRange.toList() as ArrayList
    }

    /** 라인차트 x축 Axis 리스트
     * 현재 시간 기준 00~24시 리스트 만들기*/
    fun createXAxisList(): ArrayList<Long> {
        val xValueHourList = arrayListOf<Long>()
        /*for (hourIndex in 0..23) {
            xValueHourList.add(ChartDateUtils().getToHourZeroSecMinDateTwo(time = null, hour = hourIndex, DateDiff = -2))
        }*/
/*        for (hourIndex in 0..23) {
            xValueHourList.add(ChartDateUtils().getToHourZeroSecMinDateTwo(time = null, hour = hourIndex, DateDiff = -1))
        }*/
        for (hourIndex in 0..23) {
            xValueHourList.add(ChartDateUtils().getToHourZeroSecMinDateTwo(time = null, hour = hourIndex, DateDiff = 0))
        }
        d("MockData createXAxisList xValueHourList : ${xValueHourList}")
        d("MockData createXAxisList xValueHourList.size : ${xValueHourList.size}")
        d("MockData createXAxisList xValueHourList : ${xValueHourList.map { ChartDateUtils().getTimestamp(it) }}")
        return xValueHourList
    }

    /** @@@@@@@@@@@@@@@@@@@@라인차트*/
    /**x축 value 테스트용*/
    fun createLineChartXValueList(): ArrayList<Long> {
        val xValueHourList = arrayListOf<Long>()
        /*for (hourIndex in 0..23) {
            xValueHourList.add(ChartDateUtils().getToHourZeroSecMinDateTwo(time = null, hour = hourIndex, DateDiff = -2))
        }*/
        /*for (hourIndex in 0..23) {
            xValueHourList.add(ChartDateUtils().getToHourZeroSecMinDateTwo(time = null, hour = hourIndex, DateDiff = -1))
        }*/
        for (hourIndex in 0..23) {
            xValueHourList.add(ChartDateUtils().getToHourZeroSecMinDateTwo(time = null, hour = hourIndex, DateDiff = 0))
        }
        xValueHourList.map {
            d("MockData createXValueList xValueHourList : ${ChartDateUtils().getTimestamp(it)}")

        }

        return xValueHourList
    }

    /**y축 value 테스트용
     * yMinValue 테스트용 일괄 더해주기위해*/
    fun createLineChartYValueList(testSumValue: Int): ArrayList<Int> {
        /** xValue에 맞춰 0~24 * 2리스트*/

        val yValues = arrayListOf<Int>(
            10, 100, 10, 100, 10, 100, 10, 100, 10, 100,
            0, 90, 20, 90, 20, 90, 20, 90, 20, 90,
            0, 100, 10, 200
     /*       0, 100, 10, 100, 10, 100, 10, 100, 10, 100,
            0, 90, 20, 90, 20, 90, 20, 90, 20, 90,
            0, 100, 10, 200,
            0, 100, 10, 100, 10, 100, 10, 100, 10, 100,
            0, 90, 20, 90, 20, 90, 20, 90, 20, 90,
            0, 100, 10, 200*/
        )
        /*val yValues = arrayListOf<Int>(
            100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
            100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
            100, 100, 100, 200,
            100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
            100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
            100, 100, 100, 200
        )*/
 /*     val yValues = arrayListOf<Int>()
        val range = (0..300)
        for (i in 0..(23*1)){
            yValues.add(range.random())
        }*/
        val testList = yValues.map { it + testSumValue }
        d("MockData createYValueList testList : ${testList.size}")
        d("MockData createYValueList yValues : ${yValues}")
        return testList as ArrayList<Int>
    }

    /** @@@@@@@@@@@@@@@@@@@@막대차트*/
    /** 막대 x축 Axis 리스트
     * 현재 시간 기준 일~토 시 리스트 만들기*/
    fun createBarChartXAxisList(): ArrayList<Long> {
        val xValueHourList = arrayListOf<Long>()
        for (hourIndex in 1..7) {
//            d("MockData isWeekFridayTest : ${ChartDateUtils().getCurrentDayOfWeek(hourIndex)}")
            xValueHourList.add(ChartDateUtils().getCurrentDayOfWeek(hourIndex))
        }
        d("MockData createXAxisList xValueHourList : ${xValueHourList}")
        return xValueHourList
    }

    /**x축 value 테스트용*/
    fun createBarChartXValueList(): ArrayList<Long> {
        val xValueHourList = arrayListOf<Long>()
        for (hourIndex in 0..6) {
//            d("MockData createHourOfDay getZeroSecMinDate : ${ChartDateUtils().getZeroSecMinDate(time)}")
//            d("MockData createHourOfDay getTimestamp : ${ChartDateUtils().getTimestamp(getZeroSecMinDate(time))}")
            xValueHourList.add(ChartDateUtils().getToHourZeroSecMinDate(time = null, hour = hourIndex))
        }
        d("MockData createXValueList xValueHourList : ${xValueHourList}")
        return xValueHourList
    }

    /**y축 value 테스트용
     * yMinValue 테스트용 일괄 더해주기위해*/
    fun createBarChartYValueList(testSumValue: Int): ArrayList<Int> {
        /** xValue에 맞춰 일~토 리스트*/
        val yValues = arrayListOf<Int>(
            500, 1000, 1500, 1000, 2000, 800, 1200
        )
        val testList = yValues.map { it + testSumValue }
        d("MockData createYValueList testList : ${testList}")
        d("MockData createYValueList yValues : ${yValues}")
        return testList as ArrayList<Int>
    }


    /** @@@@@@@@@@@@@@@@@@@@포인트 차트*/
    /** 포인트 차트 x축 Axis 리스트
     * 현재 시간 기준 일~토 시 리스트 만들기*/
    fun createPointChartXAxisList(): ArrayList<Long> {
        val xValueHourList = arrayListOf<Long>()
        for (hourIndex in 1..7) {
//            d("MockData isWeekFridayTest : ${ChartDateUtils().getCurrentDayOfWeek(hourIndex)}")
            xValueHourList.add(ChartDateUtils().getCurrentDayOfWeek(hourIndex))
        }
        d("MockData createXAxisList xValueHourList : ${xValueHourList}")
        return xValueHourList
    }

    /**x축 value 테스트용*/
    fun createPointChartXValueList(): ArrayList<Long> {
        val xValueHourList = arrayListOf<Long>()
        for (hourIndex in 0..6) {
//            d("MockData createHourOfDay getZeroSecMinDate : ${ChartDateUtils().getZeroSecMinDate(time)}")
//            d("MockData createHourOfDay getTimestamp : ${ChartDateUtils().getTimestamp(getZeroSecMinDate(time))}")
            xValueHourList.add(ChartDateUtils().getToHourZeroSecMinDate(time = null, hour = hourIndex))
        }
        d("MockData createXValueList xValueHourList : ${xValueHourList}")
        return xValueHourList
    }

    /**y축 value 테스트용*/
    fun createPointChartYValueList(): ArrayList<Int?> {
        /** xValue에 맞춰 일~토 리스트*/
        val yValues = arrayListOf<Int?>(
            58, 59, null, null, 60, null, 58
        )
        d("MockData createYValueList yValues : ${yValues}")
        return yValues
    }
}