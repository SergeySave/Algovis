package com.sergeysav.algovis

import com.sergeysav.algovis.algorithms.BogoSort
import com.sergeysav.algovis.algorithms.BubbleSort
import com.sergeysav.algovis.algorithms.MergeSort
import com.sergeysav.algovis.algorithms.ParMergeSort
import com.sergeysav.algovis.algorithms.ParQuickSort
import com.sergeysav.algovis.algorithms.QuickSort

/**
 * @author sergeys
 */

val algorithms = mapOf(
        delayed { arr -> BogoSort(arr) } to "Bogo Sort",
        delayed { arr -> BubbleSort(arr) } to "Bubble Sort",
        delayed { arr -> MergeSort(arr) } to "Merge Sort",
        delayed { arr -> ParMergeSort(arr) } to "Parallel Merge Sort",
        delayed { arr -> QuickSort(arr) } to "Quick Sort",
        delayed { arr -> ParQuickSort(arr) } to "Parallel Quick Sort"
)

private inline fun delayed(crossinline constructor: (DelayedArray<Int>) -> Algorithm<Int>) = { arr: Array<Int> ->
    constructor(DelayedArray(arr, 1, 1))
}
