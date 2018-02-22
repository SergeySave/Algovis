package com.sergeysav.algovis

/**
 * @author sergeys
 *
 * @constructor Creates a new QuickSort
 */
class MergeSort(array: DelayedArray<Int>): BufferArrayAlgorithm(array) {
    
    private var left: Int = -1
    private var right: Int = -1
    private var lcopy: Int = -1
    private var rcopy: Int = -1
    private var buffer1: Int = -1
    
    override fun getSelection(uuid: Int): Int {
        if (left == uuid || right == uuid) {
            return 2
        } else if (lcopy == uuid || rcopy == uuid) {
            return 1
        }
        return 0
    }
    
    override fun getBufferSelection(index: Int): Int {
        if (index == buffer1) {
            return 1
        }
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
        if (end - start <= 1) return
        
        val mid = (start + end) / 2
        sort(start, mid)
        sort(mid, end)
        
        left = start
        right = end - 1
        
        lcopy = start
        rcopy = mid
        for (i in start until end) {
            buffer1 = i
            if (rcopy >= end || lcopy < mid && array.get(lcopy) <= array.get(rcopy)) {
                buffer.set(i, array.get(lcopy++))
            } else {
                buffer.set(i, array.get(rcopy++))
            }
        }
        
        lcopy = -1
        rcopy = -1
        buffer1 = -1
        
        for (i in start until end) {
            buffer1 = i
            lcopy = i
            array.set(i, buffer.get(i))
        }
        lcopy = -1
        buffer1 = -1
        left = -1
        right = -1
    }
}