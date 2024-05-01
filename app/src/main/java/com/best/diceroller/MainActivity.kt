package com.best.diceroller

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.best.diceroller.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<DiceViewModel>()
    private val countDownTimer = 4000L
    private val countDownInterval = 350L
    private var score1 = 0
    private var score2 = 0
    private val totalRound = 3
    private var currentRound = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.btnPlayer1.setOnClickListener {
            diceRoll(binding.btnPlayer1)
        }

        binding.btnPlayer2.setOnClickListener {
            diceRoll(binding.btnPlayer2)
        }

        binding.btnReset.setOnClickListener {
            reset()
            binding.btnReset.visibility = View.GONE
        }

        diceNumberObserver()

    }

    private fun diceNumberObserver() {
        viewModel.pickedDice.observe(this){dice->
            binding.imgDice.setImageResource(dice)
        }
    }

    private fun diceRoll(button: Button){
        val timer = object: CountDownTimer(countDownTimer,countDownInterval) {
            override fun onTick(p0: Long) {
                setDice()
                button.disable()
            }
            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                if (button == binding.btnPlayer1){
                    score1 += setScore()
                    binding.tvPlayerScore1.text = "Player 1\nScore: $score1"
                    binding.btnPlayer2.enable()
                }else{
                    currentRound += 1
                    score2 += setScore()
                    binding.tvPlayerScore2.text = "Player 2\nScore: $score2"
                    binding.btnPlayer1.enable()
                }


                if (currentRound <= totalRound){
                    binding.tvRound.text = "Round $currentRound/3"
                }else{
                    result()
                    binding.btnPlayer1.disable()
                    binding.btnReset.visibility = View.VISIBLE
                }
            }
        }
        timer.start()
    }

    private fun setDice(){
        viewModel.rollDice()
    }

    private fun setScore(): Int{
        return viewModel.numDice!!
    }


    private fun result(){
        val intent = Intent(this@MainActivity,ResultActivity::class.java)
        if (score1 > score2){
            intent.putExtra("result","Player 1 is Winner")
        } else if (score1 < score2){
            intent.putExtra("result","Player 2 is Winner")
        }
        else{
            intent.putExtra("result","Draw")
        }
        startActivity(intent)
    }

    @SuppressLint("SetTextI18n")
    private fun reset(){
        currentRound = 1
        binding.tvRound.text = "Round $currentRound/3"
        score1 = 0
        binding.tvPlayerScore1.text = "Player 1\nScore: $score1"
        score2 = 0
        binding.tvPlayerScore2.text = "Player 2\nScore: $score2"
        binding.btnPlayer1.enable()
    }
}