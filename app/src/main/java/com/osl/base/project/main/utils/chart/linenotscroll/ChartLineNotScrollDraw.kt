package com.osl.base.project.main.utils.chart.linenotscroll

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.osl.base.project.main.R
import com.osl.base.project.main.utils.chart.ChartEngineUtils
import com.osl.base.project.main.utils.chart.WfInfoVo
import com.osl.base.project.main.utils.date.ChartDateUtils
import com.osl.base.project.main.utils.dpiToPixels
import timber.log.Timber.Forest.d
import java.lang.Math.abs


class ChartLineNotScrollDraw(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {
    private var touchIndex: Int = 0
    private var translateMatrix: Matrix
    private var canvasWidth: Float = 0.0f
    private var canvasHeight: Float = 0.0f

    /** paint, path*/
    private var curveLinePaint: Paint = Paint()
    private val curveLinePath: Path = Path()
    private var ringPaint: Paint = Paint()
    private var ringCenterPaint: Paint = Paint()
    private var targetZonePaint: Paint = Paint()
    private var targetZoneTextPaint: Paint = Paint()
    private var balloonPaint: Paint = Paint()
    private var textPaint: Paint = Paint()
    private var unitTextPaint: Paint = Paint()
    private var timeTextPaint: Paint = Paint()

    /**images*/
    private var dietNor: Bitmap? = null
    private var dietSel: Bitmap? = null
    private var exerciseNor: Bitmap? = null
    private var exerciseSel: Bitmap? = null

    /** list*/
    var xValueList: ArrayList<Long> = arrayListOf()
    var yValueList: ArrayList<Int> = arrayListOf()
    var xPointList: ArrayList<Float> = arrayListOf()
    var yPointList: ArrayList<Float> = arrayListOf()

    /** 음식, 운동 리스트 */
    var xFoodHealthValueList: ArrayList<WfInfoVo> =
        arrayListOf(
            WfInfoVo("음식", "정보_음식", "타입_음식"),
            WfInfoVo("운동", "정보_운동", "타입_운동")
        )
    var xFoodHealthTimeList: ArrayList<Long> = arrayListOf(
        ChartDateUtils().getToHourZeroSecMinDateTwo(
            time = null,
            hour = 1,
            DateDiff = 0
        ), ChartDateUtils().getToHourZeroSecMinDateTwo(time = null, hour = 2, DateDiff = 0)
    )
    var xFoodHealthPointList: ArrayList<Float> = arrayListOf()
    var yFoodHealthPointList: ArrayList<Float> = arrayListOf()


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
     * min : 현재 사용하지 않음
     * max : 백분율로 좌표 구할때 사용
     * */
    var yMinValue = 0
    var yMaxValue = 0//100

    /** 타겟존 좌표*/
    private var targetZoneLowPoint: Float = 0f
    private var targetZoneHighPoint: Float = 0f

    /** 터치시 말풍선 모드*/
    private var touchMode: String = ""//blood,foodHealth

    private val highLineColor = Color.parseColor("#AB3452")
    private val lowLineColor = Color.parseColor("#5480F7")
    private val targetZoneLineColor = Color.parseColor("#4D5480F7")
    private val textColor = Color.parseColor("#616161")

    init {
        curveLinePaint.apply {
            color = lowLineColor
            strokeWidth = dpiToPixels(context!!, 4)
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            isAntiAlias = true
        }

        dietNor = BitmapFactory.decodeResource(context?.resources, R.drawable.ic_graph_diet_nor)
        dietSel = BitmapFactory.decodeResource(context?.resources, R.drawable.ic_graph_diet_sel)
        exerciseNor =
            BitmapFactory.decodeResource(context?.resources, R.drawable.ic_graph_exercise_nor)
        exerciseSel =
            BitmapFactory.decodeResource(context?.resources, R.drawable.ic_graph_exercise_sel)
        translateMatrix = Matrix()

        ringPaint.apply {
            color = Color.BLUE
            strokeWidth = dpiToPixels(context!!, 4)
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
        ringCenterPaint.apply {
            color = Color.WHITE
            strokeWidth = dpiToPixels(context!!, 4)
            style = Paint.Style.FILL_AND_STROKE
            isAntiAlias = true
        }
        targetZonePaint.apply {
            color = targetZoneLineColor
        }
        targetZoneTextPaint.apply {
            color = textColor
            textSize = dpiToPixels(context!!, 10)
            isAntiAlias = true
        }
        balloonPaint.apply {
            balloonPaint.color = Color.WHITE
            balloonPaint.style = Paint.Style.FILL_AND_STROKE
            balloonPaint.strokeWidth = dpiToPixels(context!!, 1)
            balloonPaint.isAntiAlias = true
        }
        textPaint.apply {
            color = Color.BLACK
            textSize = dpiToPixels(context!!, 12)
            isAntiAlias = true
            textAlign = Paint.Align.RIGHT
            typeface = ResourcesCompat.getCachedFont(context, R.font.suit_bold_700)
        }
        unitTextPaint.apply {
            color = Color.BLACK
            textSize = dpiToPixels(context!!, 8)
            isAntiAlias = true
            textAlign = Paint.Align.LEFT
            typeface = ResourcesCompat.getCachedFont(context, R.font.suit_medium_500)
        }
        timeTextPaint.apply {
            color = Color.BLACK
            textSize = dpiToPixels(context!!, 8)
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            typeface = ResourcesCompat.getCachedFont(context, R.font.suit_medium_500)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val viewHeightMargin = dpiToPixels(context, 40)
        canvasWidth = width.toFloat()
        canvasHeight = height.toFloat() - viewHeightMargin

        if (xValueList.isEmpty() || yValueList.isEmpty()) return
        preCalculated()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (xValueList.isEmpty()) return
        d("lineChart onDraw width : ${width}")
        d("lineChart onDraw height : ${height}")
        drawTargetZone(canvas)
        drawTargetZoneText(canvas)
        drawTranslate(canvas)
    }

    private fun drawTranslate(canvas: Canvas) {
        /** clipRect left 마진*/
        val leftMargin = dpiToPixels(context, 53)
        canvas.save()
        /**이동해야하는 함수들*/
        curveLinePaint.color = highLineColor
        drawLine(canvas)

        canvas.save()
        /** 그릴 영역 지정 시작*/
        canvas.clipRect(leftMargin, targetZoneHighPoint, xPointList.last(), height.toFloat())
        curveLinePaint.color = lowLineColor
        drawLine(canvas)
        canvas.restore()
        /** 그릴 영역 지정 끝*/
        drawArcsRing(canvas)
        drawFoodHealthImage(canvas)
        if (touchMode == "blood") {
            drawTextBubbleBlood(canvas)
        } else if (touchMode == "foodHealth") {
            drawTextBubbleFoodHealth(canvas)
        }
        canvas.restore()

    }

    /** draw lin 미리 그리기*/
    fun preCalculated() {
        d(
            "lineChart curveLinePreDraw xValueList : ${xValueList}",
        )
        d(
            "lineChart curveLinePreDraw yValueList : ${yValueList}",
        )

        val xPointTemp = ArrayList<Float>()
        val yPointTemp = ArrayList<Float>()
        xValueList.forEachIndexed { index, value ->
            val xValuePoint = xPointCal(value)
            d("lineChart curveLinePreDraw x : index : ${index} ,xValuePoint : ${xValuePoint} , value: ${value}")
            xPointTemp.add(xValuePoint)
        }

        yValueList.forEachIndexed { index, value ->
//            val random = (100..300).random()//Test용
            val yValuePoint = yPointCal(value)
            d("lineChart curveLinePreDraw y : index : ${index} ,yValuePoint : ${yValuePoint}")
            yPointTemp.add(yValuePoint)
        }
        xPointList = xPointTemp
        yPointList = yPointTemp
        d("lineChart curveLinePreDraw point : xPointList : ${xPointList}")
        d("lineChart curveLinePreDraw point : yPointList : ${yPointList}")
        preDrawFoodHealthImage()
    }

    /** draw image 미리 그리기*/
    private fun preDrawFoodHealthImage() {
        val xFoodHealthPointListTemp = ArrayList<Float>()
        val yFoodHealthPointListTemp = ArrayList<Float>()
        if (xFoodHealthTimeList.isEmpty()) return
        d("drawFoodHealthImage xValueList = : ${xValueList}")
        xFoodHealthTimeList.forEachIndexed { index, value ->
            val closestIndex = findClosestSmaller(xValueList, value)
            d("drawFoodHealthImage value = : ${value}")
            d("drawFoodHealthImage closestIndex : ${closestIndex}")
            val xFoodValuePoint = xPointCal(value)
            val intersection = if (xPointList.size <= 1) {
                /** 혈당 리스트가 1개인경우*/
                Pair(xPointList[closestIndex], yPointList[closestIndex])
            } else {
                if (xPointList.size <= (closestIndex + 1)) {
                    /** 혈당 리스트보다 근접값 리스트가 큰경우 = 혈당 그래프 제외범위 그릴필요가 없음*/
                    return
                } else {
                    getIntersection(
                        xPointList[closestIndex],
                        yPointList[closestIndex],
                        xPointList[closestIndex + 1],
                        yPointList[closestIndex + 1],
                        xFoodValuePoint,
                        0f,
                        xFoodValuePoint,
                        canvasHeight
                    )
                }
            }

            xFoodHealthPointListTemp.add(xFoodValuePoint)
            yFoodHealthPointListTemp.add(intersection?.second ?: 0f)
            xFoodHealthPointList = xFoodHealthPointListTemp
            yFoodHealthPointList = yFoodHealthPointListTemp

            /** http://swrnd.olivestonelab.com:34101/proj/lguplus/gcare/issue/-/issues/335
             * 혈당 앱 down 이슈*/
            /*xFoodHealthPointList.add(xFoodValuePoint)
            yFoodHealthPointList.add(intersection?.second ?: 0f)*/
        }
    }

    /**
     * xPercent = calHourPercentage에서 나온 계산값 0.1~n.n
     * xValue = 넓이에서 백분율로 좌표값 구함
     * */
    private fun xPointCal(value: Long): Float {
        /** 첫번째 아이템 leftMargin, 왼쪽 여백
         * drawXAxis와 위치 맞추기위해*/
        val leftMargin = dpiToPixels(context, (53 + 14))

        /** 아이템 전체 leftMargin, 아이템 간에 간격 조정됨 */
        val leftMarginInterval = dpiToPixels(context, (90))
        val xPercent = calHourPercentage(value)
        val xValuePoint = xPercent * (canvasWidth - leftMarginInterval) + leftMargin

        return xValuePoint
    }

    /**
     * yPercent = (y축 값-y축 최소값)/(최대값-y축 최소값) -> 백분율 계산(0.0~n.n)
     * -y축 최소값 해주는 이유 -> 0으로 맞춰서 백분율 구하기위해
     * yValue = 높이로 백분율 좌표값 구해서 최대높이에서 빼준다
     * */
    private fun yPointCal(value: Int): Float {
        /** 아이템 전체 topMargin
         * xml 높이보다 작게 그리기위해 dp만큼 더해서 그려준다(위에 짤림 방지용*/
        val topMargin = dpiToPixels(context, 40)

        /** 아이템 전체 bottomMargin, 하단 여백*/
        val bottomMargin = dpiToPixels(context, 20)

        val yPercent = ((value.toFloat() - yMinValue)) / (yMaxValue - yMinValue)
        val yValuePoint = canvasHeight - (yPercent * canvasHeight) + (topMargin - bottomMargin)

        return yValuePoint
    }

    private fun calHourPercentage(value: Long): Float {
        /** 시간으로 백분율 구하는 함수
         * 1.시작 시간, 현재 시간을 빼준다
         * 2.totalTimeInMillis = 현재 한 페이지를 max값 -> 현재 6개라서 6을 곱해주면 한화면 최대값 나옴
         * 3. elapsedTimeInMillis / totalTimeInMillis 해주면 백분율이 나옴 (0.0~n.n 백분율)
         * 4. 추후 y축처럼 최소값 빼줘야 할 수 있음
         * */
        val startTimeInMillis = ChartDateUtils().getZeroSecMinDate(xValueList[0]) // 시작 시간, x축 최소값
        val currentTimeInMillis = value // 현재 시간

        val elapsedTimeInMillis = currentTimeInMillis - startTimeInMillis // 경과 시간을 계산
        val totalTimeInMillis =
            3600000 * xMaxCount // 총 시간을 밀리초로 계산합니다. 차트 맥스값 계산(1시간 * 24{차트 한화면 x갯수})
        val hourPercentage =
            elapsedTimeInMillis.toFloat() / totalTimeInMillis.toFloat() // 백분율을 계산 0~100으로 계산
        /*d("lineChart calHourPercentage index : ${index}")
        d("lineChart calHourPercentage startTimeInMillis : ${startTimeInMillis}")
        d("lineChart calHourPercentage currentTimeInMillis : ${currentTimeInMillis}")
        d("lineChart calHourPercentage elapsedTimeInMillis : ${startTimeInMillis}")
        d("lineChart calHourPercentage totalTimeInMillis : ${currentTimeInMillis}")
        d("lineChart calHourPercentage hourPercentage : ${hourPercentage}")*/
        return hourPercentage
    }

    /** bg 영역 타겟존 영역*/
    private fun drawTargetZone(canvas: Canvas) {
        /** 타겟존 계산*/
        targetZoneLowPoint = yPointCal(70)
        targetZoneHighPoint = yPointCal(140)
        val leftMargin = dpiToPixels(context, 53)
        canvas.drawRect(
            leftMargin, targetZoneHighPoint, canvasWidth, targetZoneLowPoint, targetZonePaint
        )
    }

    /** bg 텍스트 타겟존 영역*/
    private fun drawTargetZoneText(canvas: Canvas) {
        val leftMargin = dpiToPixels(context, 59)
        canvas.drawText("TARGET ZONE", leftMargin, yPointCal(120), targetZoneTextPaint)
    }

    private fun drawLine(canvas: Canvas) {
        curveLinePath.reset()
        curveLinePath.moveTo(xPointList[0], yPointList[0])
        for (index in 0 until xValueList.size) {
            curveLinePath.lineTo(xPointList[index], yPointList[index])
            /*이미지 좌표에 그리기*/
//            drawImage(canvas, testImage, xPointList[index], yPointList[index])
        }
        canvas.drawPath(curveLinePath, curveLinePaint)
    }


    /** 두직선 좌표로 교점 구해서 음식,운동 아이콘 그리기*/
    private fun drawFoodHealthImage(canvas: Canvas) {
        xFoodHealthPointList.forEachIndexed { index, value ->
            drawImage(canvas, index, xFoodHealthPointList[index], yFoodHealthPointList[index])
        }
    }

    /** 좌표에 이미지 그리기, 혈당, 음식, 운동*/
    private fun drawImage(
        canvas: Canvas, index: Int, xValuePoint: Float, yValuePoint: Float
    ) {
        if (xFoodHealthValueList.isEmpty()) return
        if (xFoodHealthValueList[index].healthType == "체중") return
        val drawImage = if (xFoodHealthValueList[index].healthType == "음식") {
            if (index == touchIndex && touchMode == "foodHealth") {
                dietSel
            } else {
                dietNor
            }

        } else {
            if (index == touchIndex && touchMode == "foodHealth") {
                exerciseSel
            } else {
                exerciseNor
            }
        }
        drawImage?.let { image ->
            val centerX = (xValuePoint - (image.width.toFloat() / 2f))
            val centerY = (yValuePoint - (image.height.toFloat() / 2f))
            canvas.drawBitmap(
                image, centerX, centerY, null
            )
        }
    }

    /** 마지막 좌표에 링 그리기*/
    private fun drawArcsRing(
        canvas: Canvas
    ) {
        val ovalSize = dpiToPixels(context, 14)

        val centerX = (xPointList.last() - (ovalSize / 2f))
        val centerY = (yPointList.last() - (ovalSize / 2f))

        if (yValueList.last() >= 140) {
            ringPaint.color = Color.RED
        }

        val oval = RectF(centerX, centerY, centerX + ovalSize, centerY + ovalSize)
        canvas.drawArc(oval, 0f, 360f, true, ringCenterPaint)
        canvas.drawArc(oval, 0f, 360f, true, ringPaint)
    }

    /** 말풍선 그리기 혈당*/
    private fun drawTextBubbleBlood(canvas: Canvas) {
        /** 말풍선 */
        val width = dpiToPixels(context, (72 / 2))
        val height = dpiToPixels(context, 51)
        val bottomMargin = dpiToPixels(context, 20)

        /** 실제 그려야할 말풍선 좌표값*/
        val realTopPoint = yPointList[touchIndex] - (height + bottomMargin)
        val realBottomPoint = yPointList[touchIndex] - bottomMargin

        /** top 좌표가 0보다 작으면(화면 밖으로 그리기 때문에)
         * top,bottom 을 다시 계산
         * 0보다 작은 경우 => 절대값 real top 좌표값을 + 해준다 => 최대 0으로 좌표가 변경됨*/
        val calTopPoint = if (realTopPoint > 0f) realTopPoint else 0f
        val calBottomPoint =
            if (realTopPoint > 0f) realBottomPoint else (realBottomPoint + abs(realTopPoint))
        val rectF = RectF(
            xPointList[touchIndex] - width,
            calTopPoint,
            xPointList[touchIndex] + width,
            calBottomPoint,
        )

        val radius = dpiToPixels(context, 8)

        canvas.drawRoundRect(rectF, radius, radius, balloonPaint)

        /** 화살표 */
        val arrowPosition = (rectF.left + rectF.right) / 2f
        val arrowWidth = dpiToPixels(context, 10)
        val arrowHeight = dpiToPixels(context, 10)

        val arrowPath = Path().apply {
            moveTo(arrowPosition, rectF.bottom + arrowHeight)
            lineTo(arrowPosition - arrowWidth / 2f, rectF.bottom)
            lineTo(arrowPosition + arrowWidth / 2f, rectF.bottom)
            close()
        }
        canvas.drawPath(arrowPath, balloonPaint)

        /** 텍스트 */
        val textValue = yValueList[touchIndex].toString()
        val textUnit = "mg/dL"
        val textTime = ChartDateUtils().getHHMMaGetTimestamp(xValueList[touchIndex])
        textPaint.textAlign = Paint.Align.RIGHT

        val bounds = Rect()
        textPaint.getTextBounds("100", 0, 3, bounds)
        val textBoundsHeight = bounds.height()
        val textBoundsWidth = bounds.width()

        canvas.drawText(
            textValue, rectF.left + width, rectF.top + rectF.height() / 2, textPaint
        )
        canvas.drawText(
            textUnit,
            rectF.left + width + dpiToPixels(context, 2),
            rectF.top + rectF.height() / 2,
            unitTextPaint
        )
        canvas.drawText(
            textTime,
            rectF.left + width,
            rectF.top + +rectF.height() / 2 + textBoundsHeight,
            timeTextPaint
        )
    }

    /** 말풍선 그리기 음식 운동*/
    private fun drawTextBubbleFoodHealth(canvas: Canvas) {
        if (xFoodHealthValueList.isEmpty()) return
        if (xFoodHealthValueList[touchIndex].healthType == "체중") return
        /** healthType에 따라 다른 텍스트 값 */
        val text = if (xFoodHealthValueList[touchIndex].healthType == "음식") {
            xFoodHealthValueList[touchIndex].detailInfo
        } else {
            xFoodHealthValueList[touchIndex].detailType
        }
        val textTime = ChartDateUtils().getHHMMaGetTimestamp(xFoodHealthTimeList[touchIndex])
        textPaint.textAlign = Paint.Align.CENTER

        /** 텍스트 자르기*/
        val maxLength = 14
        val textValue = ChartEngineUtils().ellipsizeText(text, maxLength)

        /** 텍스트*/
        val bounds = Rect()
        textValue.let { textPaint.getTextBounds(textValue, 0, textValue.length, bounds) }
        d("logTest textValue.length : ${textValue.length}")
        val textBoundsHeight = bounds.height()
        val textBoundsWidth = bounds.width()

        /** 말풍선 */
        val width = (textBoundsWidth / 2) + dpiToPixels(context, 10)//dpiToPixels(context, (72/2))
        val height = dpiToPixels(context, 51)
        val bottomMargin = dpiToPixels(context, 24)

        val rectF = RectF(
            xFoodHealthPointList[touchIndex] - width,
            yFoodHealthPointList[touchIndex] - (height + bottomMargin),
            xFoodHealthPointList[touchIndex] + width,
            yFoodHealthPointList[touchIndex] - bottomMargin
        )
        val radius = dpiToPixels(context, 10)

        canvas.drawRoundRect(rectF, radius, radius, balloonPaint)

        /** 화살표 */
        val arrowPosition = (rectF.left + rectF.right) / 2f
        val arrowWidth = dpiToPixels(context, 10)
        val arrowHeight = dpiToPixels(context, 10)

        val arrowPath = Path().apply {
            moveTo(arrowPosition, rectF.bottom + arrowHeight)
            lineTo(arrowPosition - arrowWidth / 2f, rectF.bottom)
            lineTo(arrowPosition + arrowWidth / 2f, rectF.bottom)
            close()
        }
        canvas.drawPath(arrowPath, balloonPaint)

//        d("drawTextBubbleFoodHealth xfoodHealthValueList : ${xFoodHealthValueList}")
//        d("drawTextBubbleFoodHealth touchIndex : ${touchIndex}")
//        d("drawTextBubbleFoodHealth chartGetTimestampTimeMin : ${ChartDateUtils().chartGetTimestampTimeMin(xFoodHealthValueList[touchIndex])}")
        /** 텍스트 */
        canvas.drawText(
            textValue, rectF.left + width, rectF.top + rectF.height() / 2, textPaint
        )
        canvas.drawText(
            textTime,
            rectF.left + width,
            rectF.top + +rectF.height() / 2 + textBoundsHeight,
            timeTextPaint
        )
    }

/*@@@@@@@@@@@@@@@@@@@@@@@@ 터치 이벤트 */
    /** 터치 이벤트*/
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (xValueList.isEmpty()) return false
        if (event?.action == MotionEvent.ACTION_UP) {
            this.parent.requestDisallowInterceptTouchEvent(false)
            /*d("lineChart ACTION_UP x : " + event.x)
            d("lineChart ACTION_UP y : " + event.y)*/
            touchActionUpEvent(event.x, event.y)
        }
        if (event?.action == MotionEvent.ACTION_DOWN) {
        }

        if (event?.action == MotionEvent.ACTION_MOVE) {
        }
        return true
    }

    /** 터치는 업 이벤트
     * point list범위로
     * 1. event.x - scrollByX 해줘서 스크롤 했을때 x값 계산*/
    private fun touchActionUpEvent(touchX: Float, touchY: Float) {
        val currentTouch = touchX
//        d("lineChart Touch touchEvent touchX : " + touchX)
//        d("lineChart Touch touchEvent currentTouch : " + currentTouch)
        val clickPadding = 25f
        val clickPaddingY = 200f
        for (index in 0 until xPointList.size) {
            val xValuePoint = xPointList[index]
            val yValuePoint = yPointList[index]
            if (currentTouch >= (xValuePoint - clickPadding) && currentTouch <= (xValuePoint + clickPadding) && touchY >= (yValuePoint - clickPaddingY) && touchY <= (yValuePoint + clickPaddingY)) {
                touchMode = "blood"
                touchIndex = index
                postInvalidateOnAnimation()
            }
        }
        for (index in 0 until xFoodHealthPointList.size) {
            val xValuePoint = xFoodHealthPointList[index]
            val yValuePoint = yFoodHealthPointList[index]
            if (currentTouch >= (xValuePoint - clickPadding) && currentTouch <= (xValuePoint + clickPadding) && touchY >= (yValuePoint - clickPaddingY) && touchY <= (yValuePoint + clickPaddingY)) {
                touchMode = "foodHealth"
                touchIndex = index
                postInvalidateOnAnimation()
            }
        }
    }


/*@@@@@@@@@@@@@@@@@@@@@@@@ 터치 이벤트 */
    /** 두직선 교차점 구하는 공식*/
    private fun getIntersection(
        x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float, x4: Float, y4: Float
    ): Pair<Float, Float>? {
        val det = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4)
        if (det == 0f) return null // 두 직선이 평행이므로 교점이 없음

        val dx = (x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4)
        val dy = (x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)
        return Pair(dx / det, dy / det)
    }

    /** 리스트에서 특정 값보다 작은 수중에 근접 값 찾는 공식 */
    private fun findClosestSmaller(list: ArrayList<Long>, target: Long): Int {
//       테스트 val list = listOf(10, 20, 30, 40, 50)
        var closest: Long? = null
        var closestIndex: Int? = null
        list.forEachIndexed { index, num ->
            if (num <= target && (closest == null || target - num < target - closest!!)) {
                closest = num
                closestIndex = index
//                d("findClosestSmaller num : ${num}")
//                d("findClosestSmaller index : ${index}")
            }
        }
//        for (num in list) {
//            if (num < target && (closest == null || target - num < target - closest)) {
//                closest = num
//            }
//        }
        return closestIndex ?: 0
    }
}