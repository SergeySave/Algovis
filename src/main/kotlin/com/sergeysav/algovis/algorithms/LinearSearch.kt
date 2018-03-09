package com.sergeysav.algovis.algorithms

import com.sergeysav.algovis.ArrayAlgorithm
import com.sergeysav.algovis.structures.ArrayStructure

/**
 * @author sergeys
 *
 * @constructor Creates a new LinearSearch
 */
class LinearSearch(array: ArrayStructure, private val searchFor: Int): ArrayAlgorithm(array) {
    
    private var selected: Int? = null
    
    override fun getSelection(uuid: Int): Int {
        if (selected == uuid) {
            return 1
        } else if (array.delayArray.baseArray[uuid] == searchFor) {
            return 2
        }
        return 0
    }
    
    override suspend fun execute() {
        for (i in 0 until array.size) {
            selected = i
            if (array.get(i) == searchFor || !isActive()) {
                return
            }
        }
    }
}