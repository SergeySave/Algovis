package com.sergeysav.algovis.structures

import com.sergeysav.algovis.Algorithm
import com.sergeysav.algovis.Drawer

/**
 * @author sergeys
 */

val structures = mapOf<String, (Double) -> Structure>(
        "Array" to { delay -> ArrayStructure(delay) }
)

abstract class Structure {
    abstract val algorithms: List<AlgorithmReference>
    abstract val initializationConditions: List<InitializationCondition>
    abstract val delayMillis: Double
    abstract val initialized: Boolean
    
    abstract fun initDraw(drawer: Drawer)
    abstract fun draw(drawer: Drawer)
}

object NullStructure: Structure() {
    override val algorithms: List<AlgorithmReference> = emptyList()
    override val initializationConditions: List<InitializationCondition> = emptyList()
    override val delayMillis: Double = 0.0
    override val initialized: Boolean = false
    
    override fun initDraw(drawer: Drawer) = Unit
    override fun draw(drawer: Drawer) = Unit
}

typealias AR = AlgorithmReference

data class AlgorithmReference(val name: String, val params: List<Param> = emptyList(),
                              val creator: (IntArray) -> Algorithm)

typealias IC = InitializationCondition

data class InitializationCondition(val name: String, private val func: (Int) -> Unit) {
    operator fun invoke(size: Int) = func(size)
}

data class Param(val name: String, val default: Int = 0, val min: Int = Int.MIN_VALUE, val max: Int = Int.MAX_VALUE,
                 val step: Int = 1)
