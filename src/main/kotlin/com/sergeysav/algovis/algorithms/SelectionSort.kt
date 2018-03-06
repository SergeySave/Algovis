package com.sergeysav.algovis.algorithms

import com.sergeysav.algovis.ArrayAlgorithm
import com.sergeysav.algovis.DelayedArray

/**
 * @author sergeys
 *
 * @constructor Creates a new BubbleSort
 */
class SelectionSort(array: DelayedArray<Int>): ArrayAlgorithm(array) {
    
    private var sortedPartition: Int = -1
    private var min: Int = -1
    private var selected: Int = -1
    
    override fun getSelection(uuid: Int): Int {
        if (uuid == min || uuid == selected) {
            return 1
        } else if (uuid <= sortedPartition) {
            return 2
        }
        return 0
    }
    
    override suspend fun execute() {
        //Loop through the array
        for (i in 0 until array.size - 1) {
            //The index of the current smallest object found
            min = i
            selected = i
            //Loop through the array
            for (i2 in i + 1 until array.size) {
                selected = i2
                //Get the smallest object in the array
                if (array.get(i2) < array.get(min)) {
                    min = i2
                }
            }
            //Swap the current index with the smallest index
            swap(i, min)
            sortedPartition = i
        }
        min = -1
        sortedPartition = array.size + 1
        selected = -1
    }
}