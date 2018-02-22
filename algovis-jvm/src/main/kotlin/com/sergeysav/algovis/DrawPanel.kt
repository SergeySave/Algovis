package com.sergeysav.algovis

import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.util.Random
import javax.swing.JPanel

/**
 * @author sergeys
 *
 * @constructor Creates a new DrawPanel
 */
class DrawPanel: JPanel() {
    
    var array: Array<Int> = arrayOf()
    var algorithm: Algorithm<Int> = NullAlgorithm()
    var maxValue = 2
    var uuids = algorithm.getUUIDs()
    var job: Job? = null
    val rand = Random()
    val drawer: Drawer = Drawer(false)
    
    init {
        addMouseListener(object: MouseAdapter() {
            override fun mouseReleased(e: MouseEvent?) {
                job?.cancel()
                job = launch {
                    array = (0 .. 999).toList().shuffled().toTypedArray()
                    algorithm = MergeSort(DelayedArray(array, 5, 5))
                    maxValue = array.max()!! + 2
                    uuids = algorithm.getUUIDs()
                    algorithm.run()
                }
            }
        })
    }
    
    override fun paint(g1: Graphics) {
        val g = g1 as Graphics2D
        
        drawer.rWidth = width
        drawer.rHeight = height
        drawer.g = g
        
        g.color = Color.BLACK
        g.fillRect(0, 0, width, height)
        g.color = Color.WHITE
        
        if (uuids.size > 0) {
            algorithm.doDraw(drawer)
        }
    }
}
