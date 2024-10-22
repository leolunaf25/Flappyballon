package com.lunatcoms.flappyballon

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameView(context: Context, attrs: AttributeSet) : SurfaceView(context, attrs) {

    private val gameScope = CoroutineScope(Dispatchers.Default)

    private var isPlaying = false
    private var isGameOver = false

    private var background1: Background
    private var background2: Background

    private var character: Character
    private var obstacles: MutableList<Obstacle> = mutableListOf()


    init {
        val displayMetrics = context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels.toFloat()
        val screenHeight = displayMetrics.heightPixels.toFloat()

        //Create Background
        val bitmap = BitmapFactory.decodeResource(resources,R.drawable.background)
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap,bitmap.width,screenHeight.toInt(),false)
        background1 = Background(scaledBitmap,0F,0F)
        background2 = Background(scaledBitmap,background1.image.width.toFloat(),0F)

        //Create Character
        val characterBitmap = BitmapFactory.decodeResource(resources,R.drawable.ballon)
        val scaledCharacter = Bitmap.createScaledBitmap(characterBitmap, screenWidth.toInt()/8,screenHeight.toInt()/12,false)

        character = Character(scaledCharacter,screenWidth/3F,screenHeight/3F,screenHeight-(scaledCharacter.height*1.7).toFloat(),0F)

        // Inicialización de obstáculos (igual que antes)
        val topObstacleImage = BitmapFactory.decodeResource(resources, R.drawable.pipe_top)
        val bottomObstacleImage = BitmapFactory.decodeResource(resources, R.drawable.pipe_bottom)

        val gapHeight = Random.nextInt(screenHeight.toInt()/5, screenHeight.toInt()/3)

        val scaledTopObstacle = Bitmap.createScaledBitmap(topObstacleImage,topObstacleImage.width,(screenHeight*.6).toInt(),false)
        val scaledBottomObstacle = Bitmap.createScaledBitmap(bottomObstacleImage,bottomObstacleImage.width,(screenHeight*.6).toInt(),false)

        obstacles.add(Obstacle(screenWidth,screenHeight.toInt(),gapHeight,screenWidth,scaledTopObstacle,scaledBottomObstacle))
        obstacles.add(Obstacle(screenWidth,screenHeight.toInt(),gapHeight,screenWidth+(screenWidth*0.55F),scaledTopObstacle,scaledBottomObstacle))

    }

    private suspend fun runGameLoop() {
        while (isPlaying) {
            update()
            draw()
            delay(17L)
        }
    }

    private fun update(){

        if (isGameOver){
            character.update()
            (context as GameActivity).showScoreBoard()
            return
        }

        background1.update()
        background2.update()

        if (background1.x + background1.image.width <= 0){
            background1.x = background2.x + background2.image.width
        }
        if (background2.x + background2.image.width <= 0){
            background2.x = background1.x + background1.image.width
        }

        character.update()

        //Obstacles
        for (obstacle in obstacles){
            obstacle.update()

            if (obstacle.checkCollision(character)){
                isGameOver = true
                character.velocityY = 0
                character.jumForce = 0
            }
            if (obstacle.checkScore(character) && !obstacle.hasBeenPassed){
                (context as GameActivity).updateScore()
                obstacle.checkPassed()
            }
        }
    }

    private fun draw(){
        if (holder.surface.isValid){
            val canvas: Canvas = holder.lockCanvas()

            canvas.drawBitmap(background1.image, background1.x, background1.y,null)
            canvas.drawBitmap(background2.image, background2.x, background2.y,null)

            for (obstacle in obstacles){
                obstacle.draw(canvas)
            }

            if (!isGameOver){
                character.draw(canvas)
            }

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

    override fun onTouchEvent(event: MotionEvent?): Boolean{
        if (event?.action == MotionEvent.ACTION_DOWN){
            character.jump()
        }
        return true
    }

}