package com.sergeysav.algovis.conditions

import com.sergeysav.algovis.randomInt

/**
 * @author sergeys
 */
val conditions = mapOf(
        { i: Int -> (0 until i).toList().shuffled().toTypedArray() } to "Shuffled",
        { i: Int -> Array(i) { x -> x } } to "Sorted",
        { i: Int ->
            Array(i) { x -> x }.apply {
                for (x in 0 until i) {
                    val index = randomInt(maxOf(0, x - i / 20), minOf(i - 1, x + i / 20))
                    val temp = this[x]
                    this[x] = this[index]
                    this[index] = temp
                }
            }
        } to "Nearly Sorted",
        { i: Int -> (0 until i).toList().map { x -> x % 5 }.shuffled().toTypedArray() } to "Few Uniques",
        { i: Int -> Array(i) { x -> i - x - 1 } } to "Reversed"
)