package com.sergeysav.algovis.structures

import com.sergeysav.algovis.Drawer

object NullStructure: Structure() {
    override val algorithms: List<AlgorithmReference> = emptyList()
    override val initializationConditions: List<InitializationCondition> = emptyList()
    override val delayMillis: Double = 0.0
    override val initialized: Boolean = false
    
    override fun initDraw(drawer: Drawer) = Unit
    override fun draw(drawer: Drawer) = Unit
}