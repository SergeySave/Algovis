package com.sergeysav.algovis.algorithms.array

import com.sergeysav.algovis.structures.ArrayStructure

/**
 * @author sergeys
 *
 * @constructor Creates a new BinarySearch
 */
class BinarySearch(array: ArrayStructure, private val searchFor: Int): ArrayAlgorithm(array) {
    
    private var selected: Int? = null
    
    override fun getSelection(index: Int): Int {
        if (selected == index) {
            return 1
        } else if (array.delayArray.baseArray[index] == searchFor) {
            return 2
        }
        return 0
    }
    
    override suspend fun execute() {
        var low = 0
        var high = array.size
        do {
            selected = (low + high) / 2
            val current = array.get(selected!!)
            when {
                searchFor < current  -> high = selected!!
                searchFor == current -> return
                searchFor > current  -> low = selected!!
            }
        } while (low < high)
    }
}