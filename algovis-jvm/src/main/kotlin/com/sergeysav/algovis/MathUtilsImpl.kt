package com.sergeysav.algovis

import java.util.Random

/**
 * @author sergeys
 */
private val random = Random()

actual fun randomInt(min: Int, max: Int): Int = random.nextInt(max - min) + min