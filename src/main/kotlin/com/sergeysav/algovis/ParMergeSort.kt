package com.sergeysav.algovis

import kotlinx.coroutines.experimental.launch

/**
 * @author sergeys
 *
 * @constructor Creates a new ParMergeSort
 */
class ParMergeSort(array: DelayedArray<Int>): BufferArrayAlgorithm(array) {
    
    private var sides = CountMap<Int>()
    private var copys = CountMap<Int>()
    private var buffers = CountMap<Int>()
    
    override fun getSelection(uuid: Int): Int {
        if (sides.contains(uuid)) {
            return 2
        } else if (copys.contains(uuid)) {
            return 1
        }
        return 0
    }
    
    override fun getBufferSelection(index: Int): Int {
        if (buffers.contains(index)) {
            return 1
        }
        return 0
    }
    
    
    override suspend fun execute() {
        sort(0, array.size)
        sides.clear()
        copys.clear()
        buffers.clear()
    }
    
    private suspend fun sort(start: Int, end: Int) {
        if (end - start <= 1) return
        
        val mid = (start + end) / 2
        val job = launch {
            sort(start, mid)
        }
        sort(mid, end)
        
        job.join()
        
        val left = start
        sides.add(left)
        val right = end - 1
        sides.add(right)
        
        var lcopy = start
        copys.add(lcopy)
        var rcopy = mid
        copys.add(rcopy)
        for (i in start until end) {
            buffers.add(i)
            if (rcopy >= end || lcopy < mid && array.get(lcopy) <= array.get(rcopy)) {
                copys.remove(lcopy)
                buffer.set(i, array.get(lcopy++))
                copys.add(lcopy)
            } else {
                copys.remove(rcopy)
                buffer.set(i, array.get(rcopy++))
                copys.remove(rcopy)
            }
            buffers.remove(i)
        }
        
        copys.remove(lcopy)
        copys.remove(rcopy)
        
        for (i in start until end) {
            buffers.add(i)
            copys.add(i)
            array.set(i, buffer.get(i))
            buffer.set(i, 0)
            copys.remove(i)
            buffers.remove(i)
        }
        
        sides.remove(left)
        sides.remove(right)
    }
}