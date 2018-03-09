package com.sergeysav.algovis.structures

import com.sergeysav.algovis.Drawer

abstract class Structure {
    abstract val algorithms: List<AlgorithmReference>
    abstract val initializationConditions: List<InitializationCondition>
    abstract val delayMillis: Double
    abstract val initialized: Boolean
    
    abstract fun initDraw(drawer: Drawer)
    abstract fun draw(drawer: Drawer)
}