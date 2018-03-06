package com.sergeysav.algovis

/**
 * @author sergeys
 *
 * @constructor Creates a new DelayedArray
 */
class DelayedArray<T>(val baseArray: Array<T>, val getTime: Double, val setTime: Double) {
    val delayer = Delayer()
    
    /**
     * Returns the array element at the specified [index]. This method can be called using the
     * index operator:
     * ```
     * value = arr[index]
     * ```
     */
    suspend fun get(index: Int): T {
        delayer.doDelay(getTime)
        return baseArray.get(index)
    }
    
    /**
     * Sets the array element at the specified [index] to the specified [value]. This method can
     * be called using the index operator:
     * ```
     * arr[index] = value
     * ```
     */
    suspend fun set(index: Int, value: T) {
        delayer.doDelay(setTime)
        baseArray.set(index, value)
    }
    
    /**
     * Returns the number of elements in the array.
     */
    val size: Int = baseArray.size
}