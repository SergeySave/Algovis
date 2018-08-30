package com.sergeysav.algovis.structures

import com.sergeysav.algovis.DelayedArray
import com.sergeysav.algovis.Drawer
import com.sergeysav.algovis.algorithms.array.BinarySearch
import com.sergeysav.algovis.algorithms.array.BogoSort
import com.sergeysav.algovis.algorithms.array.BubbleSort
import com.sergeysav.algovis.algorithms.array.CocktailSort
import com.sergeysav.algovis.algorithms.array.InlineMergeSort
import com.sergeysav.algovis.algorithms.array.InsertionSort
import com.sergeysav.algovis.algorithms.array.LinearSearch
import com.sergeysav.algovis.algorithms.array.MergeSort
import com.sergeysav.algovis.algorithms.array.ParMergeSort
import com.sergeysav.algovis.algorithms.array.ParQuickSort
import com.sergeysav.algovis.algorithms.array.QuickSort
import com.sergeysav.algovis.algorithms.array.SelectionSort
import com.sergeysav.algovis.randomInt

/**
 * @author sergeys
 */
class ArrayStructure: Structure() {
    var delayArray: DelayedArray<Int> = DelayedArray(Array(0) { 0 }, ::delayMillis, ::delayMillis)
    
    override val algorithms: List<AlgorithmReference> = listOf(
            AR("Bogo Sort") { BogoSort(this) },
            AR("Bubble Sort") { BubbleSort(this) },
            AR("Cocktail Shaker Sort") { CocktailSort(this) },
            AR("Insertion Sort") { InsertionSort(this) },
            AR("Selection Sort") { SelectionSort(this) },
            AR("Merge Sort") { MergeSort(this) },
            AR("Parallel Merge Sort") { ParMergeSort(this) },
            AR("Inline Merge Sort") { InlineMergeSort(this) },
            AR("Quick Sort") { QuickSort(this) },
            AR("Parallel Quick Sort") { ParQuickSort(this) },
            AR("Linear Search", listOf(Param("Search For"))) { param ->
                LinearSearch(this, param[0])
            },
            AR("Binary Search", listOf(Param("Search For"))) { param ->
                BinarySearch(this, param[0])
            }
    )
    
    override val initializationConditions: List<InitializationCondition> = listOf(
            ic("Shuffled") { i -> (0 until i).toList().shuffled().toTypedArray() },
            ic("Sorted") { i -> Array(i) { x -> x } },
            ic("Nearly Sorted") { i ->
                Array(i) { x -> x }.apply {
                    for (x in 0 until i) {
                        val index = randomInt(maxOf(0, x - i / 20), minOf(i - 1, x + i / 20))
                        val temp = this[x]
                        this[x] = this[index]
                        this[index] = temp
                    }
                }
            },
            ic("Few Uniques") { i -> (0 until i).toList().map { x -> x % 5 }.shuffled().toTypedArray() },
            ic("Reversed") { i -> Array(i) { x -> i - x - 1 } }
    )
    
    suspend fun get(index: Int): Int = delayArray.get(index)
    
    suspend fun set(index: Int, value: Int) = delayArray.set(index, value)
    
    override var initialized: Boolean = false
    
    val size: Int
        get() = delayArray.size
    
    private var maxValue = 0
    
    override fun initDraw(drawer: Drawer) {
        drawer.width = delayArray.size
        drawer.height = maxValue
        
        drawer.beginDraw()
    }
    
    override fun draw(drawer: Drawer) {
        for (i in 0 until delayArray.baseArray.size) {
            drawer.fill(0, i, maxValue - delayArray.baseArray[i] - 1, 1, delayArray.baseArray[i] + 1)
        }
    }
    
    
    private fun ic(name: String, initFunc: (Int) -> Array<Int>) = IC(name) { s ->
        delayArray = DelayedArray(initFunc(s), ::delayMillis, ::delayMillis)
        maxValue = (delayArray.baseArray.max() ?: 0) + 2
        initialized = true
    }
}