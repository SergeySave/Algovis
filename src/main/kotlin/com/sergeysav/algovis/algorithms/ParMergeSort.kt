package com.sergeysav.algovis.algorithms

import com.sergeysav.algovis.structures.ArrayStructure
import kotlinx.coroutines.experimental.launch

/**
 * @author sergeys
 *
 * @constructor Creates a new ParMergeSort
 */
class ParMergeSort(array: ArrayStructure): BufferArrayAlgorithm(array) {
    
    private var sort: Sort? = null
    
    override fun getSelection(uuid: Int): Int {
        sort?.run {
            val sVal = getSelection(uuid)
            if (sVal != 0) return sVal
        }
    
        return 0
    }
    
    override fun getBufferSelection(index: Int): Int {
        sort?.run {
            val sVal = getBufferSelection(index)
            if (sVal != 0) return sVal
        }
    
        return 0
    }
    
    
    override suspend fun execute() {
        Sort(0, array.size).apply {
            sort = this
            run()
            sort = null
        }
    }
    
    private inner class Sort(val start: Int, val end: Int) {
        
        var left = -1
        var right = -1
        
        var lcopy = -1
        var rcopy = -1
        
        var bufferIdx = -1
        
        var sort1: Sort? = null
        var sort2: Sort? = null
        
        fun getSelection(uuid: Int): Int {
            if (uuid == left || uuid == right) {
                return 2
            }
            if (uuid == lcopy || uuid == rcopy) {
                return 1
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
        
        fun getBufferSelection(index: Int): Int {
            if (index == bufferIdx) {
                return 1
            }
            sort1?.run {
                val sVal = getBufferSelection(index)
                if (sVal != 0) return sVal
            }
            sort2?.run {
                val sVal = getBufferSelection(index)
                if (sVal != 0) return sVal
            }
            return 0
        }
        
        suspend fun run() {
            if (end - start <= 1) return
            
            val mid = (start + end) / 2
            val job = launch {
                Sort(start, mid).apply {
                    this@Sort.sort1 = this
                    run()
                    this@Sort.sort1 = null
                }
            }
            Sort(mid, end).apply {
                this@Sort.sort2 = this
                run()
                this@Sort.sort2 = null
            }
            
            job.join()
            
            left = start
            right = end - 1
            
            lcopy = start
            rcopy = mid
            
            for (i in start until end) {
                bufferIdx = i - 1
                if (rcopy >= end || lcopy < mid && array.get(lcopy) <= array.get(rcopy)) {
                    buffer.set(i, array.get(lcopy++))
                } else {
                    buffer.set(i, array.get(rcopy++))
                }
                
                if (!isActive()) {
                    return
                }
            }
            
            lcopy = -1
            rcopy = -1
            
            for (i in start until end) {
                bufferIdx = i
                lcopy = i
                array.set(i, buffer.get(i))
                clearBuffer(i)
                
                if (!isActive()) {
                    return
                }
            }
        }
    }
}