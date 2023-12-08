package com.osl.base.project.main.utils.chart.line

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.osl.base.project.main.R
import com.osl.base.project.main.utils.date.ChartDateUtils
import com.osl.base.project.main.utils.dpiToPixels
import timber.log.Timber.Forest.d


class ChartLineAxisDraw(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {
    private var translateMatrix: Matrix
    private var scrollByX: Float = 0f

    /** 캔버스 사이즈 공용으로 사용하기 위해*/
    private var canvasWidth: Float = 0.0f
    private var canvasHeight: Float = 0.0f

    /** paint, path*/
    private var xAxisTextPaint: Paint = Paint()
    private var yAxisTextPaint: Paint = Paint()
    private var hiddenPaint: Paint = Paint()

    /** list */
    var xAxisList: ArrayList<Long> = arrayListOf()
    var yAxisList: ArrayList<Int> = arrayListOf()

    /** x,y 축 갯수
     * x : 라인은 그릴 갯수 (페이징 생각하면됨)
     * y : 그랠 갯수 (List.size -1)
     * */
    var xAxisMaxCount = 0//x 그릴 갯수
    var yAxisMaxCount = 0//y 그릴 갯수

    /** x,y 축 간격(마진값)
     * 아이템 그릴때 간격*/
    private var xAxisInterval = 0f
    private var yAxisInterval = 0f

    /** 텍스트 높이 값 */
    private var yAxisBoundsHeight = 0


    private val textColor=Color.parseColor("#6A6A6A")
    private val bgColor=Color.parseColor("#070318")

    init {
        xAxisTextPaint = Paint().apply {
            color = textColor
            textSize = dpiToPixels(context!!, 12)
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
            typeface = ResourcesCompat.getCachedFont(context, R.font.suit_regular_400)
        }
        yAxisTextPaint = Paint().apply {
            color = textColor
            textSize = dpiToPixels(context!!, 12)
            isAntiAlias = true
            typeface = ResourcesCompat.getCachedFont(context, R.font.suit_regular_400)
        }
        /** 텍스트 높이값 추출 */
        val bounds = Rect()
        yAxisTextPaint.getTextBounds("100", 0, 3, bounds)
        yAxisBoundsHeight = bounds.height()

        translateMatrix = Matrix()

        hiddenPaint.apply {
            color = bgColor
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        /** xml 높이보다 작게 그리기위해 캔버스 높이를 작게 만들기 위해 사용(위에 짤림 방지용), dp만큼 캔버스 높이 줄임*/
        val viewHeightMargin = dpiToPixels(context, 40)
        canvasWidth = width.toFloat()
        canvasHeight = height.toFloat() - viewHeightMargin

        /** 아이템 전체 leftMargin, 아이템 간에 간격 조정됨 */
        val leftMargin = dpiToPixels(context, 35)

        /** 간격(마진값) 구하는 함수, 넓이 기준 x갯수로 간격 계산
         * xInterval = width-라인차트와의 마진값 / xAxisMaxCount
         * yInterval = height-라인차트와의 마진값 / yAxisMaxCount
         * xml 높이40dp,넓이40dp 만큼 빼주고 계산
         */
        xAxisInterval = (canvasWidth - leftMargin) / xAxisMaxCount.toFloat()
        yAxisInterval = (canvasHeight) / yAxisMaxCount.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        d("TextChart", "width : ${width}")
        d("TextChart", "height : ${height}")
        drawTranslate(canvas)
        aXisHidden(canvas)
        drawYAxis(canvas)
    }

    private fun drawTranslate(canvas: Canvas) {
        d("TextChart matrixTest")
        canvas.save()
        translateMatrix.setTranslate(scrollByX, 0f)
        canvas.setMatrix(translateMatrix)
        /**이동해야하는 함수들*/
        drawXAxis(canvas)
        canvas.restore()
    }

    /**
     *  width : 1080
     *  height : 990
     * */
    private fun drawXAxis(canvas: Canvas) {
        /** 첫번째 아이템 leftMargin, 왼쪽 여백*/
        val leftMargin = dpiToPixels(context, 67)

        /** 아이템 전체 bottomMargin, 하단 여백*/
        val bottomMargin = dpiToPixels(context, 3)
        /** xAxis
         * xAxisInterval을 인덱스마다 곱해줘서 간격을 벌림
         * xValuePoint = 간격값 * 인덱스 0~n + leftMargin(왼쪽 마진값
         * */
        xAxisList.forEachIndexed { index, xValue ->
            val xValuePoint = xAxisInterval * index + leftMargin
//            d(
//                "TextChart drawXAxis : index : ${index}, xValuePoint : ${xValuePoint}"
//            )
            canvas.drawText(
                ChartDateUtils().chartGetTimestamp(xValue),
                xValuePoint,
                (canvasHeight + dpiToPixels(context, 40)) - bottomMargin,
                xAxisTextPaint
            )
        }
    }

    private fun drawYAxis(canvas: Canvas) {
        /** leftMargin, 왼쪽 여백*/
        val leftMargin = dpiToPixels(context, 16)
        /** 아이템 전체 topMargin
         * xml 높이보다 작게 그리기위해 dp만큼 더해서 그려준다(위에 짤림 방지용*/
        val topMargin = dpiToPixels(context, 40)
        /** 아이템 전체 bottomMargin, 하단 여백*/
        val bottomMargin = dpiToPixels(context, 20)
        /** yAxis
         * yValuePoint = 높이 - (간격값 * 인덱스 0~n) + xml dp차이(40dp)
         * yAxisBoundsHeight / 2 = bounds로 텍스트 높이 구해서 가운데 정렬 시키기 위헤
         * 0이 아닌 경우 텍스트 높이값 계산해서 내려가도록
         * */
        yAxisList.forEachIndexed { index, yValue ->
            val yValuePoint: Float = if (index == 0) {
                (canvasHeight + yAxisBoundsHeight / 2) - (yAxisInterval * index) + (topMargin- bottomMargin)
                //canvasHeight - (yAxisInterval * index) + topMargin
            } else {
                (canvasHeight + yAxisBoundsHeight / 2) - (yAxisInterval * index) + (topMargin- bottomMargin)
            }
//            d(
//                "TextChart drawYAxis : index : ${index}, yValuePoint : ${yValuePoint}"
//            )
            canvas.drawText(yValue.toString(), leftMargin, yValuePoint, yAxisTextPaint)
//            if (yValue != 0) {
//                canvas.drawText(yValue.toString(), leftMargin, yValuePoint, yAxisTextPaint)
//            }
        }
    }

    fun axisScrollEvent(scrollValue: Float) {
        scrollByX = scrollValue
        postInvalidateOnAnimation()
    }

    /** axis 슬라이드시 차트 가려주는 히든영역*/
    private fun aXisHidden(canvas: Canvas) {
        val leftMargin = dpiToPixels(context, 53)
        canvas.drawRect(0f, 0f, leftMargin, height.toFloat(), hiddenPaint)
    }
}