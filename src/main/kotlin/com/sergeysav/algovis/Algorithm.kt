package com.sergeysav.algovis

/**
 * @author sergeys
 */
abstract class Algorithm<T> {
    suspend fun run(isActive: () -> Boolean) {
        this.isActive = isActive
        execute()
        complete = true
    }
    
    protected abstract suspend fun execute()
    
    var complete: Boolean = false
        private set
    
    protected var isActive: () -> Boolean = { true }
        private set
    
    abstract fun getUUIDs(): List<Int>
    
    abstract fun doDraw(drawer: Drawer)
}