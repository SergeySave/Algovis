package com.sergeysav.algovis

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Font
import java.awt.Graphics2D
import java.awt.font.TextAttribute
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
    private var font: Font
    
    actual var width: Int = 0
    actual var height: Int = 0
    
    init {
        font = Font(mapOf(TextAttribute.KERNING to TextAttribute.KERNING_ON,
                          TextAttribute.WIDTH to TextAttribute.WIDTH_CONDENSED,
                          TextAttribute.TRACKING to -0.1f))
    }
    
    actual fun beginDraw() {
        val transform = AffineTransform()
        transform.translate(0.0, tDown)
        transform.scale(rWidth.toDouble() / width, rHeight.toDouble() / height)
        g.transform = transform
    
        font = Font(mapOf(TextAttribute.KERNING to TextAttribute.KERNING_ON,
                          TextAttribute.WIDTH to TextAttribute.WIDTH_CONDENSED,
                          TextAttribute.TRACKING to -0.1f
        ))
    }
    
    actual fun fill(selection: Int, x: Int, y: Int, w: Int, h: Int) {
        if (useDots) {
            g.color = colors[selection % colors.size]
            g.fillRect(x, y, w, w)
        } else {
            g.color = colors[selection % colors.size] //.withAlpha((minOf(wVal, 1.0) * 255).toInt())
            g.fillRect(x, y, w, h)
        }
    }
    
    actual fun line(x1: Int, y1: Int, x2: Int, y2: Int) {
        g.color = Color.WHITE
        g.stroke = BasicStroke(0.05f)
        g.drawLine(x1, y1, x2, y2)
    }
    
    //Text is best drawn without the transform
    actual fun text(string: String, x: Int, y: Int, w: Int, h: Int) {
        val trans = g.transform
        
        g.transform = AffineTransform()
        
        val xScreen = x * trans.scaleX + trans.translateX + 2
        val yScreen = y * trans.scaleY + trans.translateY + 4
        val wScreen = w * trans.scaleX - 8
        val hScreen = h * trans.scaleY - 8
        
        val size = fitFontToRect(string, wScreen, hScreen, 10, 10f, 10f)
        val newFont = font.deriveFont(size) //font.deriveFont(1.703125f)//
        
        val fm = g.getFontMetrics(newFont)
        val r = fm.getStringBounds(string, g)
        
        g.color = Color.BLACK
        g.font = newFont
        g.drawString(string, (xScreen + (wScreen - r.width) / 2 - r.x).toFloat(),
                     (yScreen + (hScreen - fm.height) / 2f + fm.ascent).toFloat())
        
        g.transform = trans
    }
    
    private tailrec fun fitFontToRect(string: String, w: Double, h: Double, remaining: Int, size: Float, step: Float,
                                      lastDir: Int? = null): Float {
        if (size < 0) {
            return size
        }
        
        val fm = g.getFontMetrics(font.deriveFont(size))
        val r = fm.getStringBounds(string, g)
        
        if (remaining == 0) {
            if (w > r.width && h > r.height) {
                return size
            } else {
                return size - step
            }
        }
        
        if ((w == r.width || h == r.height) && (w >= r.width && h >= r.height)) {
            return size
        }
        
        return if (w > r.width && h > r.height) { //Increase
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
        val colors = arrayOf(Color.WHITE, Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.ORANGE)
    }
    
    
    private fun Color.withAlpha(alpha: Int): Color = Color(red, green, blue, alpha)
}