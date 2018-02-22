package com.sergeysav.algovis

/**
 * @author sergeys
 */
//No other way for me use use a random than kotlin.js.Math
@Suppress("DEPRECATION")
actual fun randomInt(min: Int, max: Int): Int = (kotlin.js.Math.random() * (max - min)).toInt() + min