package com.sergeysav.algovis

import com.sergeysav.algovis.algorithms.Algorithm
import com.sergeysav.algovis.structures.NullStructure
import com.sergeysav.algovis.structures.Structure
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLDivElement
import kotlin.browser.document

/**
 * @author sergeys
 */
class Model {
    
    var structure: Structure = NullStructure
    var algorithm: Algorithm? = null
    var completionCallback: (Boolean?) -> Unit = {}
    
    var job: Algorithm? = null
    var lastActive: Boolean? = null
    
    var mainArea: HTMLDivElement = document.elementWithId("mainArea")
    var controls: HTMLDivElement = document.elementWithId("controls")
    var dataSizeMenu: HTMLButtonElement = document.elementWithId("dataSizeMenu")
    var opTimeMenu: HTMLButtonElement = document.elementWithId("opTimeMenu")
    var structureMenu: HTMLDivElement = document.elementWithId("structureMenu")
    var structureMenuItem: HTMLDivElement = document.elementWithId("structureMenuItem")
    var structureMenuSubItem: HTMLDivElement = document.elementWithId("structureMenuSubItem")
    var structureMenuSubListItem: HTMLDivElement = document.elementWithId("structureMenuSubListItem")
    var structureMenuSubSubItem: HTMLDivElement = document.elementWithId("structureMenuSubSubItem")
    var simulation: HTMLAnchorElement = document.elementWithId("simulation")
    var structureList: HTMLDivElement = document.elementWithId("structureList")
    var structureListItem: HTMLDivElement = document.elementWithId("structureListItem")
}