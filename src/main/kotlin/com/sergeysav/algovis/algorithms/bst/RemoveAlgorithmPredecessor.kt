package com.sergeysav.algovis.algorithms.bst

import com.sergeysav.algovis.structures.BSTStructure

/**
 * @author sergeys
 *
 * @constructor Creates a new RemoveAlgorithmPredecessor
 */
class RemoveAlgorithmPredecessor(bst: BSTStructure, val toRemove: Int): BSTAlgorithm(bst) {
    
    var selectedNode1: BSTStructure.Node? = null
    var selectedNode2: BSTStructure.Node? = null
    
    override fun getType(node: BSTStructure.Node): Int {
        if (node == selectedNode1 || node == selectedNode2) {
            return 1
        }
        return 0
    }
    
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
        setVisited(this)
        this@RemoveAlgorithmPredecessor.selectedNode1 = this
        if (toRemove < data) {
            val node = getLeft().remove(toRemove)
            setVisited(node)
            this@RemoveAlgorithmPredecessor.selectedNode1 = node
            setLeft(node)
            return this
        } else if (toRemove > data) {
            val node = getRight().remove(toRemove)
            setVisited(node)
            this@RemoveAlgorithmPredecessor.selectedNode1 = node
            setRight(node)
            return this
        }
        
        return this.performRemove()
    }
    
    private suspend fun BSTStructure.Node.performRemove(): BSTStructure.Node? {
        setVisited(this)
        this@RemoveAlgorithmPredecessor.selectedNode1 = this
        val leftChild = getLeft()
        val rightChild = getRight()
        return if (rightChild == null) {
            return leftChild //If null then we had no children so return null
            //If not null then it should replace this one
        } else {
            if (leftChild != null) {
                var parent: BSTStructure.Node = this
                var predecessor: BSTStructure.Node = leftChild
                setVisited(predecessor)
                this@RemoveAlgorithmPredecessor.selectedNode2 = predecessor
                
                while (predecessor.getRight() != null) {
                    setVisited(predecessor)
                    this@RemoveAlgorithmPredecessor.selectedNode2 = predecessor
                    parent = predecessor
                    predecessor = predecessor.getRight()!!
                }
                
                this.data = predecessor.data
                if (parent == this) {
                    setLeft(predecessor.performRemove())
                } else {
                    parent.setRight(predecessor.performRemove())
                }
                
                this@RemoveAlgorithmPredecessor.selectedNode2 = null
                
                this
            } else {
                rightChild
            }
        }
    }
}