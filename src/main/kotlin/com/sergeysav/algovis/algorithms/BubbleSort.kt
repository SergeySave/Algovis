package com.sergeysav.algovis.algorithms

import com.sergeysav.algovis.structures.ArrayStructure

/**
 * @author sergeys
 *
 * @constructor Creates a new BubbleSort
 */
class BubbleSort(array: ArrayStructure): ArrayAlgorithm(array) {
    
    private var selected: Int? = null
    private var sorted: Int = array.size
    
    override fun getSelection(index: Int): Int {
        if (selected == index) {
            return 1
        } else if (index >= sorted) {
            return 2
        }
        return 0
    }
    
    override suspend fun execute() {
        //Loop through the array starting from the end
        for (end in array.size - 1 downTo 1) {
            //Has an action been taken
            var actionTaken = false
            
            //Loop through the array from the beginning to the previous end value
            for (i in 0 until end) {
                setVisited(i)
                selected = i
                //If the next value in the array is bigger
                if (array.get(i) > array.get(i + 1)) {
                    //Swap the elements
                    swap(i, i + 1)
                    actionTaken = true
                }
    
                if (!isActive()) {
                    return
                }
            }
            
            sorted = end
            
            //If nothing happened stop sorting
            if (!actionTaken) break
        }
        selected = -1
        sorted = -1
    }
}