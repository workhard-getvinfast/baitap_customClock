package com.example.customviewclock

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import java.util.*

class MyClockView: View, Runnable {
    private var clockRadius:Float = 60f
    private var brandName:String = "Gucci"
    private var clockWiseColor:Int = Color.RED
    private var clockWiseSize:Float = 5f
    private var backColor:Int = Color.YELLOW
    private var borderWidth:Float = 10f
    private var mainColor:Int = Color.BLACK
    private var brandSize:Float = 5f
    private var circleCenterRadius:Float = 10f
    private var handlerTime = Handler(Looper.getMainLooper())
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)


    constructor(context: Context?) : super(context){
        inits()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        getAttrs(attrs!!, context!!)
        inits()
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        getAttrs(attrs!!, context!!)
        inits()
    }

    private fun inits() {
        handlerTime.postDelayed(this, 60000)
    }

    override fun run() {
        invalidate()
        handlerTime.postDelayed(this, 60000)
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
    private fun getAttrs(attrs: AttributeSet, context: Context){
        val type = context.obtainStyledAttributes(attrs, R.styleable.MyClockView)
        clockRadius = type.getDimensionPixelSize(R.styleable.MyClockView_clockRadius, width).toFloat()
        borderWidth = type.getDimensionPixelSize(R.styleable.MyClockView_borderWidth, width).toFloat()
        circleCenterRadius = type.getDimensionPixelSize(R.styleable.MyClockView_circleCenterRadius, 20).toFloat()
        clockWiseSize = type.getDimensionPixelSize(R.styleable.MyClockView_clockWiseSize, width).toFloat()
        brandSize = type.getDimensionPixelSize(R.styleable.MyClockView_borderWidth, width).toFloat()
        brandName = type.getString(R.styleable.MyClockView_brandName).toString()
        clockWiseColor = type.getColor(R.styleable.MyClockView_clockWiseColor, clockWiseColor)
        backColor = type.getColor(R.styleable.MyClockView_backColor, backColor)
        mainColor = type.getColor(R.styleable.MyClockView_mainColor, mainColor)
        type.recycle()
    }
    override fun onDetachedFromWindow() {
        handlerTime.removeCallbacks(this)
        super.onDetachedFromWindow()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val currentTime = Calendar.getInstance()
        drawBorder(canvas)
        drawBack(canvas)
        drawBrand(canvas)
        drawDate(canvas, currentTime)
        drawClockWise(canvas, currentTime)
        drawCenterCircle(canvas)
    }

    private fun drawBorder(canvas: Canvas?){
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.STROKE
        paint.color = backColor
        paint.strokeWidth = borderWidth
        canvas?.drawCircle(width.toFloat() / 2, height.toFloat() / 2, clockRadius / 2, paint)
    }
    private fun drawBack(canvas: Canvas?){
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.FILL
        paint.color = mainColor
        canvas?.drawCircle(width.toFloat() / 2, height.toFloat() / 2, clockRadius / 2 - borderWidth / 2, paint)
    }
    private fun drawDate(canvas: Canvas?, rightNow: Calendar) {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        val rect = Rect()
        paint.color = Color.WHITE

        val date = rightNow.get(Calendar.DATE).toString()
        paint.textSize = brandSize * 2f / 3f
        paint.getTextBounds(date, 0, date.length, rect)
        val dateLocationX:Float = (width - rect.width())/2f
        val dateLocationY:Float = 9f*clockRadius/10f
        //drawBackgroundDate(canvas, date)
        canvas?.drawText(date, dateLocationX, dateLocationY, paint)
    }

    private fun drawBackgroundDate(canvas: Canvas?, date: String){
        val backgroundRect = Rect()
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
        paint.getTextBounds(date, 0, date.length, backgroundRect)
        canvas?.drawRect(100f, 100f, 100f, 100f, paint)
    }

    private fun drawBrand(canvas: Canvas?) {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        var rect = Rect()
        paint.color = backColor
        paint.textSize = brandSize
        paint.getTextBounds(brandName, 0, brandName.length, rect)
        val brandLocationX:Int = (width - rect.width())/2
        canvas?.drawText(brandName, brandLocationX.toFloat(), clockRadius / 8f + borderWidth, paint)
    }
    private fun drawClockWise(canvas: Canvas?, rightNow: Calendar){
        val hour = rightNow.get(Calendar.HOUR)
        val minute = rightNow.get(Calendar.MINUTE)
        val unitOfHourRotate:Float = (hour*60 + minute)/720f - 90f
        val unitOfMinuteRotate:Float = minute*6f
        drawHourHand(canvas, unitOfHourRotate)
        drawMinuteHand(canvas, unitOfMinuteRotate)

    }
    private fun drawHourHand(canvas: Canvas?, radiusRotate: Float){
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = clockWiseColor
        paint.style = Paint.Style.FILL
        canvas?.rotate(radiusRotate, width/2f, height/2f )
        canvas?.drawRect(width / 2f - clockWiseSize / 2 - 40f, height / 2f - clockWiseSize / 2, width / 2f + clockRadius / 4f, height / 2f + clockWiseSize / 2, paint)
    }
    private fun drawMinuteHand(canvas: Canvas?, radiusRotate: Float){
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = clockWiseColor
        paint.style = Paint.Style.FILL
        canvas?.rotate(radiusRotate, width/2f, height/2f )
        canvas?.drawRect(width / 2f - clockWiseSize / 2 - 40f, height / 2f - clockWiseSize / 2, width / 2f + 2f * clockRadius / 5f, height / 2f + clockWiseSize / 2, paint)
    }
    private fun drawCenterCircle(canvas: Canvas?){
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
        canvas?.drawCircle(width/2f, height/2f, circleCenterRadius,paint)

        paint.color = Color.BLACK
        canvas?.drawCircle(width/2f, height/2f, 2f*circleCenterRadius/3f,paint)
    }

}