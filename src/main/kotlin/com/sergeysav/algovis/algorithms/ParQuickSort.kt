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
        var l = _l
        var h = _h
    
        if (l >= h || !isActive()) return
        
        counts.add(l)
        counts.add(h)
        
        var pivotIdx = middleValue(l, h, (h + l) / 2) { x -> array.get(x) }
        pivots.add(pivotIdx)
        val pivotVal = array.get(pivotIdx)
        
        val oldLow = l
        val oldHigh = h
    
        while (l < h && isActive()) {
            while (array.get(l) < pivotVal && isActive()) {
                counts.remove(l)
                counts.add(++l)
            }
            while (array.get(h) > pivotVal && isActive()) {
                counts.remove(h)
                counts.add(--h)
            }
            if (!isActive()) {
                return
            }
            if (l < h) {
                val swapPivotIdx = if (pivotIdx == l) 1 else if (pivotIdx == h) -1 else 0
                swap(l, h)
                if (swapPivotIdx == 1) {
                    pivots.remove(pivotIdx)
                    pivotIdx = h
                    pivots.add(pivotIdx)
                }
                if (swapPivotIdx == -1) {
                    pivots.remove(pivotIdx)
                    pivotIdx = l
                    pivots.add(pivotIdx)
                }
                counts.remove(l)
                counts.add(++l)
                counts.remove(h)
                counts.add(--h)
            } else if (l == h) {
                counts.remove(l)
                counts.add(++l)
                counts.remove(h)
                counts.add(--h)
            }
        }
        
        counts.remove(l)
        counts.remove(h)
        pivots.remove(pivotIdx)
        
        val job = launch {
            quickSort(oldLow, h)
        }
        quickSort(l, oldHigh)
        
        job.join()
    }
    
}