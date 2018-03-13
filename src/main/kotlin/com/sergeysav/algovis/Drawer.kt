package com.sergeysav.algovis

/**
 * @author sergeys
 *
 * @constructor Creates a new Drawer
 */
expect class Drawer {
    var width: Int
    var height: Int
    
    fun beginDraw()
    
    fun fill(selection: Int, x: Int, y: Int, w: Int, h: Int)
    
    fun line(x1: Int, y1: Int, x2: Int, y2: Int)
    
    fun text(string: String, x: Int, y: Int, w: Int, h: Int)
}
