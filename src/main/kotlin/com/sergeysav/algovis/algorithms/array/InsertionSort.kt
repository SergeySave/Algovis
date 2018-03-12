package com.sergeysav.algovis.algorithms.array

import com.sergeysav.algovis.structures.ArrayStructure

/**
 * @author sergeys
 *
 * @constructor Creates a new BubbleSort
 */
class InsertionSort(array: ArrayStructure): ArrayAlgorithm(array) {
    
    private var sel: Int = -1
    private var part: Int = -1
    
    override fun getSelection(index: Int): Int {
        if (sel == index) {
            return 1
        } else if (index <= part) {
            return 2
        }
        return 0
    }
    
    override suspend fun execute() {
        //Loop through the array
        for (i in 0 until array.size) {
            setVisited(i)
            part = i
            sel = i
            // Once again it's a good idea to put 'Val' at the end of array members
            val pivotVal = array.get(i)
            // Start from one below the pivot location and continue downwards until the pivot
            //    is either correctly placed or is at the beginning of the array
            for (j in i - 1 downTo 0) {
                setVisited(j + 1)
                sel = j + 1
                // Instead of finding the corret location and then shifting everything, it's easier
                //    to just keep swapping the pivot down (sort of like bubble sort)
                if (array.get(j) > pivotVal) {
                    swap(j, j + 1)
                } else {
                    // If the next element down is not less than the pivot, the pivot is placed
                    break
                }
            }
        }
        part++
        sel = -2
    }
}