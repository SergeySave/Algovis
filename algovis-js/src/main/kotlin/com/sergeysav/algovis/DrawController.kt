package com.sergeysav.algovis

import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.Element
import org.w3c.dom.HTMLCanvasElement
import kotlin.browser.document
import kotlin.browser.window
import kotlin.math.roundToInt

class DrawController(val model: Model) {
    private val canvas: HTMLCanvasElement = document.elementWithId("canvas")
    private val renderContext: CanvasRenderingContext2D = canvas.getContext("2d") as CanvasRenderingContext2D
    
    private val drawer = Drawer(false)
    
    operator fun invoke() = launch {
        while (isActive) {
            with(model) {
                val controlsStyle = window.getComputedStyle(controls)
                val mainAreaStyle = window.getComputedStyle(mainArea)
                canvas.width = mainArea.clientWidth -
                        (mainAreaStyle.paddingLeft.substring(0, mainAreaStyle.paddingLeft.length - 2).toInt() +
                         mainAreaStyle.paddingRight.substring(0, mainAreaStyle.paddingRight.length - 2).toInt())
                canvas.height = window.innerHeight -
                        (controls as Element).getBoundingClientRect().height.roundToInt() -
                        7 * (controlsStyle.marginTop.substring(0, controlsStyle.marginTop.length - 2).toInt() +
                             controlsStyle.marginBottom.substring(0, controlsStyle.marginBottom.length - 2).toInt())
                
                drawer.context = renderContext
                drawer.rWidth = canvas.width
                drawer.rHeight = canvas.height
                
                drawer.clear()
                
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
            
            delay(1000 / 30)
        }
    }
}