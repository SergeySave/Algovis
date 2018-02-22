package com.sergeysav.algovis

import kotlin.math.min

/**
 * @author sergeys
 */
expect fun randomInt(min: Int, max: Int): Int

inline fun middleValue(i1: Int, i2: Int, i3: Int, byFunc: (Int) -> Int = { x -> x }): Int {
    val byFunc1 = byFunc(i1)
    val byFunc2 = byFunc(i2)
    val byFunc3 = byFunc(i3)
    if (byFunc1 < byFunc2 && byFunc1 < byFunc3) {
        return min(i2, i3)
    } else if (byFunc2 < byFunc1 && byFunc2 < byFunc3) {
        return min(i1, i3)
    } else {
        return min(i1, i2)
    }
}