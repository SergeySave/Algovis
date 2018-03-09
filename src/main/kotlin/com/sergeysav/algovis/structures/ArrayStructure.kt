package com.sergeysav.algovis.structures

import com.sergeysav.algovis.DelayedArray
import com.sergeysav.algovis.Drawer
import com.sergeysav.algovis.algorithms.BogoSort
import com.sergeysav.algovis.algorithms.BubbleSort
import com.sergeysav.algovis.algorithms.InsertionSort
import com.sergeysav.algovis.algorithms.LinearSearch
import com.sergeysav.algovis.algorithms.MergeSort
import com.sergeysav.algovis.algorithms.ParMergeSort
import com.sergeysav.algovis.algorithms.ParQuickSort
import com.sergeysav.algovis.algorithms.QuickSort
import com.sergeysav.algovis.algorithms.SelectionSort
import com.sergeysav.algovis.randomInt

/**
 * @author sergeys
 */
class ArrayStructure(override val delayMillis: Double): Structure() {
    var delayArray: DelayedArray<Int> = DelayedArray(Array(0) { 0 }, delayMillis, delayMillis)
    
    override val algorithms: List<AlgorithmReference> = listOf(
            AR("Bogo Sort") { params -> BogoSort(this) },
            AR("Bubble Sort") { params -> BubbleSort(this) },
            AR("Insertion Sort") { params -> InsertionSort(this) },
            AR("Selection Sort") { params -> SelectionSort(this) },
            AR("Merge Sort") { params -> MergeSort(this) },
            AR("Parallel Merge Sort") { params -> ParMergeSort(this) },
            AR("Quick Sort") { params -> QuickSort(this) },
            AR("Parallel Quick Sort") { params -> ParQuickSort(this) },
            AR("Linear Search", listOf(Param("Search For"))) { param -> LinearSearch(this, param[0]) }
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
        delayArray = DelayedArray(initFunc(s), delayMillis, delayMillis)
        maxValue = (delayArray.baseArray.max() ?: 0) + 2
        initialized = true
    }
}