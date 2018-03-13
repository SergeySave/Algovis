package com.sergeysav.algovis

/**
 * @author sergeys
 *
 * @constructor Creates a new Drawer
 */
actual class Drawer {
    actual var width: Int = 0
    actual var height: Int = 0
    
    actual fun beginDraw() {
    }
    
    actual fun fill(selection: Int, x: Int, y: Int, w: Int, h: Int) {}
    actual fun line(x1: Int, y1: Int, x2: Int, y2: Int) {}
    actual fun text(string: String, x: Int, y: Int, w: Int, h: Int) {}
}