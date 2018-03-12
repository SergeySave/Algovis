package com.sergeysav.algovis.algorithms.array

import com.sergeysav.algovis.randomInt
import com.sergeysav.algovis.structures.ArrayStructure

/**
 * @author sergeys
 *
 * @constructor Creates a new BogoSort
 */
class BogoSort(array: ArrayStructure): ArrayAlgorithm(array) {
    
    var index = -1
    var index2 = -1
    
    override fun getSelection(index: Int): Int = if (this.index == index || index2 == index) {
        1
    } else {
        0
    }
    
    override suspend fun execute() {
        while (isActive() && notSorted()) {
            shuffle()
        }
    }
    
    private suspend fun notSorted(): Boolean {
        for (i in 1 until array.size) {
            setVisited(i)
            index = i
            if (array.get(i - 1) >= array.get(i) || !isActive()) {
                index = -1
                return true
            }
        }
        index = -1
        return false
    }
    
    private suspend fun shuffle() {
        for (i in 0 until array.size) {
            setVisited(i)
            index = i
            val other = randomInt(0, array.size - 1)
            index2 = other
            swap(i, other)
            
            if (!isActive()) {
                return
            }
        }
        index2 = -1
    }
}