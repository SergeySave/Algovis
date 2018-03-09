package com.sergeysav.algovis.algorithms

import com.sergeysav.algovis.DelayedArray
import com.sergeysav.algovis.Drawer
import com.sergeysav.algovis.structures.ArrayStructure

/**
 * @author sergeys
 *
 * @constructor Creates a new ArrayAlgorithm
 */
abstract class BufferArrayAlgorithm(val array: ArrayStructure): Algorithm() {
    
    protected val buffer = DelayedArray(Array(array.delayArray.size) { -1 }, array.delayMillis,
                                        array.delayMillis)
    val maxValue = (array.delayArray.baseArray.max() ?: 0) + 2
    
    override fun getUUIDs(): List<Int> = array.delayArray.baseArray.indices.toList()
    abstract fun getSelection(uuid: Int): Int
    abstract fun getBufferSelection(index: Int): Int
    
    override fun initDraw(drawer: Drawer) {
        drawer.width = array.delayArray.size
        drawer.height = maxValue * 2
        
        drawer.beginDraw()
    }
    
    override fun doDraw(drawer: Drawer) {
        for (i in 0 until array.delayArray.baseArray.size) {
            val selection = getSelection(i)
            if (selection != 0) {
                drawer.fill(selection, i, maxValue - array.delayArray.baseArray[i] - 1, 1,
                            array.delayArray.baseArray[i] + 1)
            }
        }
        for (i in 0 until array.delayArray.baseArray.size) {
            drawer.fill(getBufferSelection(i), i, maxValue - buffer.baseArray[i] - 1, 1, buffer.baseArray[i] + 1)
        }
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