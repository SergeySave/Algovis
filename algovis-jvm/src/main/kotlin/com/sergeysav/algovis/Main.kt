@file:JvmName("Main")

package com.sergeysav.algovis

import kotlinx.coroutines.experimental.launch
import javax.swing.ButtonGroup
import javax.swing.JFrame
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.JRadioButtonMenuItem
import javax.swing.WindowConstants

/**
 * @author sergeys
 */
fun main(args: Array<String>) {
    val jFrame = JFrame("AlgoVis")
    
    val jMenuBar = JMenuBar()
    
    val drawPanel = DrawPanel(jMenuBar)
    
    val algorithms = algorithms
    val conditions = conditions
    
    var generator: (Array<Int>) -> Algorithm<*> = { i -> NullAlgorithm<Int>() }
    var condition: (Int) -> Array<Int> = conditions.keys.first()
    
    jFrame.jMenuBar = jMenuBar
    
    jMenuBar.apply {
        add(JMenu("Simulation").apply {
            add(JMenuItem("Start").apply {
                addActionListener {
                    val algorithm = generator(condition(1000))
                    drawPanel.algorithm = algorithm
                    drawPanel.job?.cancel()
                    drawPanel.job = launch { algorithm.run(::isActive) }
                }
            })
            add(JMenuItem("Stop").apply {
                addActionListener {
                    drawPanel.job?.cancel()
                }
            })
        })
        add(JMenu("Algorithm").apply {
            add(JMenu("Algorithm Selection").apply {
                val group = ButtonGroup()
                algorithms.forEach { (gen, name) ->
                    add(JRadioButtonMenuItem(name).apply {
                        group.add(this)
                        addActionListener {
                            generator = gen
                        }
                    })
                }
            })
            add(JMenu("Starting Conditions").apply {
                val group = ButtonGroup()
                conditions.forEach { (cond, name) ->
                    add(JRadioButtonMenuItem(name).apply {
                        group.add(this)
                        addActionListener {
                            condition = cond
                        }
                        if (cond == condition) {
                            doClick()
                        }
                    })
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