package com.sergeysav.algovis.algorithms.array

import com.sergeysav.algovis.middleValue
import com.sergeysav.algovis.structures.ArrayStructure
import kotlinx.coroutines.isActive

/**
 * @author sergeys
 *
 * @constructor Creates a new QuickSort
 */
class QuickSort(array: ArrayStructure): ArrayAlgorithm(array) {
    
    private var low: Int = -1
    private var high: Int = -1
    private var pivotIdx: Int = -1
    
    override fun getSelection(index: Int): Int {
        if (low == index || high == index) {
            return 1
        } else if (pivotIdx == index) {
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
    
        if (low >= high || !isActive) return
        
        pivotIdx = middleValue(low, high, (high + low) / 2) { x -> array.get(x) }
        val pivotVal = array.get(pivotIdx)
        
        val oldLow = low
        val oldHigh = high
    
        while (low < high && isActive) {
            setVisited(low)
            while (array.get(low) < pivotVal && isActive) {
                low++
                setVisited(low)
            }
            setVisited(high)
            while (array.get(high) > pivotVal && isActive) {
                high--
                setVisited(high)
            }
            if (!isActive) {
                return
            }
            if (low < high) {
                val swapPivotIdx = if (pivotIdx == low) 1 else if (pivotIdx == high) -1 else 0
                swap(low, high)
                if (swapPivotIdx == 1) pivotIdx = high
                if (swapPivotIdx == -1) pivotIdx = low
                low++
                setVisited(low)
                high--
                setVisited(high)
            } else if (low == high) {
                low++
                setVisited(low)
                high--
                setVisited(high)
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