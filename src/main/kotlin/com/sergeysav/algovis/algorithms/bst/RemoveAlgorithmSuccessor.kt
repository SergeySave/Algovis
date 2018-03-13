package com.sergeysav.algovis.algorithms.bst

import com.sergeysav.algovis.Drawer
import com.sergeysav.algovis.algorithms.Algorithm
import com.sergeysav.algovis.structures.BSTStructure

/**
 * @author sergeys
 *
 * @constructor Creates a new RemoveAlgorithmPredecessor
 */
class RemoveAlgorithmSuccessor(val bst: BSTStructure, val toRemove: Int): Algorithm() {
    
    var selectedNode1: BSTStructure.Node? = null
    var selectedNode2: BSTStructure.Node? = null
    
    override suspend fun execute() {
        bst.root = bst.root.remove(toRemove)
        bst.delayer.doDelay(bst.delayMillis)
        selectedNode1 = null
        selectedNode2 = null
    }
    
    private suspend fun BSTStructure.Node?.remove(toRemove: Int): BSTStructure.Node? {
        if (!isActive() || this == null) {
            return this
        }
        this@RemoveAlgorithmSuccessor.selectedNode1 = this
        if (toRemove < data) {
            val node = getLeft().remove(toRemove)
            this@RemoveAlgorithmSuccessor.selectedNode1 = node
            setLeft(node)
            return this
        } else if (toRemove > data) {
            val node = getRight().remove(toRemove)
            this@RemoveAlgorithmSuccessor.selectedNode1 = node
            setRight(node)
            return this
        }
        
        return this.performRemove()
    }
    
    private suspend fun BSTStructure.Node.performRemove(): BSTStructure.Node? {
        this@RemoveAlgorithmSuccessor.selectedNode1 = this
        val leftChild = getLeft()
        val rightChild = getRight()
        return if (leftChild == null) {
            return rightChild //If null then we had no children so return null
            //If not null then it should replace this one
        } else {
            if (rightChild != null) {
                var parent: BSTStructure.Node = this
                var predecessor: BSTStructure.Node = rightChild
                this@RemoveAlgorithmSuccessor.selectedNode2 = predecessor
                
                while (predecessor.getLeft() != null) {
                    this@RemoveAlgorithmSuccessor.selectedNode2 = predecessor
                    parent = predecessor
                    predecessor = predecessor.getLeft()!!
                }
                
                this.data = predecessor.data
                if (parent == this) {
                    setRight(predecessor.performRemove())
                } else {
                    parent.setLeft(predecessor.performRemove())
                }
                
                this@RemoveAlgorithmSuccessor.selectedNode2 = null
                
                this
            } else {
                leftChild
            }
        }
    }
    
    override fun initDraw(drawer: Drawer) {
        bst.initDraw(drawer)
    }
    
    override fun doDraw(drawer: Drawer) {
        selectedNode1?.also { selected ->
            selected.draw(drawer, 1)
        }
        selectedNode2?.also { selected ->
            selected.draw(drawer, 1)
        }
    }
}