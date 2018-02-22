package com.sergeysav.algovis

/**
 * @author sergeys
 *
 * @constructor Creates a new ArrayAlgorithm
 */
abstract class BufferArrayAlgorithm(val array: DelayedArray<Int>): Algorithm<Int>() {
    
    protected val buffer = DelayedArray(Array(array.size) { 0 }, array.getTime, array.setTime)
    val maxValue = (array.baseArray.max() ?: 0) + 2
    
    override fun getUUIDs(): List<Int> = array.baseArray.indices.toList()
    abstract fun getSelection(uuid: Int): Int
    abstract fun getBufferSelection(index: Int): Int
    
    override fun doDraw(drawer: Drawer) {
        drawer.width = array.size
        drawer.height = maxValue * 2
        
        for (i in 0 until array.baseArray.size) {
            drawer.fill(getSelection(i), i, maxValue * 2 - array.baseArray[i] - 1, 1, array.baseArray[i] + 1)
        }
        for (i in 0 until array.baseArray.size) {
            drawer.fill(getBufferSelection(i), i, maxValue - buffer.baseArray[i] - 1, 1, buffer.baseArray[i] + 1)
        }
    }
    
    protected suspend fun swap(i1: Int, i2: Int) {
        val temp = array.get(i1)
        array.set(i1, array.get(i2))
        array.set(i2, temp)
    }
}