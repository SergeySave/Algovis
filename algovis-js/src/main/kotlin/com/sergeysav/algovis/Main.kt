package com.sergeysav.algovis

import com.sergeysav.algovis.structures.structures
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.GlobalEventHandlers
import org.w3c.dom.Node
import kotlin.browser.window

/**
 * @author sergeys
 */
fun main(args: Array<String>) {
    val model = Model()
    DrawController(model)() //Invoke (start) the Draw Controller
    
    var operationTime = 5.0
    var initializationSize = 100
    var algorithmName: String? = null
    
    with(model) {
        dataSizeMenu.innerText = "Initialization Size: $initializationSize"
        dataSizeMenu.id = ""
        dataSizeMenu.onclick = { _ ->
            promptInt("Data Size", 1, 1 shl 20, initializationSize) {
                initializationSize = it
                dataSizeMenu.innerText = "Initialization Size: $initializationSize"
            }
        }
        
        opTimeMenu.textContent = "Operation Time: $operationTime ms"
        opTimeMenu.id = ""
        opTimeMenu.onclick = { _ ->
            promptDouble("Operation Time (ms)", 0.0, (1 shl 20).toDouble(), operationTime) {
                operationTime = it
                structure.delayMillis = operationTime
                opTimeMenu.textContent = "Operation Time: $operationTime ms"
            }
        }
        
        simulationMenu.id = ""
        simulationMenuItem.remove()
        simulationMenuItem.id = ""
        
        structureMenu.id = ""
        structureMenuSubSubItem.remove()
        structureMenuSubSubItem.id = ""
        structureMenuSubItem.remove()
        structureMenuSubItem.id = ""
        structureMenuSubListItem.remove()
        structureMenuSubListItem.id = ""
        structureMenuItem.remove()
        structureMenuItem.id = ""
        
        structureList.id = ""
        structureListItem.remove()
        structureListItem.id = ""
        
        fun updateSimulationMenu() {
            //Remove all children elements
            while (simulationMenu.lastElementChild != null) {
                simulationMenu.removeChild(simulationMenu.lastElementChild as Node)
            }
            
            val alg = algorithm
            if (alg == null) {
                simulationMenu.appendChild(simulationMenuItem.cloneNode(true).apply {
                    textContent = "No Algorithm Selected"
                })
            } else if (!alg.complete) {
                if (!alg.running) {
                    simulationMenu.appendChild(simulationMenuItem.cloneNode(true).apply {
                        textContent = "Start Algorithm"
                        (this.unsafeCast<GlobalEventHandlers>()).onclick = {
                            job?.cancel()
                            job = launch {
                                alg.run(::isActive)
                                updateSimulationMenu()
                            }
                            launch {
                                delay(50)
                                updateSimulationMenu()
                            }
                        }
                    })
                } else {
                    simulationMenu.appendChild(simulationMenuItem.cloneNode(true).apply {
                        textContent = "Stop Algorithm"
                        (this.unsafeCast<GlobalEventHandlers>()).onclick = {
                            job?.cancel()
                            launch {
                                delay(50)
                                alg.running = false
                                updateSimulationMenu()
                            }
                        }
                    })
                }
            } else {
                simulationMenu.appendChild(simulationMenuItem.cloneNode(true).apply {
                    textContent = if (alg.running) "Algorithm Finished" else "Algorithm Aborted"
                })
            }
        }
        
        completionCallback = { updateSimulationMenu() }
        
        fun updateStructureMenu() {
            //Remove all children elements
            while (structureMenu.lastElementChild != null) {
                structureMenu.removeChild(structureMenu.lastElementChild as Node)
            }
            
            //Initialization Menu
            structureMenu.appendChild(structureMenuItem.cloneNode(true).apply {
                firstChild!!.textContent = if (structure.initialized) "Reinitialize" else "Initialize"
                for (initializionCondition in structure.initializationConditions) {
                    lastElementChild!!.appendChild(structureMenuSubItem.cloneNode(true).apply {
                        textContent = initializionCondition.name
                        (this.unsafeCast<GlobalEventHandlers>()).onclick = {
                            job?.cancel()
                            initializionCondition(initializationSize)
                            updateStructureMenu()
                        }
                    })
                }
            })
            
            if (structure.initialized) {
                structureMenu.appendChild(structureMenuItem.cloneNode(true).apply {
                    firstChild!!.textContent = "Algorithm"
                    for ((name, params, creator) in structure.algorithms) {
                        if (params.isEmpty()) {
                            lastElementChild!!.appendChild(structureMenuSubItem.cloneNode(true).apply {
                                textContent = name
                                if (algorithmName == name) {
                                    classList.add("active")
                                }
                                (this.unsafeCast<GlobalEventHandlers>()).onclick = {
                                    job?.cancel()
                                    algorithm = creator(intArrayOf())
                                    algorithmName = name
                                    updateStructureMenu()
                                }
                            })
                        } else {
                            lastElementChild!!.appendChild(structureMenuSubListItem.cloneNode(true).apply {
                                firstChild!!.textContent = name
                                if (algorithmName == name) {
                                    classList.add("active")
                                }
                                
                                val paramVals = IntArray(params.size) { i -> params[i].default }
                                
                                for (i in 0 until params.size) {
                                    lastElementChild!!.appendChild(structureMenuSubSubItem.cloneNode(true).apply {
                                        textContent = "${params[i].name}: ${paramVals[i]}"
                                        (this.unsafeCast<GlobalEventHandlers>()).onclick = { _ ->
                                            promptInt(params[i].name, params[i].min, params[i].max, paramVals[i]) {
                                                paramVals[i] = it
                                                this.textContent = "${params[i].name}: ${paramVals[i]}"
                                            }
                                        }
                                    })
                                }
                                
                                lastElementChild!!.appendChild(structureMenuSubSubItem.cloneNode(true).apply {
                                    textContent = "------"
                                })
                                
                                lastElementChild!!.appendChild(structureMenuSubSubItem.cloneNode(true).apply {
                                    textContent = "Select"
                                    (this.unsafeCast<GlobalEventHandlers>()).onclick = {
                                        job?.cancel()
                                        algorithm = creator(paramVals)
                                        algorithmName = name
                                        updateStructureMenu()
                                    }
                                })
                            })
                        }
                    }
                })
            }
            updateSimulationMenu()
        }
        
        structureMenu.appendChild(structureMenuItem.cloneNode(true).apply {
            textContent = "No Structure Selected"
        })
        
        for ((name, generator) in structures) {
            structureList.appendChild(structureListItem.cloneNode(true).apply {
                textContent = name
                (this.unsafeCast<GlobalEventHandlers>()).onclick = {
                    structure = generator()
                    job?.cancel()
                    structure.delayMillis = operationTime
                    algorithm = null
                    algorithmName = null
                    updateStructureMenu()
                }
            })
        }
    }
}

inline fun <reified T> Document.elementWithId(name: String) = this.getElementById(name) as T

fun promptInt(message: String, min: Int, max: Int, default: Int = 0, callback: (Int) -> Unit) {
    var result: String?
    do {
        result = window.prompt("$message (integer between $min and $max)", default.toString())
    } while (result != null && (result.toIntOrNull() == null || result.toInt().toString() != result) && result.toInt() !in min .. max)
    if (result != null) {
        callback(result.toInt())
    }
}


fun promptDouble(message: String, min: Double, max: Double, default: Double = 0.0, callback: (Double) -> Unit) {
    var result: String?
    do {
        result = window.prompt("$message (Must be between $min and $max)", default.toString())
    } while (result != null && (result.toDoubleOrNull() == null || result.toDouble().toString() != result) && result.toDouble() !in min .. max)
    if (result != null) {
        callback(result.toDouble())
    }
}

val Node.lastElementChild
    get() = (this as Element).lastElementChild

val Node.classList
    get() = (this as Element).classList
