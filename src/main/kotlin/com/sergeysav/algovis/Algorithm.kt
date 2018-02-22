package com.sergeysav.algovis

/**
 * @author sergeys
 */
abstract class Algorithm<T> {
    suspend fun run() {
        execute()
        complete = true
    }
    
    protected abstract suspend fun execute()
    
    var complete: Boolean = false
        private set
    
    abstract fun getUUIDs(): List<Int>
    
    abstract fun doDraw(drawer: Drawer)
}