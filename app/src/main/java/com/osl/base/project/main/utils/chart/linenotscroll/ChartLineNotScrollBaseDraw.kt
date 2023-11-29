package com.osl.base.project.main.utils.chart.linenotscroll

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.osl.base.project.main.utils.dpiToPixels
import timber.log.Timber.Forest.d


class ChartLineNotScrollBaseDraw(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {
    private var canvasWidth: Float = 0.0f
    private var canvasHeight: Float = 0.0f

    /** paint, path*/
    private val baseLineXPaint: Paint = Paint()
    private val baseGraduationPaint: Paint = Paint()

    /** list*/
    var yValueList: ArrayList<Int> = arrayListOf()

    /** x,y 축 갯수
     * x : 라인은 그릴 갯수 (페이징 생각하면됨)
     * y : (List.size -1)
     * 사이즈-1 을 해줘야 하는 이유(사이즈는 11이지만 10등분을 해야하기 때문)
     * */
    var xMaxCount = 0//그릴 x 갯수, 현재 0~6{6}
    var yMaxCount = 0//그릴 y 갯수, 현재 0~10(10)

    /** x,y 축 간격(마진값)*/
    var xInterval = 0f
    var yInterval = 0f

    private val graduationColor=Color.parseColor("#99D6D6D6")
    init {
        baseLineXPaint.apply {
            strokeWidth = dpiToPixels(context!!, 6)
            style = Paint.Style.FILL
            color = Color.GRAY
            isAntiAlias = true
        }
        baseGraduationPaint.apply {
            strokeWidth = dpiToPixels(context!!, 1)
            style = Paint.Style.STROKE
            color = graduationColor
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
        val leftMargin = dpiToPixels(context, 0)

        /** x,y 축 간격(마진값) 최대값 / 아이템 갯수 */
        xInterval = (canvasWidth - leftMargin) / xMaxCount.toFloat()
        yInterval = canvasHeight / yMaxCount.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
//        baseLineDraw(canvas)
        baseGraduationLineDrawTest(canvas)
    }

    /** x,y 기준선 그리는 함수*/
    private fun baseLineDraw(canvas: Canvas) {
        val marginLeft = dpiToPixels(context, 30)
        val marginBottom = dpiToPixels(context, 30)
        val xStartX = marginLeft
        val xStopX = canvasWidth
        val xStartY = canvasHeight - marginBottom
        val xStopY = canvasHeight - marginBottom

        val yStartX = marginLeft
        val yStopX = marginLeft
        val yStartY = 0f
        val yStopY = (canvasHeight - marginBottom)
        //x 축
        canvas.drawLine(xStartX, xStartY, xStopX, xStopY, baseLineXPaint)
        //y 축
        canvas.drawLine(yStartX, yStartY, yStopX, yStopY, baseLineXPaint)
    }

    /** 눈금 그려주는 함수
     * yValuePoint = 간격값 * 인덱스 0~n*/
    private fun baseGraduationLineDrawTest(canvas: Canvas) {
        /** 아이템 전체 topMargin
         * xml 높이보다 작게 그리기위해 dp만큼 더해서 그려준다(위에 짤림 방지용*/
        val topMargin = dpiToPixels(context, 40)
        /** 첫번째 아이템 leftMargin, 왼쪽 여백*/
        val leftMargin = dpiToPixels(context, 53)

        /** 아이템 전체 bottomMargin, 하단 여백*/
        val bottomMargin = dpiToPixels(context, 20)

        /** 길이,간격*/
        val intervals = floatArrayOf(dpiToPixels(context, 2), dpiToPixels(context, 2))

        /**점선을 그리기 위한 효과 설정, phase:첫번째 dash가 얼만큼 잘려도 되는지 시작값*/
        val effect = DashPathEffect(intervals, dpiToPixels(context, 1))
        baseGraduationPaint.pathEffect = effect

        yValueList.forEachIndexed { index, yValue ->
            val yValuePoint = canvasHeight - (yInterval * index) +topMargin-bottomMargin
            d("baseChart : index : ${index}, yValuePoint : ${yValuePoint}")
            //x 축
            canvas.drawLine(0f+leftMargin, yValuePoint, canvasWidth, yValuePoint, baseGraduationPaint)
//            if (yValue != 100) {
//                canvas.drawLine(0f+leftMargin, yValuePoint, canvasWidth, yValuePoint, baseGraduationPaint)
//            }
        }
    }
}