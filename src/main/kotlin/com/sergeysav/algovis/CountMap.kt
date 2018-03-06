package com.sergeysav.algovis

/**
 * @author sergeys
 *
 * @constructor Creates a new CountMap
 */
class CountMap<T> {
    private val map = mutableMapOf<T, Int>()
    
    fun add(element: T) {
        map[element] = (map[element] ?: 0) + 1
    }
    
    fun remove(element: T) {
        if (map[element] ?: 0 > 1) {
            map[element] = map[element]!! - 1
        }
        if (map[element] ?: 0 == 1) {
            map.remove(element)
        }
    }
    
    fun contains(element: T): Boolean {
        if ((map[element] ?: return false) == 0) {
            map.remove(element)
            return false
        }
        return true
    }
    
    fun clear() {
        map.clear()
    }
}