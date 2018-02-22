package com.sergeysav.algovis

/**
 * @author sergeys
 *
 * @constructor Creates a new ArrayAlgorithm
 */
abstract class ArrayAlgorithm(val array: DelayedArray<Int>): Algorithm<Int>() {
    
    val maxValue = (array.baseArray.max() ?: 0) + 2
    
    override fun getUUIDs(): List<Int> = array.baseArray.indices.toList()
    abstract fun getSelection(uuid: Int): Int
    
    override fun doDraw(drawer: Drawer) {
        drawer.width = array.size
        drawer.height = maxValue
        
        for (i in 0 until array.baseArray.size) {
            drawer.fill(getSelection(i), i, maxValue - array.baseArray[i] - 1, 1, array.baseArray[i] + 1)
        }
    }
    
    protected suspend fun swap(i1: Int, i2: Int) {
        val temp = array.get(i1)
        array.set(i1, array.get(i2))
        array.set(i2, temp)
    }
}