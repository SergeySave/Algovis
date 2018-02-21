package com.sergeysav.algovis

import kotlinx.coroutines.experimental.delay

/**
 * @author sergeys
 *
 * @constructor Creates a new DelayedArray
 */
class DelayedArray<T>(val baseArray: Array<T>, val getTime: Int, val setTime: Int) {
    
    /**
     * Returns the array element at the specified [index]. This method can be called using the
     * index operator:
     * ```
     * value = arr[index]
     * ```
     */
    public suspend fun get(index: Int): T {
        delay(getTime)
        return baseArray.get(index)
    }
    
    /**
     * Sets the array element at the specified [index] to the specified [value]. This method can
     * be called using the index operator:
     * ```
     * arr[index] = value
     * ```
     */
    public suspend fun set(index: Int, value: T): Unit {
        delay(setTime)
        baseArray.set(index, value)
    }
    
    /**
     * Returns the number of elements in the array.
     */
    public val size: Int = baseArray.size
}