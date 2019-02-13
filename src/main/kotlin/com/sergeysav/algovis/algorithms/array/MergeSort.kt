package com.sergeysav.algovis.algorithms.array

import com.sergeysav.algovis.structures.ArrayStructure
import kotlinx.coroutines.isActive

/**
 * @author sergeys
 *
 * @constructor Creates a new QuickSort
 */
class MergeSort(array: ArrayStructure): BufferArrayAlgorithm(array) {
    
    private var left: Int = -1
    private var right: Int = -1
    private var lcopy: Int = -1
    private var rcopy: Int = -1
    private var buffer1: Int = -1
    
    override fun getSelection(index: Int): Int {
        //        if (left == index || right == index) {
        //            return 2
        //        } else if (lcopy == index || rcopy == index) {
        //            return 1
        //        }
        return 0
    }
    
    override fun getBufferSelection(index: Int): Int {
        //        if (index == buffer1) {
        //            return 1
        //        }
        return 0
    }
    
    override suspend fun execute() {
        sort(0, array.size)
        left = -1
        right = -1
        lcopy = -1
        rcopy = -1
        buffer1 = -1
    }
    
    private suspend fun sort(start: Int, end: Int) {
        if (end - start <= 1 || !isActive) return
        
        val mid = (start + end) / 2
        sort(start, mid)
        sort(mid, end)
        
        left = start
        right = end - 1
        
        lcopy = start
        rcopy = mid
        for (i in start until end) {
            buffer1 = i
            if (rcopy < end) {
            }
            if (lcopy < end) {
            }
            if (rcopy >= end || lcopy < mid && array.get(lcopy) <= array.get(rcopy)) {
                buffer.set(i, array.get(lcopy++))
            } else {
                buffer.set(i, array.get(rcopy++))
            }
    
            if (!isActive) {
                return
            }
        }
        
        lcopy = -1
        rcopy = -1
        buffer1 = -1
        
        for (i in start until end) {
            buffer1 = i
            lcopy = i
            array.set(i, buffer.get(i))
            clearBuffer(i)
    
            if (!isActive) {
                return
            }
        }
        lcopy = -1
        buffer1 = -1
        left = -1
        right = -1
    }
}
