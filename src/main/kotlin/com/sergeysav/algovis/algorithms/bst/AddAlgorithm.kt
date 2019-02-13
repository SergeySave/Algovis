package com.sergeysav.algovis.algorithms.bst

import com.sergeysav.algovis.Drawer
import com.sergeysav.algovis.algorithms.Algorithm
import com.sergeysav.algovis.structures.BSTStructure
import kotlinx.coroutines.isActive

/**
 * @author sergeys
 *
 * @constructor Creates a new AddAlgorithm
 */
class AddAlgorithm(val bst: BSTStructure, val toAdd: Int): Algorithm() {
    
    var selectedNode: BSTStructure.Node? = null
    
    override suspend fun execute() {
        bst.root = bst.root.add(toAdd)
        bst.delayer.doDelay(bst.delayMillis)
        selectedNode = null
    }
    
    private suspend fun BSTStructure.Node?.add(toAdd: Int): BSTStructure.Node? {
        if (!isActive) {
            return this
        }
        this@AddAlgorithm.selectedNode = this
        if (this == null) {
            return bst.Node(toAdd)
        }
        if (toAdd < data) {
            val node = getLeft().add(toAdd)
            this@AddAlgorithm.selectedNode = node
            setLeft(node)
        } else if (data < toAdd) {
            val node = getRight().add(toAdd)
            this@AddAlgorithm.selectedNode = node
            setRight(node)
        }
        this@AddAlgorithm.selectedNode = this
        return this
    }
    
    override fun initDraw(drawer: Drawer) {
        bst.initDraw(drawer)
    }
    
    override fun doDraw(drawer: Drawer) {
        selectedNode?.also { selected ->
            selected.draw(drawer, 1)
        }
    }
}