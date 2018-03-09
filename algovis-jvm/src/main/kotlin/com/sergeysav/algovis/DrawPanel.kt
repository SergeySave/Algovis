package com.sergeysav.algovis

import com.sergeysav.algovis.algorithms.Algorithm
import com.sergeysav.algovis.structures.NullStructure
import com.sergeysav.algovis.structures.Structure
import kotlinx.coroutines.experimental.Job
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JMenuBar
import javax.swing.JPanel

/**
 * @author sergeys
 *
 * @constructor Creates a new DrawPanel
 */
class DrawPanel(private val jMenuBar: JMenuBar): JPanel() {
    
    var structure: Structure = NullStructure
    var algorithm: Algorithm? = null
    var completionCallback: (Boolean?) -> Unit = {}
    
    var job: Job? = null
    var lastActive: Boolean? = null
    val drawer: Drawer = Drawer(false)
    
    override fun paint(g1: Graphics) {
        val g = g1 as Graphics2D
        
        drawer.rWidth = width
        drawer.rHeight = height
        drawer.tDown = jMenuBar.height.toDouble()
        drawer.g = g
        
        g.color = Color.BLACK
        g.fillRect(0, 0, width, height)
        g.color = Color.WHITE
    
        algorithm?.initDraw(drawer) ?: run {
            structure.initDraw(drawer)
        }
        structure.draw(drawer)
        algorithm?.doDraw(drawer)
    
        if (job?.isActive != lastActive) {
            lastActive = job?.isActive
            completionCallback(lastActive)
        }
    }
    
}
