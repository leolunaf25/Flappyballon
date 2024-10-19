package com.lunatcoms.flappyballon

import android.graphics.Bitmap

class Background(var image: Bitmap, var x:Float, var y:Float) {
    private val speed = 5

    fun update(){
        x -= speed
    }

}