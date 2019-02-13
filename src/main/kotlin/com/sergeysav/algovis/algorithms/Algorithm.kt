package com.sergeysav.algovis.algorithms

import com.sergeysav.algovis.Drawer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * @author sergeys
 */
abstract class Algorithm: CoroutineScope {
    private var job: Job? = null
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job!!
    
    fun start(onCompletion: () -> Unit) {
        job = Job()
        running = true
        launch {
            try {
                execute()
            } finally {
                complete = true
            }
            onCompletion()
        }
    }
    
    fun stop() {
        job?.cancel()
        running = false
        complete = true
    }
    
    protected abstract suspend fun execute()
    
    var complete: Boolean = false
        private set
    
    var running: Boolean = false
    
    val active: Boolean
        get() = running && (!complete)
    
    abstract fun initDraw(drawer: Drawer)
    abstract fun doDraw(drawer: Drawer)
}