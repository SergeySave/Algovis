package com.sergeysav.algovis.structures

data class Param(val name: String, val default: Int = 0, val min: Int = Int.MIN_VALUE, val max: Int = Int.MAX_VALUE,
                 val step: Int = 1)