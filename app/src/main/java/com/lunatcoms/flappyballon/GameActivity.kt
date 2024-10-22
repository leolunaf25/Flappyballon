package com.lunatcoms.flappyballon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.lunatcoms.flappyballon.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding

    private var score: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvScore.text = score.toString()

        binding.btnRefresh.setOnClickListener {
            binding.gameView.pause()
            recreate()
        }

        binding.btnExit.setOnClickListener {
            // Salir del juego
            //finishAffinity()
            //exitProcess(0)
            finish()  // Cierra la actividad actual y regresa a la anterior
        }
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

    fun showScoreBoard(){
        updateBoard()
        runOnUiThread {
            binding.cvBoard.visibility = View.VISIBLE
        }
    }

    private fun updateBoard() {
        runOnUiThread {
            binding.tvBoardScore.text = score.toString()
            binding.tvRecord.text = getHighScore().toString()
        }
    }

    fun updateScore() {
        score++
        runOnUiThread {
            binding.tvScore.text = score.toString()
        }
        if (score > getHighScore()){
            saveRecord(score)
        }
    }

    private fun saveRecord(score: Int) {
        val sharedPreferences = getSharedPreferences("game_prefabs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("high_score", score)
        editor.apply()
    }

    private fun getHighScore(): Int {
        val sharedPreferences = getSharedPreferences("game_prefabs", MODE_PRIVATE)
        return sharedPreferences.getInt("high_score",0)
    }
}