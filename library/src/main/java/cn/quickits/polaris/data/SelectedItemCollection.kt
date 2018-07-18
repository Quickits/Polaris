package cn.quickits.polaris.data

import android.os.Bundle

class SelectedItemCollection {

    private lateinit var selectedItems: LinkedHashSet<FileItem>

    companion object {
        const val STATE_SELECTION = "STATE_SELECTION"
    }

    fun onCreate(bundle: Bundle?) {
        selectedItems = linkedSetOf()
    }

    fun add(item: FileItem) {
        selectedItems.add(item)
    }

    fun remove(item: FileItem): Boolean = selectedItems.remove(item)

    fun isSelected(item: FileItem): Boolean = selectedItems.contains(item)

    fun count(): Int = selectedItems.size

    fun asListOfString(): List<String> {
        val paths = mutableListOf<String>()

        for (item in selectedItems) {
            paths.add(item.file.absolutePath)
        }

        return paths
    }
}