package com.lunatcoms.flappyballon

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.SurfaceView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameView(context: Context, attrs: AttributeSet) : SurfaceView(context, attrs) {

    private val gameScope = CoroutineScope(Dispatchers.Default)

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

    private suspend fun runGameLoop() {
        while (isPlaying) {
            update()
            draw()
            delay(17L)
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

    fun resume() {
        isPlaying = true
        gameScope.launch {
            runGameLoop() // Lanzar el ciclo del juego en una corrutina
        }
    }

    fun pause() {
        isPlaying = false
        gameScope.coroutineContext.cancelChildren() // Cancelar todas las corrutinas del scope
    }

}