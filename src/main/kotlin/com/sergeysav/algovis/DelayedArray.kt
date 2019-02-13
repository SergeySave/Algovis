package com.sergeysav.algovis

/**
 * @author sergeys
 *
 * @constructor Creates a new DelayedArray
 */
class DelayedArray<T>(val baseArray: Array<T>, val getTime: () -> Double, val setTime: () -> Double) {
    val delayer = Delayer()
    
    private var visitedMain = mutableMapOf<Int, Int>()
    private var visitedEditing = mutableMapOf<Int, Int>()
    val visited: MutableMap<Int, Int>
        get() {
            val result = visitedMain
            visitedMain = visitedEditing
            visitedEditing = result
            return result
        }
    
    constructor(baseArray: Array<T>, getTime: Double, setTime: Double): this(baseArray, { getTime }, { setTime })
    
    /**
     * Returns the array element at the specified [index]. This method can be called using the
     * index operator:
     * ```
     * value = arr[index]
     * ```
     */
    suspend fun get(index: Int): T {
        delayer.doDelay(getTime())
        visitedMain[index] = 0
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
        delayer.doDelay(setTime())
        visitedMain[index] = 0
        baseArray.set(index, value)
    }
    
    /**
     * Returns the number of elements in the array.
     */
    val size: Int = baseArray.size
}