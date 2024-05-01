package com.best.diceroller

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.best.diceroller.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var diceList: List<Int>
    private val countDownTimer = 4000L
    private val countDownInterval = 350L
    private var score1 = 0
    private var score2 = 0
    private var round = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        diceList = listOf(
            R.drawable.empty_dice,
            R.drawable.dice_1,
            R.drawable.dice_2,
            R.drawable.dice_3,
            R.drawable.dice_4,
            R.drawable.dice_5,
            R.drawable.dice_6,
        )

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

    }

    private fun diceRoll(button: Button){
        var randomDice: Int? = null
        val timer = object: CountDownTimer(countDownTimer,countDownInterval) {
            override fun onTick(p0: Long) {
                randomDice = Random.nextInt(6) + 1
                binding.imgDice.setImageResource(diceList[randomDice!!])
                button.disable()
            }
            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                if (button == binding.btnPlayer1){
                    score1 += randomDice!!
                    binding.tvPlayerScore1.text = "Player 1\nScore: $score1"
                    binding.btnPlayer2.enable()
                }else{
                    round += 1
                    score2 += randomDice!!
                    binding.tvPlayerScore2.text = "Player 2\nScore: $score2"
                    binding.btnPlayer1.enable()
                }

                if (round < 4){
                    binding.tvRound.text = "Round $round/3"
                }else{
                    result()
                    binding.btnPlayer1.disable()
                    binding.btnReset.visibility = View.VISIBLE
                }

            }
        }
        timer.start()
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

    private fun reset(){
        round = 0
        binding.tvRound.text = "Round $round/3"
        score1 = 0
        binding.tvPlayerScore1.text = "Player 1\nScore: $score1"
        score2 = 0
        binding.tvPlayerScore2.text = "Player 2\nScore: $score2"
        binding.btnPlayer1.enable()
    }
}