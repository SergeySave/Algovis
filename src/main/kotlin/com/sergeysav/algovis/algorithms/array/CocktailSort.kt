package com.sergeysav.algovis.algorithms.array

import com.sergeysav.algovis.structures.ArrayStructure
import kotlinx.coroutines.isActive

/**
 * @author sergeys
 *
 * @constructor Creates a new BubbleSort
 */
class CocktailSort(array: ArrayStructure): ArrayAlgorithm(array) {
    
    private var selected: Int? = null
    private var high: Int = array.size
    private var low: Int = -1
    
    override fun getSelection(index: Int): Int {
        if (selected == index) {
            return 1
        } else if (index >= high || index <= low) {
            return 2
        }
        return 0
    }
    
    override suspend fun execute() {
        
        while (low < high) {
            var lastChanged = low
            
            for (i in low + 1 until high - 1) {
                selected = i
                //If the next value in the array is bigger
                if (array.get(i) > array.get(i + 1)) {
                    //Swap the elements
                    swap(i, i + 1)
                    lastChanged = i
                }
    
                if (!isActive) return
            }
            high = lastChanged + 1
            
            if (lastChanged == low) break
            
            for (i in high - 1 downTo low + 2) {
                selected = i
                //If the current value is greater than the previous
                if (array.get(i) < array.get(i - 1)) {
                    //Swap the elements
                    swap(i, i - 1)
                    lastChanged = i
                }
    
                if (!isActive) return
            }
            low = lastChanged - 1
            
            if (lastChanged == high) break
        }
        
        selected = -1
        high = -1
        low = array.size
    }
}