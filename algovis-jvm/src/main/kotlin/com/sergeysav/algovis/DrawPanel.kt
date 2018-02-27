package com.sergeysav.algovis

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
    
    var algorithm: Algorithm<*> = NullAlgorithm<Int>()
    var job: Job? = null
    val drawer: Drawer = Drawer(false)
    
    val generators = arrayOf(
            arrayGenerator { arr -> MergeSort(arr) },
            arrayGenerator { arr -> ParMergeSort(arr) }
    )
    
    private inline fun arrayGenerator(crossinline constructor: (DelayedArray<Int>) -> Algorithm<Int>) = { size: Int ->
        val array = (0 until size).toList().shuffled().toTypedArray()
        algorithm = constructor(DelayedArray(array, 1, 1))
        algorithm
    }
    
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
    }
}
