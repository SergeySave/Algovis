package com.sergeysav.algovis

/**
 * @author sergeys
 *
 * @constructor Creates a new NullAlgorithm
 */
object NullAlgorithm: Algorithm() {
    
    override suspend fun execute() = Unit
    
    override fun getUUIDs(): List<Int> = emptyList()
    
    override fun initDraw(drawer: Drawer) = Unit
    override fun doDraw(drawer: Drawer) = Unit
}