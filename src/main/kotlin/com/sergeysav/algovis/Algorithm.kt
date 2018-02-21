package com.sergeysav.algovis

/**
 * @author sergeys
 */
abstract class Algorithm {
    final suspend fun run() {
        execute()
        complete = true
    }
    
    protected abstract suspend fun execute()
    
    var complete: Boolean = false
        private set
}