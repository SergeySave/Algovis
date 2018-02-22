package com.sergeysav.algovis

/**
 * @author sergeys
 *
 * @constructor Creates a new BogoSort
 */
class BogoSort(array: DelayedArray<Int>): ArrayAlgorithm(array) {
    
    var index = -1
    var index2 = -1
    
    override fun getSelection(uuid: Int): Int = if (index == uuid || index2 == uuid) {
        1
    } else {
        0
    }
    
    override suspend fun execute() {
        while (notSorted()) {
            shuffle()
        }
    }
    
    private suspend fun notSorted(): Boolean {
        for (i in 1 until array.size) {
            index = i
            if (array.get(i - 1) >= array.get(i)) {
                index = -1
                return true
            }
        }
        index = -1
        return false
    }
    
    private suspend fun shuffle() {
        for (i in 0 until array.size) {
            index = i
            val other = randomInt(0, array.size - 1)
            index2 = other
            swap(i, other)
        }
        index2 = -1
    }
}