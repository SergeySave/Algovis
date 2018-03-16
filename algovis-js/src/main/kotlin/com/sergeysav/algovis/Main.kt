package com.sergeysav.algovis

import com.sergeysav.algovis.algorithms.Algorithm
import com.sergeysav.algovis.structures.structures
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.document

/**
 * @author sergeys
 */
var alg: Algorithm? = null
var job: Job? = null

val algorithmButton = document.getElementById("algorithm") as HTMLButtonElement

fun main(args: Array<String>) {
    
    val elementById = document.getElementById("canvas") as HTMLCanvasElement
    val context: CanvasRenderingContext2D = elementById.getContext("2d") as CanvasRenderingContext2D
    
    val structure = structures["Binary Search Tree"]!!()
    structure.initializationConditions[1](8)
    structure.delayMillis = 500.0
    
    val drawer = Drawer(false)
    drawer.context = context
    drawer.rWidth = elementById.width
    drawer.rHeight = elementById.height
    
    alg = structure.algorithms[1].creator(intArrayOf(0))
    
    launch {
        while (isActive) {
            val algorithm: Algorithm? = alg
            
            algorithm?.initDraw(drawer) ?: run {
                structure.initDraw(drawer)
            }
            
            drawer.clear()
            
            structure.draw(drawer)
            algorithm?.doDraw(drawer)
            
            delay(1000 / 30)
        }
    }
    
}

fun onStartButton() {
    if (algorithmButton.textContent == "Start") {
        algorithmButton.textContent = "Stop"
        job?.cancel()
        job = launch {
            alg?.run(::isActive)
        }
    } else if (algorithmButton.textContent == "Stop") {
        algorithmButton.textContent = "Start"
        job?.cancel()
    }
}