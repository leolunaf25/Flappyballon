package com.lunatcoms.flappyballon

import android.graphics.Bitmap
import android.graphics.Canvas

class Character(
    private val characterImage: Bitmap,
    var x: Float,
    var y: Float,
    private val groundLevel: Float,
    private val rooftLevel: Float
) {

    var velocityY = 0
    private val gravity = 2
    var jumForce = -40


    fun update() {
        velocityY += gravity
        y += velocityY

        if (y < rooftLevel){
            y = rooftLevel
            velocityY = 0
        }

        if (y > groundLevel){
            y = groundLevel
            velocityY = 0
        }

    }

    fun jump(){
        velocityY = jumForce
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(characterImage, x, y, null)
    }

}