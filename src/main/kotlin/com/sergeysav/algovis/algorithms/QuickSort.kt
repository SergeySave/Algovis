package com.sergeysav.algovis.algorithms

import com.sergeysav.algovis.ArrayAlgorithm
import com.sergeysav.algovis.DelayedArray
import com.sergeysav.algovis.middleValue

/**
 * @author sergeys
 *
 * @constructor Creates a new QuickSort
 */
class QuickSort(array: DelayedArray<Int>): ArrayAlgorithm(array) {
    
    private var low: Int = -1
    private var high: Int = -1
    private var pivotIdx: Int = -1
    
    override fun getSelection(uuid: Int): Int {
        if (low == uuid || high == uuid) {
            return 1
        } else if (pivotIdx == uuid) {
            return 2
        }
        return 0
    }
    
    override suspend fun execute() {
        quickSort(0, array.size - 1)
        low = -1
        high = -1
        pivotIdx = -1
    }
    
    private suspend fun quickSort(_l: Int, _h: Int) {
        var l = _l
        var h = _h
        low = l
        high = h
        
        if (low >= high) return
        
        val pivotVal = middleValue(low, high, (high + low) / 2) { x -> array.get(x) }
        pivotIdx = pivotVal
        
        val oldLow = low
        val oldHigh = high
        
        while (low < high) {
            while (array.get(low) < pivotVal) low++
            while (array.get(high) > pivotVal) high--
            if (low < high) {
                val swapPivotIdx = if (pivotIdx == low) 1 else if (pivotIdx == high) -1 else 0
                swap(low, high)
                if (swapPivotIdx == 1) pivotIdx = high
                if (swapPivotIdx == -1) pivotIdx = low
                low++
                high--
            } else if (low == high) {
                low++
                high--
            }
        }
        
        l = low
        h = high
        quickSort(oldLow, high)
        low = l
        high = h
        quickSort(low, oldHigh)
    }
    
}