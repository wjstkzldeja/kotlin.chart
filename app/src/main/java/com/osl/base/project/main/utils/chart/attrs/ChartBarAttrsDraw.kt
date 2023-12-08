package com.osl.base.project.main.utils.chart.attrs

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.osl.base.project.main.R
import com.osl.base.project.main.utils.chart.ChartEngineUtils
import com.osl.base.project.main.utils.date.ChartDateUtils
import com.osl.base.project.main.utils.dpiToPixels
import timber.log.Timber.Forest.d


class ChartBarAttrsDraw(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {
    /** 캔버스 사이즈 공용으로 사용하기 위해*/
    private var canvasWidth: Float = 0.0f
    private var canvasHeight: Float = 0.0f

    /** paint, path*/
    private var barPaint: Paint = Paint()
    private val dottedLinePaint: Paint = Paint()

    /** list */
    var xValueList: ArrayList<Long> = arrayListOf()
    var yValueList: ArrayList<Int> = arrayListOf()

    /** x,y 축 간격(마진값)
     * 아이템 그릴때 간격*/
    private var xInterval = 0f

    /** x,y 축 갯수
     * x : 라인은 그릴 갯수 (페이징 생각하면됨)
     * y : 그랠 갯수 (List.size -1)
     * */
    var xMaxCount = 0//x 그릴 갯수
    var yMaxCount = 0//y 그릴 갯수

    /** y축 max,min 값
     * min : 백분율로 좌표 구할때 사용
     * max : 백분율로 좌표 구할때 사용
     * */
    var yMinValue = 0
    var yMaxValue = 0//100

    /** 평균 값*/
    var averageValue = 0f

    var barColorSel = Color.parseColor("#587cf7")
    var barColorNone = Color.parseColor("#a9c4f6")
    val graduationColor = Color.parseColor("#f75875")

    init {
        /** attrs -> xml 에서 전달된 값으로 차트 구현*/
        context?.theme?.obtainStyledAttributes(attrs, R.styleable.ChartAttr, 0, 0)?.apply {
            /** Y 최소 값*/
            yMinValue = getInt(R.styleable.ChartAttr_yMinValue, 0)
            /** Y 최대 값*/
            yMaxValue = getInt(R.styleable.ChartAttr_yMaxValue, 0)
            /** Y 정적 리스트*/
            yValueList = ChartEngineUtils().createBarChartYValueList(yMinValue)
            /** X 그려질 최대 갯수*/
            xMaxCount = getInt(R.styleable.ChartAttr_xMaxCount, 0)
            /** Y 그려질 최대 갯수(동적 구현 : 리스트 사이즈 - 1)*/
            yMaxCount = (yValueList.size - 1)
        }

        barPaint = Paint().apply {
            color = barColorSel
            isAntiAlias = true
        }
        dottedLinePaint.apply {
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
        val leftMargin = dpiToPixels(context, 80)

        xInterval = (canvasWidth - leftMargin) / xMaxCount.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        d("barChart bar: ${height}")
        drawBar(canvas)
        drawDottedLine(canvas)
    }

    private fun drawBar(canvas: Canvas) {
        /** 아이템 전체 topMargin
         * xml 높이보다 작게 그리기위해 dp만큼 더해서 그려준다(위에 짤림 방지용*/
        val topMargin = dpiToPixels(context, 40)

        /** 첫번째 아이템 leftMargin, 왼쪽 여백*/
        val leftMargin = dpiToPixels(context, 71+10)

        /** 아이템 전체 bottomMargin, 하단 여백*/
        val bottomMargin = dpiToPixels(context, 20)

        /** 막대 넓이 */
        val barWidth = dpiToPixels(context, (14/2))


        yValueList.forEachIndexed { index, value ->
            val xValuePoint = xInterval * index + leftMargin

            val yPercent = ((value.toFloat() - yMinValue)) / (yMaxValue - yMinValue)
            val yValuePoint = canvasHeight - (yPercent * canvasHeight) + (topMargin - bottomMargin)

            /** rectF bottom값 : 캔버스 높이 + (xml보다 작게그릴 dp - 하단 마진값*/
            val rectBottom = canvasHeight + (topMargin - bottomMargin)

            /** 현재 요일 값 1~7을 기준으로 paint 변경 */
            if ((index + 1) == ChartDateUtils().currentDayOfWeek()) {
                barPaint.color = barColorSel
            } else {
                barPaint.color = barColorNone
            }
            /** 기본 막대*/
            canvas.drawRect(
                (xValuePoint - barWidth),
                yValuePoint,
                (xValuePoint + barWidth),
                rectBottom,
                barPaint
            )


            /** 상단 라운드 막대*/
            /*val radius = dpiToPixels(context, 10) // 라운드 반지름 값
            val path = Path()
            val rectF = RectF((xValuePoint - barWidth), yValuePoint, (xValuePoint + barWidth), rectBottom)
            path.addRoundRect(rectF, floatArrayOf(radius, radius, radius, radius, 0f, 0f, 0f, 0f), Path.Direction.CW)
            canvas.drawPath(path, barPaint)*/
        }
    }

    /** 점선*/
    private fun drawDottedLine(canvas: Canvas) {
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
        dottedLinePaint.pathEffect = effect

        /**1400f 값 나중에 변경 필요*/
        val yPercent = ((averageValue - yMinValue)) / (yMaxValue - yMinValue)
        val yValuePoint = canvasHeight - (yPercent * canvasHeight) + (topMargin - bottomMargin)
        canvas.drawLine(0f + leftMargin, yValuePoint, width.toFloat(), yValuePoint, dottedLinePaint)
    }
}