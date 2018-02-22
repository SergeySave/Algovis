@file:JvmName("Main")

package com.sergeysav.algovis

import javax.swing.JFrame
import javax.swing.WindowConstants

/**
 * @author sergeys
 */
fun main(args: Array<String>) {
    val jFrame = JFrame("AlgoVis")
    
    jFrame.add(DrawPanel())
    
    jFrame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
    jFrame.setSize(800, 800)
    
    jFrame.isVisible = true
    
    Thread {
        while (true) {
            Thread.sleep(1000 / 60)
            jFrame.repaint()
        }
    }.apply {
        name = "Refresh Thread"
        isDaemon = true
        start()
    }
}