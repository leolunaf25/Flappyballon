package com.lunatcoms.flappyballon

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.SurfaceView

class GameView(context: Context, attrs: AttributeSet) : SurfaceView(context, attrs), Runnable {

    private var thread: Thread? = null
    private var isPlaying = false

    private var background1: Background
    private var background2: Background


    init {
        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels.toFloat()
        val screenHeight = displayMetrics.heightPixels.toFloat()

        val bitmap = BitmapFactory.decodeResource(resources,R.drawable.background)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap,bitmap.width,screenHeight.toInt(),false)

        background1 = Background(scaledBitmap,0F,0F)
        background2 = Background(scaledBitmap,background1.image.width.toFloat(),0F)
    }

    override fun run() {
        while (isPlaying) {
            update()
            draw()
            sleep()
        }
    }

    private fun update(){

        background1.update()
        background2.update()

        if (background1.x + background1.image.width <= 0){
            background1.x = background2.x + background2.image.width
        }
        if (background2.x + background2.image.width <= 0){
            background2.x = background1.x + background1.image.width
        }
    }

    private fun draw(){
        if (holder.surface.isValid){
            val canvas: Canvas = holder.lockCanvas()

            canvas.drawBitmap(background1.image, background1.x, background1.y,null)
            canvas.drawBitmap(background2.image, background2.x, background2.y,null)

            holder.unlockCanvasAndPost(canvas)

        }
    }

    private fun sleep() {
        Thread.sleep(17)
    }

    fun resume() {
        isPlaying = true
        thread = Thread(this)
        thread?.start()
    }

    fun pause() {
        isPlaying = false
        try {
            thread?.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

}