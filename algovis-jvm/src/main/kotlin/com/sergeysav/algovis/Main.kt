@file:JvmName("Main")

package com.sergeysav.algovis

import kotlinx.coroutines.experimental.launch
import javax.swing.JFrame
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.WindowConstants

/**
 * @author sergeys
 */
fun main(args: Array<String>) {
    val jFrame = JFrame("AlgoVis")
    
    val jMenuBar = JMenuBar()
    
    val drawPanel = DrawPanel(jMenuBar)
    val generators = drawPanel.generators
    
    jFrame.jMenuBar = jMenuBar
    
    jMenuBar.apply {
        add(JMenu("Simulation").apply {
            add(JMenuItem("Start").apply {
                addActionListener {
                    val algorithm = generators[1](1000)
                    drawPanel.job?.cancel()
                    drawPanel.job = launch { algorithm.run() }
                }
            })
        })
    }
    
    jFrame.add(drawPanel)
    
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