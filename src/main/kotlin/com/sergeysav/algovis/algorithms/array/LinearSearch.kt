package com.sergeysav.algovis.algorithms.array

import com.sergeysav.algovis.structures.ArrayStructure
import kotlinx.coroutines.isActive

/**
 * @author sergeys
 *
 * @constructor Creates a new LinearSearch
 */
class LinearSearch(array: ArrayStructure, private val searchFor: Int): ArrayAlgorithm(array) {
    
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
        for (i in 0 until array.size) {
            setVisited(i)
            selected = i
            if (array.get(i) == searchFor || !isActive) {
                return
            }
        }
    }
}