@file:JvmName("Main")

package com.sergeysav.algovis

import com.sergeysav.algovis.structures.structures
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
import javax.swing.JSeparator
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
    
    val structures = structures
    
    jFrame.jMenuBar = jMenuBar
    
    var operationTime = 5.0
    var initializationSize = 100
    lateinit var dataSizeMenu: JMenuItem
    lateinit var opTimeMenu: JMenuItem
    lateinit var structureMenu: JMenu
    lateinit var simulationMenu: JMenu
    
    fun <T: Number> createSpinnerDialog(name: String, default: T, min: Comparable<T>, max: Comparable<T>, step: T,
                                        callback: (Any) -> Unit) {
        val jDialog = JDialog(jFrame)
        
        val jSpinner = JSpinner(SpinnerNumberModel(default, min, max, step))
    
        jDialog.addWindowListener(object: WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                jSpinner.commitEdit()
                callback(jSpinner.value)
            }
        })
        val jPanel = JPanel()
        
        jPanel.add(JLabel(name))
        jPanel.add(jSpinner)
        
        jDialog.add(jPanel)
        
        jDialog.pack()
        jDialog.isVisible = true
    }
    
    fun createDataSizeDialog() = createSpinnerDialog("Data Size", initializationSize, 1, 1 shl 20, 1) { result ->
        initializationSize = result as Int
        dataSizeMenu.text = "Data Size: $initializationSize"
    }
    
    fun createOpTimeDialog() =
            createSpinnerDialog("Operation Time (ms)", operationTime, 0.0, (1 shl 20).toDouble(), 0.1) { result ->
                operationTime = result as Double
                drawPanel.structure.delayMillis = operationTime
                opTimeMenu.text = "Operation Time: $operationTime ms"
            }
    
    fun updateSimulationMenu() {
        simulationMenu.removeAll()
        simulationMenu.apply {
            val algorithm = drawPanel.algorithm
            if (algorithm == null) {
                add("No Algorithm Selected")
            } else if (!algorithm.complete) {
                if (!algorithm.running) {
                    add(JMenuItem("Start Algorithm").apply {
                        addActionListener {
                            drawPanel.job?.cancel()
                            drawPanel.job = launch {
                                algorithm.run(::isActive)
                                updateSimulationMenu()
                            }
                            launch {
                                kotlinx.coroutines.experimental.delay(50)
                                updateSimulationMenu()
                            }
                        }
                    })
                } else {
                    add(JMenuItem("Stop Algorithm").apply {
                        addActionListener {
                            drawPanel.job?.cancel()
                            launch {
                                kotlinx.coroutines.experimental.delay(50)
                                algorithm.running = false
                                updateSimulationMenu()
                            }
                        }
                    })
                }
            } else {
                if (algorithm.running) {
                    add("Algorithm Finished")
                } else {
                    add("Algorithm Aborted")
                }
            }
        }
    }
    
    drawPanel.completionCallback = { updateSimulationMenu() }
    
    fun updateStructureMenu() {
        structureMenu.removeAll()
        structureMenu.apply {
            add(JMenu(if (!drawPanel.structure.initialized) "Initialize" else "Reinitialize").apply {
                for (initializionCondition in drawPanel.structure.initializationConditions) {
                    add(JMenuItem(initializionCondition.name).apply {
                        addActionListener {
                            drawPanel.job?.cancel()
                            initializionCondition(initializationSize)
                            updateStructureMenu()
                        }
                    })
                }
            })
            if (drawPanel.structure.initialized) {
                add(JMenu("Algorithm").apply {
                    val group = ButtonGroup()
                    for ((name, params, creator) in drawPanel.structure.algorithms) {
                        if (params.isEmpty()) {
                            add(JMenuItem(name).apply {
                                group.add(this)
                                addActionListener {
                                    drawPanel.job?.cancel()
                                    drawPanel.algorithm = creator(intArrayOf())
                                    updateStructureMenu()
                                }
                            })
                        } else {
                            add(JMenu(name).apply {
                                group.add(this)
                                val paramVals = IntArray(params.size) { i -> params[i].default }
                                
                                for (i in 0 until params.size) {
                                    add(JMenuItem("${params[i].name}: ${paramVals[i]}").apply {
                                        addActionListener {
                                            createSpinnerDialog(params[i].name, paramVals[i], params[i].min,
                                                                params[i].max, params[i].step) { result ->
                                                paramVals[i] = result as Int
                                                this.text = "${params[i].name}: ${paramVals[i]}"
                                            }
                                        }
                                    })
                                }
                                
                                add(JSeparator())
                                
                                add(JMenuItem("Select").apply {
                                    addActionListener {
                                        drawPanel.job?.cancel()
                                        drawPanel.algorithm = creator(paramVals)
                                        updateStructureMenu()
                                    }
                                })
                            })
                        }
                    }
                })
            }
        }
        updateSimulationMenu()
    }
    
    jMenuBar.apply {
        add(JMenu("Simulation").apply {
            simulationMenu = this
            updateSimulationMenu()
        })
        add(JMenu("Current Structure").apply {
            structureMenu = this
            add(JMenuItem("No Structure Selected"))
        })
        add(JMenu("Structures").apply {
            val group = ButtonGroup()
            for ((name, generator) in structures) {
                add(JRadioButtonMenuItem(name).apply {
                    group.add(this)
                    addActionListener {
                        drawPanel.job?.cancel()
                        drawPanel.structure = generator()
                        drawPanel.structure.delayMillis = operationTime
                        drawPanel.algorithm = null
                        updateStructureMenu()
                    }
                })
            }
        })
        add(JMenu("Options").apply {
            add(JMenuItem("Operation Time: $operationTime ms").apply {
                opTimeMenu = this
                addActionListener {
                    createOpTimeDialog()
                }
            })
            add(JMenuItem("Initialization Size: $initializationSize").apply {
                dataSizeMenu = this
                addActionListener {
                    createDataSizeDialog()
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