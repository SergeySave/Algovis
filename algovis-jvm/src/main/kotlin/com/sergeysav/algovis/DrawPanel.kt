package com.sergeysav.algovis

import kotlinx.coroutines.experimental.Job
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.JPanel

/**
 * @author sergeys
 *
 * @constructor Creates a new DrawPanel
 */
class DrawPanel(private val jMenuBar: JMenuBar): JPanel() {
    
    var algorithm: Algorithm<*> = NullAlgorithm<Int>()
    var job: Job? = null
    val drawer: Drawer = Drawer(false)
    
    lateinit var statusItem: JMenuItem
    
    override fun paint(g1: Graphics) {
        val g = g1 as Graphics2D
        
        drawer.rWidth = width
        drawer.rHeight = height
        drawer.tDown = jMenuBar.height.toDouble()
        drawer.g = g
        
        g.color = Color.BLACK
        g.fillRect(0, 0, width, height)
        g.color = Color.WHITE
    
        algorithm.doDraw(drawer)
    
        if (job?.isActive == true) {
            statusItem.text = "Status: Running"
        } else {
            if (algorithm.complete) {
                statusItem.text = "Status: Complete"
            } else {
                statusItem.text = "Status: Cancelled"
            }
        }
    }
    
}
