package com.sergeysav.algovis.structures

/**
 * @author sergeys
 */

val structures = mapOf<String, (Double) -> Structure>(
        "Array" to ::ArrayStructure,
        "Binary Search Tree" to ::BSTStructure
)

typealias AR = AlgorithmReference
typealias IC = InitializationCondition

