package com.sergeysav.algovis.structures

data class InitializationCondition(val name: String, private val func: (Int) -> Unit) {
    operator fun invoke(size: Int) = func(size)
}