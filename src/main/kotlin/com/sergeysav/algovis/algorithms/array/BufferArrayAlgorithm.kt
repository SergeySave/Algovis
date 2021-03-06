package com.sergeysav.algovis.algorithms.array

import com.sergeysav.algovis.DelayedArray
import com.sergeysav.algovis.Drawer
import com.sergeysav.algovis.algorithms.Algorithm
import com.sergeysav.algovis.structures.ArrayStructure

/**
 * @author sergeys
 *
 * @constructor Creates a new ArrayAlgorithm
 */
abstract class BufferArrayAlgorithm(val array: ArrayStructure): Algorithm() {
    
    protected val buffer = DelayedArray(Array(array.delayArray.size) { -1 }, array::delayMillis,
                                        array::delayMillis)
    
    open fun getSelection(index: Int): Int = 0
    open fun getBufferSelection(index: Int): Int = 0
    
    override fun initDraw(drawer: Drawer) {
        drawer.width = array.delayArray.size
        drawer.height = array.maxValue * 2
        
        drawer.beginDraw()
    }
    
    override fun doDraw(drawer: Drawer) {
        val reading = array.delayArray.visited
    
        val readingBuff = buffer.visited
        
        for (i in 0 until array.delayArray.baseArray.size) {
            val selection = getSelection(i)
            if (selection != 0) {
                drawer.fill(selection, i, array.maxValue - array.delayArray.baseArray[i] - 1, 1,
                            array.delayArray.baseArray[i] + 1)
            }
        }
        for ((i, selection) in reading) {
            drawer.fill(selection + 1, i, array.maxValue - array.delayArray.baseArray[i] - 1, 1,
                        array.delayArray.baseArray[i] + 1)
        }
        for (i in 0 until array.delayArray.baseArray.size) {
            drawer.fill(getBufferSelection(i), i, array.maxValue * 2 - buffer.baseArray[i] - 1, 1,
                        buffer.baseArray[i] + 1)
        }
        for ((i, selection) in readingBuff) {
            drawer.fill(selection + 1, i, array.maxValue * 2 - buffer.baseArray[i] - 1, 1, buffer.baseArray[i] + 1)
        }
        
        reading.clear()
        readingBuff.clear()
    }
    
    protected suspend fun swap(i1: Int, i2: Int) {
        val temp = array.get(i1)
        array.set(i1, array.get(i2))
        array.set(i2, temp)
    }
    
    protected suspend fun clearBuffer(index: Int) {
        buffer.set(index, -1)
    }
}