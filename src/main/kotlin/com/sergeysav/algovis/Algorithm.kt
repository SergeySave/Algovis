package com.sergeysav.algovis

/**
 * @author sergeys
 */
abstract class Algorithm {
    suspend fun run(isActive: () -> Boolean) {
        this.isActive = isActive
        running = true
        try {
            execute()
        } finally {
            complete = true
        }
    }
    
    protected abstract suspend fun execute()
    
    var complete: Boolean = false
        private set
    
    var running: Boolean = false
    
    protected var isActive: () -> Boolean = { true }
        private set
    
    abstract fun getUUIDs(): List<Int>
    
    abstract fun initDraw(drawer: Drawer)
    abstract fun doDraw(drawer: Drawer)
}