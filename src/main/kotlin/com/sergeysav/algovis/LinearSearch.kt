package com.sergeysav.algovis

/**
 * @author sergeys
 *
 * @constructor Creates a new LinearSearch
 */
class LinearSearch<T>(val array: DelayedArray<T>, val searchFor: T) : Algorithm() {
    override suspend fun execute() {
        for (i in 0 until array.size) {
            if (array.get(i) == searchFor) {
                return
            }
        }
    }
}