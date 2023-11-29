package com.osl.base.project.main.utils.chart.linenotscroll

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.OverScroller
import androidx.core.view.ViewCompat
import com.osl.base.project.main.R
import com.osl.base.project.main.utils.chart.ChartEngineUtils
import com.osl.base.project.main.utils.date.ChartDateUtils
import com.osl.base.project.main.utils.dpiToPixels
import com.osl.base.project.main.views.chart.ChartViewModel
import timber.log.Timber.Forest.d


class ChartLineNotScrollDraw(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {
    private var touchIndex: Int = 0
    private lateinit var mScroller: OverScroller
    private var mGestureDetector: GestureDetector? = null
    private var translateMatrix: Matrix
    private var scrollByX: Float = 0f
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
    private var testImage: Bitmap? = null

    /** list*/
    var xValueList: ArrayList<Long> = arrayListOf()
    var yValueList: ArrayList<Int> = arrayListOf()
    var xPointList: ArrayList<Float> = arrayListOf()
    var yPointList: ArrayList<Float> = arrayListOf()

    /** 음식, 운동 리스트 */
    var xFoodHealthValueList: ArrayList<Long> =
        arrayListOf(1682434800877, 1682438400877)
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

    /** 마지막 누른 화면 값*/
    var lastDownX = 0f

    /** 타겟존 좌표*/
    private var targetZoneLowPoint: Float = 0f
    private var targetZoneHighPoint: Float = 0f

    /** 터치시 말풍선 모드*/
    private var touchMode: String = ""//blood,foodHealth

    private val highLineColor = Color.parseColor("#AB3452")
    private val lowLineColor = Color.parseColor("#5480F7")
    private val targetZoneLineColor = Color.parseColor("#0D5480F7")
    private val textColor = Color.parseColor("#616161")

    init {
        curveLinePaint.apply {
            color = lowLineColor
            strokeWidth = dpiToPixels(context!!, 4)
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            isAntiAlias = true
        }

        testImage = BitmapFactory.decodeResource(context?.resources, R.drawable.icon_discover)
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
        }
        unitTextPaint.apply {
            color = Color.BLACK
            textSize = dpiToPixels(context!!, 8)
            isAntiAlias = true
            textAlign = Paint.Align.LEFT
        }
        timeTextPaint.apply {
            color = Color.BLACK
            textSize = dpiToPixels(context!!, 8)
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val viewHeightMargin = dpiToPixels(context, 40)
        canvasWidth = width.toFloat()
        canvasHeight = height.toFloat() - viewHeightMargin

        mGestureDetector = GestureDetector(context, mOnSimpleOnGestureListener)
        mScroller = OverScroller(context)

        preCalculated()
        if (xValueList.isEmpty()) return
        moveLastScroller()
    }

    /*스크롤 맨끝으로 이동 시키는 함수*/
    private fun moveLastScroller() {
        /** 마지막 좌표에 여백 추가*/
        val addLastMargin = dpiToPixels(context, 30)
        xPointList.last().let { lastValue ->
            val lastWidthPoint = -(lastValue - canvasWidth + addLastMargin)
            scrollByX = lastWidthPoint
            ViewCompat.postInvalidateOnAnimation(this)
//            d("lineChart initScrollLocation lastWidthPoint : ${lastWidthPoint}")
        }
    }

    private fun scrollerCal(){
        d("scrollerCal xPointList : ${xPointList}")
        d("scrollerCal xPointList : ${canvasWidth}")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (xValueList.isEmpty()) return
        d("lineChart onDraw width : ${width}")
        d("lineChart onDraw height : ${height}")
        drawTargetZone(canvas)
        drawTargetZoneText(canvas)
        drawTranslate(canvas)
        d("lineChart onDraw scrollByX : ${scrollByX}")
    }

    private fun drawTranslate(canvas: Canvas) {
        /** clipRect left 마진*/
        val leftMargin = dpiToPixels(context, 53)
        canvas.save()
//        canvas.clipRect(leftMargin, 0f, xPointList.last(), canvas.height.toFloat())
        translateMatrix.setTranslate(scrollByX, 0f)
        canvas.setMatrix(translateMatrix)
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
        drawFoodHealthImage(canvas)
        drawArcsRing(canvas)
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
        xValueList.forEachIndexed { index, value ->
            val xValuePoint = xPointCal(value)
            d("lineChart curveLinePreDraw x : index : ${index} ,xValuePoint : ${xValuePoint}")
            xPointList.add(xValuePoint)
        }

        yValueList.forEachIndexed { index, value ->
            val yValuePoint = yPointCal(value)
            d("lineChart curveLinePreDraw y : index : ${index} ,yValuePoint : ${yValuePoint}")
            yPointList.add(yValuePoint)
        }
        d("lineChart curveLinePreDraw point : xPointList : ${xPointList}")
        d("lineChart curveLinePreDraw point : yPointList : ${yPointList}")

        /** 타겟존 계산*/
        targetZoneLowPoint = yPointCal(70)
        targetZoneHighPoint = yPointCal(140)

        preDrawFoodHealthImage()
    }

    /** draw image 미리 그리기*/
    private fun preDrawFoodHealthImage(){
        if (xFoodHealthValueList.isEmpty()) return
        d("drawFoodHealthImage xValueList = : ${xValueList}")
        xFoodHealthValueList.forEachIndexed { index, value ->
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

            val xPoint = xFoodValuePoint//intersection?.first ?: 0f
            val yPoint = intersection?.second ?: 0f//intersection ?: 0f
            xFoodHealthPointList.add(xPoint)
            yFoodHealthPointList.add(yPoint)
            d("drawFoodHealthImage first = : ${xPoint}")
            d("drawFoodHealthImage second = : ${yPoint}")
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
        val leftMarginInterval = dpiToPixels(context, (35))
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
        val leftMargin = dpiToPixels(context, 53)
        canvas.drawRect(
            leftMargin,
            targetZoneHighPoint,
            canvasWidth,
            targetZoneLowPoint,
            targetZonePaint
        )
    }

    /** bg 텍스트 타겟존 영역*/
    private fun drawTargetZoneText(canvas: Canvas) {
        val leftMargin = dpiToPixels(context, 59)
        canvas.drawText("TARGET ZONE", leftMargin, yPointCal(120), targetZoneTextPaint)
    }

    private fun drawLine(canvas: Canvas) {
        curveLinePath.reset()
        curveLinePath.moveTo(0f, yPointList[0])
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
        testImage?.let { image ->
            val centerX = (xValuePoint - (image.width.toFloat() / 2f))
            val centerY = (yValuePoint - (image.height.toFloat() / 2f))
            canvas.drawBitmap(
                image,
                centerX,
                centerY,
                null
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
        val width = dpiToPixels(context, 40)
        val height = dpiToPixels(context, 40)
        val bottomMargin = dpiToPixels(context, 20)

        val rectF = RectF(
            xPointList[touchIndex] - width,
            yPointList[touchIndex] - (height + bottomMargin),
            xPointList[touchIndex] + width,
            yPointList[touchIndex] - bottomMargin
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

        /** 텍스트 */
        val textValue = yValueList[touchIndex].toString()
        val textUnit = "mg/dl"
        val textTime = ChartDateUtils().chartGetTimestampTimeMin(xValueList[touchIndex])
        textPaint.textAlign = Paint.Align.RIGHT
        val bounds = Rect()
        textPaint.getTextBounds("100", 0, 3, bounds)
        val textBoundsHeight = bounds.height()
        val textBoundsWidth = bounds.width()

        canvas.drawText(
            textValue,
            rectF.left + width,
            rectF.top + rectF.height() / 2,
            textPaint
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
        /** 말풍선 */
        val width = dpiToPixels(context, 40)
        val height = dpiToPixels(context, 40)
        val bottomMargin = dpiToPixels(context, 20)

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
        val text = "음식 음식 음식 음식 음식 음식 음식 음식"
        val textTime = ChartDateUtils().chartGetTimestampTimeMin(xFoodHealthValueList[touchIndex])
        textPaint.textAlign = Paint.Align.CENTER

        val maxLength = 14
        val textValue = ellipsizeText(text, maxLength)

        val bounds = Rect()
        textPaint.getTextBounds("100", 0, 3, bounds)
        val textBoundsHeight = bounds.height()

        canvas.drawText(
            textValue,
            rectF.left + width,
            rectF.top + rectF.height() / 2,
            textPaint
        )
        canvas.drawText(
            textTime,
            rectF.left + width,
            rectF.top + +rectF.height() / 2 + textBoundsHeight,
            timeTextPaint
        )
    }

    /** 글자 자르기*/
    fun ellipsizeText(originalText: String, maxLength: Int): String {
        if (originalText.length <= maxLength) {
            return originalText
        }
        return originalText.substring(0, maxLength) + "..."
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
            this.parent.requestDisallowInterceptTouchEvent(true)
            /** down x값과 이동한 scroll 값을 빼줘서 선택한 현재화면의 down값을 계산함(스크롤값이 -라서 빼줌) */
            mScroller.forceFinished(true)
            lastDownX = (event.x - scrollByX)
            /*d("lineChart ACTION_DOWN x : " + event.x)
            d("lineChart ACTION_DOWN y : " + event.y)
            d("lineChart ACTION_DOWN scrollByX : " + scrollByX)
            d("lineChart ACTION_DOWN startX : " + lastDownX)*/
        }

        if (event?.action == MotionEvent.ACTION_MOVE) {
            /*d("lineChart ACTION_MOVE x : " + event.x)
            d("lineChart ACTION_MOVE y : " + event.y)*/
            touchActionMoveEvent((event.x), event.y)
        }
        event?.let { mGestureDetector?.onTouchEvent(it) }

        return true
    }

    /** 터치는 업 이벤트
     * point list범위로
     * 1. event.x - scrollByX 해줘서 스크롤 했을때 x값 계산*/
    private fun touchActionUpEvent(touchX: Float, touchY: Float) {
        val currentTouch = touchX - scrollByX
//        d("lineChart Touch touchEvent touchX : " + touchX)
//        d("lineChart Touch touchEvent currentTouch : " + currentTouch)
        val clickPadding = 50f
        for (index in 0 until xValueList.size) {
            val xValuePoint = xPointList[index]
            val yValuePoint = yPointList[index]
            if (currentTouch >= (xValuePoint - clickPadding) &&
                currentTouch <= (xValuePoint + clickPadding) &&
                touchY >= (yValuePoint - clickPadding) &&
                touchY <= (yValuePoint + clickPadding)
            ) {
                touchMode = "blood"
                touchIndex = index
                postInvalidateOnAnimation()
            }
        }
        for (index in 0 until xFoodHealthValueList.size) {
            val xValuePoint = xFoodHealthPointList[index]
            val yValuePoint = yFoodHealthPointList[index]
            if (currentTouch >= (xValuePoint - clickPadding) &&
                currentTouch <= (xValuePoint + clickPadding) &&
                touchY >= (yValuePoint - clickPadding) &&
                touchY <= (yValuePoint + clickPadding)
            ) {
                touchMode = "foodHealth"
                touchIndex = index
                postInvalidateOnAnimation()
            }
        }
    }

    /** 터치 드래그 이벤트*/
    private fun touchActionMoveEvent(touchX: Float, touchY: Float) {
        d("lineChart touchActionMoveEvent touchX : " + touchX)
        d("lineChart touchActionMoveEvent startX : " + lastDownX)
        d("lineChart touchActionMoveEvent scrollByX : " + scrollByX)
        d("lineChart touchActionMoveEvent xValueList.last() : " + xPointList.last())
        /** move x값과 위에서 계산한 lastDownX값을 -해서 현재 스크롤 값으로 계산  */
        scrollByX = touchX - lastDownX
        /** 스크롤 값이 0 보다 큰경우 스크롤값 초기화 시킴*/
        if (scrollByX > 0) {
            scrollByX = 0f
        }

        /** 마지막 좌표에 여백 추가*/
        val addLastMargin = dpiToPixels(context, 30)
        /** 마지막 좌표 - width*/
        xPointList.last().let { lastValue ->
            val lastWidthPoint = -(lastValue - canvasWidth + addLastMargin)
            if (scrollByX < lastWidthPoint) {
                scrollByX = lastWidthPoint
            }
        }

        ViewCompat.postInvalidateOnAnimation(this)
    }

    /** 심플 제스처 리스너 생성*/
    private val mOnSimpleOnGestureListener: GestureDetector.SimpleOnGestureListener =
        object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(
                e1: MotionEvent,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                /** 움직인 x - 기존 x = 이동할 거리 값*/
                val distanceX = e2.x - e1.x

                d("lineChart onFling distanceX : ${distanceX}")
                mScroller.startScroll(scrollByX.toInt(), 0, distanceX.toInt(), 0, 500)

                return super.onFling(e1, e2, velocityX, velocityY)
            }
        }

    /** 스크롤 이벤트 들어오는 함수
     * currVelocity를 사용해야 들어옴
     * */
    override fun computeScroll() {
        /*d("lineChart computeScroll getCurrX(): ${mScroller.currX} ")
        d("lineChart computeScroll currVelocity(): ${mScroller.currVelocity} ")
        d("lineChart computeScroll scrollByX(): ${scrollByX} ")
        d("lineChart computeScroll computeScrollOffset: ${mScroller.computeScrollOffset()} ")*/
        if (mScroller.computeScrollOffset()) { //현재 스크롤 상태를 업데이트 한다.
            val currX: Int = mScroller.currX //현재 x값
            val currY: Int = mScroller.currY //현재 y값
            val currV: Float = mScroller.currVelocity //현재 속도값(받아오지 않으면 속도가 바뀌지 않는다)
//            d("lineChart computeScroll currX : ${currX} ")
//            d("lineChart computeScroll currY : ${currY} ")
//            d("lineChart computeScroll currV : ${currV} ")
//            d("lineChart computeScroll scrollByX(): ${scrollByX} ")

            /**이부분에서 animation을 실행하거나 view의 상태를 바꿔준다.*/
            scrollByX = currX.toFloat()

            /** ACTION_MOVE와 같은 체크함수*/
            if (scrollByX > 0) {
                scrollByX = 0f
            }

            /** 마지막 좌표에 여백 추가*/
            val addLastMargin = dpiToPixels(context, 30)
            /** 마지막 좌표 - width*/
            xPointList.last().let { lastValue ->
                val lastWidthPoint = -(lastValue - canvasWidth + addLastMargin)
                if (scrollByX < lastWidthPoint) {
                    scrollByX = lastWidthPoint
                }
            }
            ViewCompat.postInvalidateOnAnimation(this)
        }
        super.computeScroll()
    }
/*@@@@@@@@@@@@@@@@@@@@@@@@ 터치 이벤트 */
    /** 두직선 교차점 구하는 공식*/
    private fun getIntersection(
        x1: Float, y1: Float, x2: Float, y2: Float,
        x3: Float, y3: Float, x4: Float, y4: Float
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
            if (num < target && (closest == null || target - num < target - closest!!)) {
                closest = num
                closestIndex = index
                /*d("findClosestSmaller num : ${num}")
                d("findClosestSmaller index : ${index}")*/
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