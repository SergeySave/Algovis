package com.sergeysav.algovis.structures

import com.sergeysav.algovis.algorithms.Algorithm

data class AlgorithmReference(val name: String, val params: List<Param> = emptyList(),
                              val creator: (IntArray) -> Algorithm)