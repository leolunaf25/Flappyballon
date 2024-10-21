package com.lunatcoms.flappyballon

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import kotlin.random.Random

class Obstacle(
    private val screenWidth: Float,
    private val screenHeight: Int,
    private var gapHeight: Int,
    private var xPosition: Float,
    private val topObstacleImage: Bitmap,
    private val bottomObstacleImage: Bitmap
) {

    private val obstacleWidth: Int = topObstacleImage.width
    private val obstacleHeight: Int = topObstacleImage.height

    private var topObstacleY: Int // Posición Y del obstáculo superior
    private var bottomObstacleY: Int // Posición Y del obstáculo inferior

    init {
        topObstacleY =
            Random.nextInt(-obstacleHeight + (screenHeight - obstacleHeight - gapHeight), 0)
        bottomObstacleY = topObstacleY + obstacleHeight + gapHeight
    }

    fun update() {
        gapHeight = Random.nextInt(screenHeight / 5, screenHeight / 4)

        xPosition -= 10

        // Reposicionar el obstáculo si sale de la pantalla
        if (xPosition + obstacleWidth < 0) {
            xPosition = screenWidth
            // Generar una nueva posición Y para el obstáculo superior
            topObstacleY =
                Random.nextInt(-obstacleHeight + (screenHeight - obstacleHeight - gapHeight), 0)
            bottomObstacleY = topObstacleY + obstacleHeight + gapHeight
        }
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(topObstacleImage, xPosition, topObstacleY.toFloat(), null)
        canvas.drawBitmap(bottomObstacleImage, xPosition, bottomObstacleY.toFloat(), null)
    }

    fun checkCollision(character: Character):Boolean{
        val characterRect = Rect(character.x.toInt(), character.y.toInt(),character.x.toInt() + character.characterImage.width, character.y.toInt()+character.characterImage.height)
        val topRect = Rect(xPosition.toInt(),topObstacleY,xPosition.toInt()+obstacleWidth, topObstacleY+obstacleHeight)
        val bottomRect = Rect(xPosition.toInt(),bottomObstacleY,xPosition.toInt()+obstacleWidth,bottomObstacleY+obstacleHeight)
        return Rect.intersects(characterRect,topRect) || Rect.intersects(characterRect,bottomRect)
    }

}