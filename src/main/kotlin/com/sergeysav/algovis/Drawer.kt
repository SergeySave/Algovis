package com.sergeysav.algovis

/**
 * @author sergeys
 *
 * @constructor Creates a new Drawer
 */
expect class Drawer {
    var width: Int
    var height: Int
    fun fill(selection: Int, x: Int, y: Int, w: Int, h: Int)
}
