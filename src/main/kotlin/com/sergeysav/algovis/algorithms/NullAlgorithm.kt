package com.sergeysav.algovis.algorithms

import com.sergeysav.algovis.Drawer

/**
 * @author sergeys
 *
 * @constructor Creates a new NullAlgorithm
 */
object NullAlgorithm: Algorithm() {
    
    override suspend fun execute() = Unit
    
    override fun initDraw(drawer: Drawer) = Unit
    override fun doDraw(drawer: Drawer) = Unit
}