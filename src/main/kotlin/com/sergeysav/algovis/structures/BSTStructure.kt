package com.sergeysav.algovis.structures

import com.sergeysav.algovis.Delayer
import com.sergeysav.algovis.Drawer
import com.sergeysav.algovis.algorithms.bst.AddAlgorithm
import com.sergeysav.algovis.toThe
import kotlin.math.max

/**
 * @author sergeys
 *
 * @constructor Creates a new BSTStructure
 */
class BSTStructure(override val delayMillis: Double): Structure() {
    val delayer = Delayer()
    
    override val algorithms: List<AlgorithmReference> = listOf(
            AR("Add", listOf(Param("value"))) { params -> AddAlgorithm(this, params[0]) }
    )
    
    override val initializationConditions: List<InitializationCondition> = listOf(
            ic("Empty") {
                root = null
            },
            ic("Balanced") {
                root = Node(0).apply {
                    left = Node(-10).apply {
                        left = Node(-20)
                        right = Node(-5)
                    }
                    right = Node(10).apply {
                        left = Node(5)
                        right = Node(20)
                    }
                }
            },
            ic("Degenerate") {
                root = Node(0).apply {
                    right = Node(10).apply {
                        right = Node(20).apply {
                            right = Node(30)
                        }
                    }
                }
            }
    )
    
    override var initialized: Boolean = false
    
    override fun initDraw(drawer: Drawer) {
        drawer.width = (2 toThe (height + 1)) * 4
        drawer.height = (3 + height) * 4 - 2 // height + 1 + 2
        
        drawer.beginDraw()
    }
    
    override fun draw(drawer: Drawer) {
        root.drawTree(drawer, 0, drawer.width, 4, null)
    }
    
    private fun Node?.drawTree(drawer: Drawer, left: Int, right: Int, y: Int, parentX: Int?) {
        if (this != null) {
            val pos = (left + right) / 2
            
            this.x = pos
            this.y = y
            
            if (parentX != null) {
                drawer.line(pos, y, parentX, y - 2)
            }
            
            this.left.drawTree(drawer, left, pos, y + 4, pos)
            this.right.drawTree(drawer, pos, right, y + 4, pos)
            
            this.draw(drawer, 0)
        }
    }
    
    var root: Node? = null
    
    val height: Int
        get() {
            return root?.height ?: -1
        }
    
    private fun ic(name: String, func: (Int) -> Unit) = IC(name, func).apply { initialized = true }
    
    inner class Node(var data: Int) {
        var left: Node? = null
        var right: Node? = null
        var x: Int = 0
        var y: Int = 0
        
        val height: Int
            get() {
                return max(left?.height ?: -1, right?.height ?: -1) + 1
            }
        
        suspend fun setLeft(node: Node?) {
            left = node
            delayer.doDelay(delayMillis)
        }
        
        suspend fun setRight(node: Node?) {
            right = node
            delayer.doDelay(delayMillis)
        }
        
        suspend fun getLeft(): Node? {
            delayer.doDelay(delayMillis)
            return left
        }
        
        suspend fun getRight(): Node? {
            delayer.doDelay(delayMillis)
            return right
        }
        
        fun draw(drawer: Drawer, color: Int) {
            drawer.fill(color, x - 1, y, 2, 2)
            drawer.text(data.toString(), x - 1, y, 2, 2)
        }
    }
}