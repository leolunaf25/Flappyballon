package com.lunatcoms.flappyballon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lunatcoms.flappyballon.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding

    private lateinit var gameView: GameView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gameView = findViewById<GameView>(R.id.gameView)
    }

    override fun onResume() {
        super.onResume()
        gameView.resume()  // Iniciar o reanudar el juego
    }

    override fun onPause() {
        super.onPause()
        gameView.pause()  // Pausar el juego cuando la app no esté activa
    }
}