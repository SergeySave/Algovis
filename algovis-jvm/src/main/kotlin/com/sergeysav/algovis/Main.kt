@file:JvmName("Main")

package com.sergeysav.algovis

import kotlinx.coroutines.experimental.launch
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.ButtonGroup
import javax.swing.JDialog
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JMenu
import javax.swing.JMenuBar
import javax.swing.JMenuItem
import javax.swing.JPanel
import javax.swing.JRadioButtonMenuItem
import javax.swing.JSpinner
import javax.swing.SpinnerNumberModel
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
    
    var generator: (Array<Int>, Double) -> Algorithm<*> = { _, _ -> NullAlgorithm<Int>() }
    var condition: (Int) -> Array<Int> = conditions.keys.first()
    
    var operationTime = 5.0
    
    jFrame.jMenuBar = jMenuBar
    
    var dataSize = 100
    lateinit var dataSizeMenu: JMenuItem
    lateinit var opTimeMenu: JMenuItem
    
    fun createDataSizeDialog() {
        val jDialog = JDialog(jFrame)
        
        val jSpinner = JSpinner(SpinnerNumberModel(dataSize, 1, 1 shl 20, 1))
        
        jDialog.addWindowListener(object: WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                jSpinner.commitEdit()
                dataSize = jSpinner.value as Int
                dataSizeMenu.text = "Data Size: $dataSize"
            }
        })
        val jPanel = JPanel()
        
        jPanel.add(JLabel("Data Size"))
        jPanel.add(jSpinner)
        
        jDialog.add(jPanel)
        
        jDialog.pack()
        jDialog.isVisible = true
    }
    
    fun createOpTimeDialog() {
        val jDialog = JDialog(jFrame)
        
        val jSpinner = JSpinner(SpinnerNumberModel(operationTime, 0.0, (1 shl 20).toDouble(), 0.1))
        
        jDialog.addWindowListener(object: WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                jSpinner.commitEdit()
                operationTime = jSpinner.value as Double
                opTimeMenu.text = "Operation Time: $operationTime ms"
            }
        })
        val jPanel = JPanel()
        
        jPanel.add(JLabel("Operation Time (ms)"))
        jPanel.add(jSpinner)
        
        jDialog.add(jPanel)
        
        jDialog.pack()
        jDialog.isVisible = true
    }

    jMenuBar.apply {
        add(JMenu("Simulation").apply {
            add(JMenuItem("Start").apply {
                addActionListener {
                    val algorithm = generator(condition(dataSize), operationTime)
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
            add(JMenuItem("Status: Not Running").apply {
                drawPanel.statusItem = this
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
            add(JMenuItem("Data Size: $dataSize").apply {
                dataSizeMenu = this
                addActionListener {
                    createDataSizeDialog()
                }
            })
            add(JMenuItem("Operation Time: $operationTime ms").apply {
                opTimeMenu = this
                addActionListener {
                    createOpTimeDialog()
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