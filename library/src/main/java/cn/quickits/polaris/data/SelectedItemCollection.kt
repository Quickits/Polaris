package cn.quickits.polaris.data

import android.content.Context
import android.net.Uri
import cn.quickits.polaris.util.PathUtils

class SelectedItemCollection(private val context: Context) {

    private var selectedItems: LinkedHashSet<FileItem> = linkedSetOf()

    fun add(item: FileItem) {
        selectedItems.add(item)
    }

    fun remove(item: FileItem): Boolean = selectedItems.remove(item)

    fun isSelected(item: FileItem): Boolean = selectedItems.contains(item)

    fun isNotEmpty(): Boolean = selectedItems.isNotEmpty()

    fun count(): Int = selectedItems.size

    fun asListOfUri(): List<Uri> {
        val uris = mutableListOf<Uri>()
        for (item in selectedItems) {
            uris.add(item.uri)
        }
        return uris
    }

    fun asListOfPath(): List<String> {
        val paths = mutableListOf<String>()

        for (item in selectedItems) {
            val path = PathUtils.getPath(context, item.uri) ?: continue
            paths.add(path)
        }
        return paths
    }
}