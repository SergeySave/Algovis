package com.sergeysav.algovis.algorithms.bst

import com.sergeysav.algovis.Drawer
import com.sergeysav.algovis.algorithms.Algorithm
import com.sergeysav.algovis.structures.BSTStructure

/**
 * @author sergeys
 */
abstract class BSTAlgorithm(val bst: BSTStructure): Algorithm() {
    
    private var visited = mutableMapOf<BSTStructure.Node, Int>()
    private var visitedDrawing = mutableMapOf<BSTStructure.Node, Int>()
    
    abstract fun getType(node: BSTStructure.Node): Int
    
    fun setVisited(node: BSTStructure.Node?, type: Int = 0) = synchronized(visited) {
        if (node != null) {
            visited[node] = type
        }
    }
    
    override fun initDraw(drawer: Drawer) {
        bst.initDraw(drawer)
    }
    
    override fun doDraw(drawer: Drawer) {
        
        val reading = visited
        synchronized(visited) {
            visited = visitedDrawing
            visitedDrawing = reading
        }
        
        for ((node, type) in reading) {
            node.draw(drawer, type)
        }
        
        fun drawRecur(node: BSTStructure.Node?) {
            if (node != null) {
                node.draw(drawer, getType(node))
                drawRecur(node.left)
                drawRecur(node.right)
            }
        }
        
        drawRecur(bst.root)
        
        reading.clear()
    }
}