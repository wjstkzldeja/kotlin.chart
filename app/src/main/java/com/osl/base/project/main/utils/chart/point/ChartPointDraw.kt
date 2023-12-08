package com.osl.base.project.main.utils.chart.point

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.osl.base.project.main.utils.date.ChartDateUtils
import com.osl.base.project.main.utils.dpiToPixels
import timber.log.Timber.Forest.d


class ChartPointDraw(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {
    private var canvasWidth: Float = 0.0f
    private var canvasHeight: Float = 0.0f

    /** paint, path*/
    private var linePaint: Paint = Paint()
    private val curveLinePath: Path = Path()
    private var ringPaint: Paint = Paint()
    private var ringCenterPaint: Paint = Paint()
    private var textPaint: Paint = Paint()

    /** list*/
    var xValueList: ArrayList<Long> = arrayListOf()
    var yValueList: ArrayList<Float?> = arrayListOf()

    var xPointList: ArrayList<Float> = arrayListOf()
    var yPointList: ArrayList<Float> = arrayListOf()

    /** x,y 축 간격(마진값)
     * 아이템 그릴때 간격*/
    private var xAxisInterval = 0f

    /** x,y 축 갯수
     * x : 라인은 그릴 갯수 (페이징 생각하면됨)
     * y : (List.size -1)
     * 사이즈-1 을 해줘야 하는 이유(사이즈는 11이지만 10등분을 해야하기 때문)
     * */
    var xMaxCount = 0//그릴 x 갯수, 현재 0~6{6}
    var yMaxCount = 10//그릴 y 갯수, 현재 0~10(10)

    /** x축 max,min 값
     * min : 현재 사용하지 않음
     * max : 백분율로 좌표 구할때 사용
     * */
    var xMinValue = 0 //현재 사용하지 않음
    var xMaxValue = 0 //100

    /** y축 max,min 값
     * min : y축 최소값(몸무게에 따라)
     * max : 백분율로 좌표 구할때 사용
     * */
    var yMinValue = 0
    var yMaxValue = 0//100

    var lineColor = Color.parseColor("#587CF7")
    var ovalColor = Color.parseColor("#587CF7")
    var ovalColorDark = Color.parseColor("#FFFFFF")
    val textColorNone = Color.parseColor("#66000000")
    val textColorSel = Color.parseColor("#587CF7")

    private val lineBgPath: Path = Path()
    private var lineBgPaint: Paint = Paint()

    init {
        linePaint.apply {
            color = lineColor
            strokeWidth = dpiToPixels(context!!, 2)
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            isAntiAlias = true
        }

        ringPaint.apply {
            color = ovalColor
            strokeWidth = dpiToPixels(context!!, 2)
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
        ringCenterPaint.apply {
            color = ovalColorDark
            strokeWidth = dpiToPixels(context!!, 2)
            style = Paint.Style.FILL_AND_STROKE
            isAntiAlias = true
        }
        textPaint = Paint().apply {
            color = textColorNone
            textSize = dpiToPixels(context!!, 14)
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        lineBgPaint.apply {
            style = Paint.Style.FILL
            isAntiAlias = true
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        /** xml 높이보다 작게 그리기위해 캔버스 높이를 작게 만들기 위해 사용(위에 짤림 방지용), dp만큼 캔버스 높이 줄임*/
        val viewHeightMargin = dpiToPixels(context, 40)
        canvasWidth = width.toFloat()
        canvasHeight = height.toFloat() - viewHeightMargin

        /** 아이템 전체 leftMargin, 아이템 간에 간격 조정됨 */
        val leftMargin = dpiToPixels(context, 80)

        xAxisInterval = (canvasWidth - leftMargin) / xMaxCount.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        preCalculated()
        drawLineBg(canvas)
        drawLine(canvas)
        drawRing(canvas)
    }

    fun preCalculated() {
        /** 첫번째 아이템 leftMargin, 왼쪽 여백*/
        val leftMargin = dpiToPixels(context, 71 + 10)
        /**
         * xPercent = calHourPercentage에서 나온 계산값 0.1~n.n
         * xValue = 넓이에서 백분율로 좌표값 구함
         * */
        d("PointChart curveLinePreDraw xValueList : ${xValueList}")
        d("PointChart curveLinePreDraw yValueList : ${yValueList}")
        val xPointTemp = ArrayList<Float>()
        val yPointTemp = ArrayList<Float>()
        xValueList.forEachIndexed { index, value ->
            val xPercent = xAxisInterval * index + leftMargin
            val xValuePoint =
                xAxisInterval * index + leftMargin//xPercent * canvasWidth + leftMargin
            d("PointChart curveLinePreDraw x : index : ${index} ,xPercent : ${xPercent}, xValuePoint : ${xValuePoint}")
            xPointTemp.add(xValuePoint)
        }


        /** 아이템 전체 topMargin
         * xml 높이보다 작게 그리기위해 dp만큼 더해서 그려준다(위에 짤림 방지용*/
        val topMargin = dpiToPixels(context, 40)

        /** 아이템 전체 bottomMargin, 하단 여백*/
        val bottomMargin = dpiToPixels(context, 20)

        /**
         * yPercent = (y축 값-y축 최소값)/(최대값-y축 최소값) -> 백분율 계산(0.0~n.n)
         * -y축 최소값 해주는 이유 -> 0으로 맞춰서 백분율 구하기위해
         * yValue = 높이로 백분율 좌표값 구해서 최대높이에서 빼준다
         * */
        yValueList.forEachIndexed { index, value ->
            var yValueNotNull = value

            /** 값중에 null 인 경우
             * 이중 포문으로 null이었던 값까지 포문이 돌고 반복문 중에 마지막 값을 사용함
             * ex) List : {58, 59, null, null, 60, null, 58}
             *  - 2번(null) 인덱스 : 1번(59) 인덱스 사용
             *  - 3번(null) 인덱스 : 1번(59) 인덱스 사용
             *  - 5번(null) 인덱스 : 4번(60) 인덱스 사용
             *  이슈 : 0인덱스 null인 경우 현재는 yMinValue 적용됨*/
            if (value == null) {
                for (i in 0..index) {
                    if (yValueList[i] != null) {
                        yValueNotNull = yValueList[i]
                    }
                }
            }

            val yPercent =
                (((yValueNotNull ?: yMinValue).toFloat() - yMinValue)) / (yMaxValue - yMinValue)
            val yValuePoint = canvasHeight - (yPercent * canvasHeight) + (topMargin - bottomMargin)
            d("PointChart curveLinePreDraw y : index : ${index} ,yPercent : ${yPercent}, yValuePoint : ${yValuePoint}")
            yPointTemp.add(yValuePoint)
        }
        xPointList = xPointTemp
        yPointList = yPointTemp
        d("PointChart curveLinePreDraw point : xPointList : ${xPointList}")
        d("PointChart curveLinePreDraw point : yPointList : ${yPointList}")
    }

    private fun drawLine(canvas: Canvas) {
        curveLinePath.reset()
        for (index in 0 until xValueList.size) {
            if (index == 0) {
                curveLinePath.moveTo(xPointList[index], yPointList[index])
            }
            curveLinePath.lineTo(xPointList[index], yPointList[index])
        }
        canvas.drawPath(curveLinePath, linePaint)
    }

    private fun drawRing(canvas: Canvas) {
        for (index in 0 until xValueList.size) {
            /**좌표에 dot 그리기*/
            drawArcsRing(canvas, xPointList[index], yPointList[index])
            drawValueText(canvas, index, yValueList[index], xPointList[index], yPointList[index])
        }
    }

    private fun drawValueText(
        canvas: Canvas,
        index: Int,
        yValue: Float?,
        xValuePoint: Float,
        yValuePoint: Float
    ) {
        /** 아이템 전체 bottomMargin, 하단 여백*/
        val bottomMargin = dpiToPixels(context, 10)

        val nonNullList = yValueList.filterNotNull()
        val max: Float? = nonNullList.maxOrNull()
        if (yValue == max) {
            textPaint.color = textColorSel
        } else {
            textPaint.color = textColorNone
        }
        canvas.drawText(
            (yValue ?: "-").toString(),
            xValuePoint,
            yValuePoint - bottomMargin,
            textPaint
        )
    }

    /** 좌표에 링 그리기*/
    private fun drawArcsRing(
        canvas: Canvas,
        xValuePoint: Float,
        yValuePoint: Float
    ) {
        val ovalSize = dpiToPixels(context, 10)

        val centerX = (xValuePoint - (ovalSize / 2f))
        val centerY = (yValuePoint - (ovalSize / 2f))

        val oval = RectF(centerX, centerY, centerX + ovalSize, centerY + ovalSize)
        canvas.drawArc(oval, 0f, 360f, true, ringCenterPaint)
        canvas.drawArc(oval, 0f, 360f, true, ringPaint)
    }

    val colors = intArrayOf(
        Color.parseColor("#5480F7"),
        Color.parseColor("#1A5480F7"),
    )

    /** 좌표에 가운데 가리는 링 그리기*/
    private fun drawArcsRingCneter(
        canvas: Canvas,
        xValuePoint: Float,
        yValuePoint: Float
    ) {
        val ovalSize = dpiToPixels(context, 5)

        val centerX = (xValuePoint - (ovalSize / 2f))
        val centerY = (yValuePoint - (ovalSize / 2f))

        val oval = RectF(centerX, centerY, centerX + ovalSize, centerY + ovalSize)
//        canvas.drawArc(oval, 0f, 360f, true, ringCenterPaint)
    }

    /** 이거 때문에 xml 안나옴*/
    private fun drawLineBg(canvas: Canvas) {
        if (xPointList.isEmpty()) return
        val bottomMargin = dpiToPixels(context, 20)

        /** 차트 그라데이션 배경
         * 1. 기존 라인 차트와 동일하게 그린 후
         * 2. (x = 마지막 x ,y = 맨아래로{height}) 으로 이동
         * 3. (x = 처음으로 ,y = 맨아래로{height}) 으로 이동
         * 4. 전체로 보면 사각형을 만든다 생각하고  안에 색상 채워줌
         * */
        val calHeight = height.toFloat() - bottomMargin
        lineBgPath.reset()
        lineBgPaint.apply {
            shader = LinearGradient(
                0f,
                0f,
                0f,
                calHeight,
                colors,
                null,
                Shader.TileMode.CLAMP
            )
        }
        lineBgPath.moveTo(xPointList[0], calHeight)
        for (index in 0 until xValueList.size) {
            lineBgPath.lineTo(xPointList[index], yPointList[index])
        }

        /** 이건 반대로 위에 채울때*/
        /*    erasePath.lineTo(width.toFloat(), y)
            erasePath.lineTo(width.toFloat(), 0f)
            erasePath.lineTo(0f, 0f)*/

        xPointList.lastOrNull()?.let { lineBgPath.lineTo(it, calHeight) }
        lineBgPath.lineTo(0f, calHeight)

        canvas.drawPath(lineBgPath, lineBgPaint)
    }
}