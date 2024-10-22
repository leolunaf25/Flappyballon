package com.lunatcoms.flappyballon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lunatcoms.flappyballon.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding

    private var score: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvScore.text = score.toString()

    }

    override fun onResume() {
        super.onResume()
        // Iniciar o reanudar el juego
        binding.gameView.resume()
    }

    override fun onPause() {
        super.onPause()
        //Pausar el juego cuando la app no esté activa
        binding.gameView.pause()
    }

    fun updateScore(){
        score++
        runOnUiThread {
            binding.tvScore.text = score.toString()
        }
    }
}