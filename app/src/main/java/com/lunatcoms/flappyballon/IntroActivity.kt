package com.lunatcoms.flappyballon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lunatcoms.flappyballon.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {
            navigateToGame()
        }
    }

    private fun navigateToGame() {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }
}