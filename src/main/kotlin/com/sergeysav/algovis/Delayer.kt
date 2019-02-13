package com.sergeysav.algovis

import kotlinx.coroutines.delay

/**
 * @author sergeys
 *
 * @constructor Creates a new Delayer
 */
class Delayer {
    var extraTime: Double = 0.0
    
    suspend fun doDelay(time: Double) {
        extraTime += (time % 1)
        if (extraTime >= 1) {
            extraTime -= 1
            delay(time.toLong() + 1)
        } else if (time >= 1) {
            delay(time.toLong())
        }
    }
}