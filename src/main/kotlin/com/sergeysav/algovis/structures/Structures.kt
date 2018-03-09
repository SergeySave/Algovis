package com.sergeysav.algovis.structures

/**
 * @author sergeys
 */

val structures = mapOf<String, (Double) -> Structure>(
        "Array" to { delay -> ArrayStructure(delay) }
)

typealias AR = AlgorithmReference
typealias IC = InitializationCondition

