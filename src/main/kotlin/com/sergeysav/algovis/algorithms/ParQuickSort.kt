package com.sergeysav.algovis.algorithms

import com.sergeysav.algovis.ArrayAlgorithm
import com.sergeysav.algovis.CountMap
import com.sergeysav.algovis.DelayedArray
import com.sergeysav.algovis.middleValue
import kotlinx.coroutines.experimental.launch

/**
 * @author sergeys
 *
 * @constructor Creates a new ParQuickSort
 */
class ParQuickSort(array: DelayedArray<Int>): ArrayAlgorithm(array) {
    
    private var counts = CountMap<Int>()
    private var pivots = CountMap<Int>()
    
    override fun getSelection(uuid: Int): Int {
        if (counts.contains(uuid)) {
            return 1
        } else if (pivots.contains(uuid)) {
            return 2
        }
        return 0
    }
    
    override suspend fun execute() {
        quickSort(0, array.size - 1)
        counts.clear()
        pivots.clear()
    }
    
    private suspend fun quickSort(_l: Int, _h: Int) {
        var low = _l
        var high = _h
        counts.add(low)
        counts.add(high)
    
        if (low >= high || !isActive()) return
    
        var pivotIdx = middleValue(low, high, (high + low) / 2) { x -> array.get(x) }
        pivots.add(pivotIdx)
        val pivotVal = array.get(pivotIdx)
    
        val oldLow = low
        val oldHigh = high
    
        while (low < high && isActive()) {
            while (array.get(low) < pivotVal && isActive()) {
                counts.remove(low++)
                counts.add(low)
            }
            while (array.get(high) > pivotVal && isActive()) {
                counts.remove(high--)
                counts.add(high)
            }
            if (!isActive()) {
                return
            }
            if (low < high) {
                val swapPivotIdx = if (pivotIdx == low) 1 else if (pivotIdx == high) -1 else 0
                swap(low, high)
                if (swapPivotIdx == 1) {
                    pivots.remove(pivotIdx)
                    pivotIdx = high
                    pivots.add(pivotIdx)
                }
                if (swapPivotIdx == -1) {
                    pivots.remove(pivotIdx)
                    pivotIdx = low
                    pivots.add(pivotIdx)
                }
                counts.remove(low++)
                counts.add(low)
                counts.remove(high--)
                counts.add(high)
            } else if (low == high) {
                counts.remove(low++)
                counts.add(low)
                counts.remove(high--)
                counts.add(high)
            }
        }
    
        val job = launch {
            quickSort(oldLow, high)
        }
        quickSort(low, oldHigh)
        
        job.join()
    }
    
}