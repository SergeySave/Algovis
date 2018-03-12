package com.sergeysav.algovis.algorithms.array

import com.sergeysav.algovis.Drawer
import com.sergeysav.algovis.algorithms.Algorithm
import com.sergeysav.algovis.structures.ArrayStructure

/**
 * @author sergeys
 *
 * @constructor Creates a new ArrayAlgorithm
 */
abstract class ArrayAlgorithm(val array: ArrayStructure): Algorithm() {
    
    private val maxValue = (array.delayArray.baseArray.max() ?: 0) + 2
    
    var visitedMain = mutableMapOf<Int, Int>()
    var visitedEditing = mutableMapOf<Int, Int>()
    
    fun setVisited(index: Int, type: Int = 0) = synchronized(visitedMain) {
        visitedMain[index] = type
    }
    
    open fun getSelection(index: Int): Int = 0
    
    override fun initDraw(drawer: Drawer) {
        drawer.width = array.delayArray.size
        drawer.height = maxValue
        
        drawer.beginDraw()
    }
    
    override fun doDraw(drawer: Drawer) {
        
        val reading = visitedMain
        synchronized(visitedMain) {
            visitedMain = visitedEditing
            visitedEditing = reading
        }
        
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
        
        reading.clear()
    }
    
    protected suspend fun swap(i1: Int, i2: Int) {
        val temp = array.get(i1)
        array.set(i1, array.get(i2))
        array.set(i2, temp)
    }
}