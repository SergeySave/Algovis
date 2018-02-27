package com.sergeysav.algovis

import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.AffineTransform

/**
 * @author sergeys
 *
 * @constructor Creates a new Drawer
 */
actual class Drawer(val useDots: Boolean) {
    lateinit var g: Graphics2D
    var rWidth: Int = 0
    var rHeight: Int = 0
    var tDown: Double = 0.0
    
    actual var width: Int = 0
    actual var height: Int = 0
    
    actual fun beginDraw() {
        val transform = AffineTransform()
        transform.translate(0.0, tDown)
        transform.scale(rWidth.toDouble() / width, rHeight.toDouble() / height)
        g.transform = transform
    }
    
    actual fun fill(selection: Int, x: Int, y: Int, w: Int, h: Int) {
        val wVal = w.toDouble() * rWidth / width
        if (useDots) {
            g.color = colors[selection % colors.size]
            g.fillRect(x, y, w, w)
            //            g.fillRect((x.toDouble() * rWidth / width).toInt(), (y.toDouble() * rHeight / height).toInt(),
            //                       maxOf(wVal.toInt(), 1), maxOf(wVal.toInt(), 1))
        } else {
            g.color = colors[selection % colors.size] //.withAlpha((minOf(wVal, 1.0) * 255).toInt())
            g.fillRect(x, y, w, h)
            //            g.fillRect((x.toDouble() * rWidth / width).toInt(), (y.toDouble() * rHeight / height).toInt(),
            //                       maxOf(wVal.toInt(), 1), maxOf((h.toDouble() * rHeight / height).toInt(), 1))
        }
    }
    
    companion object {
        val colors = arrayOf(Color.WHITE, Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.ORANGE)
    }
    
    
    private fun Color.withAlpha(alpha: Int): Color = Color(red, green, blue, alpha)
}