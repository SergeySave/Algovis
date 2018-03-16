package com.sergeysav.algovis

import org.w3c.dom.CENTER
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.CanvasTextAlign
import org.w3c.dom.CanvasTextBaseline
import org.w3c.dom.MIDDLE

/**
 * @author sergeys
 *
 * @constructor Creates a new Drawer
 */
actual class Drawer(val useDots: Boolean) {
    actual var width: Int = 0
    actual var height: Int = 0
    
    var rWidth: Int = 0
    var rHeight: Int = 0
    lateinit var context: CanvasRenderingContext2D
    
    actual fun beginDraw() {
        context.setTransform(rWidth.toDouble() / width, 0.0, 0.0, rHeight.toDouble() / height, 0.0, 0.0)
    }
    
    fun clear() {
        context.fillStyle = "white"
        context.fillRect(0.0, 0.0, width.toDouble(), height.toDouble())
    }
    
    actual fun fill(selection: Int, x: Int, y: Int, w: Int, h: Int) {
        context.fillStyle = colors[selection % colors.size]
        if (useDots) {
            context.fillRect(x.toDouble(), y.toDouble(), w.toDouble(), w.toDouble())
        } else {
            context.fillRect(x.toDouble(), y.toDouble(), w.toDouble(), h.toDouble())
        }
    }
    
    actual fun line(x1: Int, y1: Int, x2: Int, y2: Int) {
        context.strokeStyle = "black"
        context.lineWidth = 0.05
        context.beginPath()
        context.moveTo(x1.toDouble(), y1.toDouble())
        context.lineTo(x2.toDouble(), y2.toDouble())
        context.stroke()
    }
    
    actual fun text(string: String, x: Int, y: Int, w: Int, h: Int) {
        context.textBaseline = CanvasTextBaseline.MIDDLE
        context.textAlign = CanvasTextAlign.CENTER
        
        context.resetTransform()
        
        val xScreen = x * (rWidth.toDouble() / width) + 4
        val yScreen = y * (rHeight.toDouble() / height) + 4
        val wScreen = w * (rWidth.toDouble() / width) - 8
        val hScreen = h * (rHeight.toDouble() / height) - 8
        
        context.font = "${fitFontToRect(string, wScreen, hScreen, 10, 20f, 10f)}px sans-serif"
        context.fillStyle = "white"
        context.fillText(string, xScreen + wScreen / 2, yScreen + hScreen / 2)
        
        context.setTransform(rWidth.toDouble() / width, 0.0, 0.0, rHeight.toDouble() / height, 0.0, 0.0)
    }
    
    private tailrec fun fitFontToRect(string: String, w: Double, h: Double, remaining: Int, size: Float, step: Float,
                                      lastDir: Int? = null): Float {
        if (size < 0) {
            return size
        }
        
        context.font = "${size}px sans-serif"
        val width = context.measureText(string).width
        
        if (remaining == 0) {
            if (w > width) {
                return size
            } else {
                return size - step
            }
        }
        
        if (w == width) {
            return size
        }
        
        return if (w > width) { //Increase
            if (lastDir == null) {
                fitFontToRect(string, w, h, remaining - 1, size + step, step, 1)
            } else {
                if (lastDir == 1) {
                    fitFontToRect(string, w, h, remaining - 1, size + step, step, 1)
                } else {
                    fitFontToRect(string, w, h, remaining - 1, size + step / 2, step / 2, 1)
                }
            }
        } else { //Decrease
            if (lastDir == null) {
                fitFontToRect(string, w, h, remaining - 1, size - step, step, -1)
            } else {
                if (lastDir == -1) {
                    fitFontToRect(string, w, h, remaining - 1, size - step, step, -1)
                } else {
                    fitFontToRect(string, w, h, remaining - 1, size - step / 2, step / 2, -1)
                }
            }
        }
    }
    
    companion object {
        val colors = arrayOf("black", "red", "yellow", "green", "cyan", "blue", "orange")
    }
}