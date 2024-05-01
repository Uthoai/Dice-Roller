package com.best.diceroller

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DiceViewModel: ViewModel() {

    private val diceList = listOf(
        R.drawable.empty_dice,
        R.drawable.dice_1,
        R.drawable.dice_2,
        R.drawable.dice_3,
        R.drawable.dice_4,
        R.drawable.dice_5,
        R.drawable.dice_6,
    )
    private val _pickedDice = MutableLiveData<Int>()

    val pickedDice: LiveData<Int>
        get() = _pickedDice

    private val numberOfSide = 6
    var numDice: Int? = null
    fun rollDice(){
        val dice = Dice(numberOfSide)
        numDice = dice.roll()
        _pickedDice.postValue(diceList[numDice!!])
    }
}