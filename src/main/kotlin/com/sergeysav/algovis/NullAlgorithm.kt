package com.sergeysav.algovis

/**
 * @author sergeys
 *
 * @constructor Creates a new NullAlgorithm
 */
class NullAlgorithm<T>: Algorithm<T>() {
    override suspend fun execute() {}
    
    override fun getUUIDs(): List<Int> = emptyList()
    
    override fun doDraw(drawer: Drawer) {}
}