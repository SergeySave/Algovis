package com.sergeysav.algovis.algorithms.array

import com.sergeysav.algovis.structures.ArrayStructure

/**
 * @author sergeys
 *
 * Based on the algorithm found in
 * https://github.com/liuxinyu95/AlgoXY/blob/algoxy/sorting/merge-sort/src/mergesort.c
 *
 * @constructor Creates a new InlineMergeSort
 */
class InlineMergeSort(array: ArrayStructure): ArrayAlgorithm(array) {
    
    private var low: Int = -1
    private var up: Int = -1
    private var working: Int = -1
    
    override fun getSelection(index: Int): Int = when (index) {
        low, up, working -> 1
        else             -> 0
    }
    
    override suspend fun execute() {
        inlineMergeSort(0, array.size)
        low = -1
        up = -1
        working = -1
    }
    
    private suspend fun inlineMergeSort(l: Int, u: Int) {
        if (u - l > 1) {
            val m = l + (u - l) / 2
            var w = l + u - m
            low = l
            up = u
            working = w
            sortIntoWorkingArea(l, m, w)
            while (w - l > 2) {
                low = l
                up = u
                working = w
                val n = w
                w = l + (n - l + 1) / 2
                sortIntoWorkingArea(w, n, l)
                mergeIntoWorkingArea(l, l + n - w, n, u, w)
            }
            for (i in w downTo (l + 1)) {
                var j = i
                while (j < u && array.get(j) < array.get(j - 1)) {
                    low = l
                    up = u
                    working = w
                    swap(j, j - 1)
                    j++
                }
            }
        }
    }
    
    private suspend fun sortIntoWorkingArea(_l: Int, u: Int, _w: Int) {
        var l = _l
        var w = _w
        low = l
        up = u
        working = w
        if (u - l > 1) {
            val m = l + (u - l) / 2
            inlineMergeSort(l, m)
            inlineMergeSort(m, u)
            mergeIntoWorkingArea(_l, m, m, u, _w)
        } else {
            while (l < u) {
                low = l
                up = u
                working = w
                swap(l++, w++)
            }
        }
    }
    
    private suspend fun mergeIntoWorkingArea(_i: Int, m: Int, _j: Int, n: Int, _w: Int) {
        var i = _i
        var j = _j
        var w = _w
        low = i
        up = j
        working = w
        while (i < m && j < n) {
            low = i
            up = j
            working = w
            swap(w++, if (array.get(i) < array.get(j)) i++ else j++)
        }
        while (i < m) {
            low = i
            up = j
            working = w
            swap(w++, i++)
        }
        while (j < n) {
            low = i
            up = j
            working = w
            swap(w++, j++)
        }
    }
}