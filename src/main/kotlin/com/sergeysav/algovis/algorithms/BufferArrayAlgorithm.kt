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
    private val maxValue = (array.delayArray.baseArray.max() ?: 0) + 2
    
    var visitedMain = mutableMapOf<Int, Int>()
    var visitedEditing = mutableMapOf<Int, Int>()
    
    var visitedBuffMain = mutableMapOf<Int, Int>()
    var visitedBuffEditing = mutableMapOf<Int, Int>()
    
    fun setVisited(index: Int, type: Int = 0) {
        visitedMain[index] = type
    }
    
    fun setBuffVisited(index: Int, type: Int = 0) {
        visitedBuffMain[index] = type
    }
    
    open fun getSelection(index: Int): Int = 0
    open fun getBufferSelection(index: Int): Int = 0

    override fun initDraw(drawer: Drawer) {
        drawer.width = array.delayArray.size
        drawer.height = maxValue * 2
        
        drawer.beginDraw()
    }
    
    override fun doDraw(drawer: Drawer) {
        val reading = visitedMain
        visitedMain = visitedEditing
        visitedEditing = reading
    
        val readingBuff = visitedBuffMain
        visitedBuffMain = visitedBuffEditing
        visitedBuffEditing = readingBuff
    
        for (i in 0 until array.delayArray.baseArray.size) {
            val selection = getSelection(i)
            if (selection != 0) {
                drawer.fill(selection, i, maxValue - array.delayArray.baseArray[i] - 1, 1,
                            array.delayArray.baseArray[i] + 1)
            }
        }
        for ((i, selection) in reading) {
            drawer.fill(selection + 1, i, maxValue - array.delayArray.baseArray[i] - 1, 1,
                        array.delayArray.baseArray[i] + 1)
        }
        for (i in 0 until array.delayArray.baseArray.size) {
            drawer.fill(getBufferSelection(i), i, maxValue * 2 - buffer.baseArray[i] - 1, 1, buffer.baseArray[i] + 1)
        }
        for ((i, selection) in readingBuff) {
            drawer.fill(selection + 1, i, maxValue * 2 - buffer.baseArray[i] - 1, 1, buffer.baseArray[i] + 1)
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