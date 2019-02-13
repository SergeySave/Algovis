package com.sergeysav.algovis.algorithms.array

import com.sergeysav.algovis.middleValue
import com.sergeysav.algovis.structures.ArrayStructure
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * @author sergeys
 *
 * @constructor Creates a new ParQuickSort
 */
class ParQuickSort(array: ArrayStructure): ArrayAlgorithm(array) {
    
    private var sort: Sort? = null
    
    override fun getSelection(index: Int): Int {
        sort?.run {
            return getSelection(index)
        }
        
        return 0
    }
    
    override suspend fun execute() {
        Sort(0, array.size - 1).apply {
            sort = this
            run()
            sort = null
        }
    }
    
    private inner class Sort(val _l: Int, val _h: Int) {
        var low = -1
        var high = -1
        
        var pivotIdx = -1
        
        var sort1: Sort? = null
        var sort2: Sort? = null
        
        fun getSelection(uuid: Int): Int {
            if (uuid == low || uuid == high) {
                return 1
            }
            if (uuid == pivotIdx) {
                return 2
            }
            sort1?.run {
                val sVal = getSelection(uuid)
                if (sVal != 0) return sVal
            }
            sort2?.run {
                val sVal = getSelection(uuid)
                if (sVal != 0) return sVal
            }
            return 0
        }
        
        suspend fun run() {
            low = _l
            high = _h
    
            if (low >= high || !isActive) return
            
            pivotIdx = middleValue(low, high, (high + low) / 2) { x -> array.get(x) }
            val pivotVal = array.get(pivotIdx)
            
            val oldLow = low
            val oldHigh = high
    
            while (low < high && isActive) {
                while (array.get(low) < pivotVal && isActive) ++low
                while (array.get(high) > pivotVal && isActive) --high
                if (!isActive) {
                    return
                }
                if (low < high) {
                    val swapPivotIdx = if (pivotIdx == low) 1 else if (pivotIdx == high) -1 else 0
                    swap(low, high)
                    if (swapPivotIdx == 1) {
                        pivotIdx = high
                    }
                    if (swapPivotIdx == -1) {
                        pivotIdx = low
                    }
                    ++low
                    --high
                } else if (low == high) {
                    ++low
                    --high
                }
            }
            
            val l = low
            val h = high
            
            low = -1
            high = -1
            pivotIdx = -1
            
            val job = launch {
                Sort(oldLow, h).apply {
                    this@Sort.sort1 = this
                    run()
                    this@Sort.sort1 = null
                }
            }
            Sort(l, oldHigh).apply {
                this@Sort.sort2 = this
                run()
                this@Sort.sort2 = null
            }
            
            job.join()
        }
    }
}