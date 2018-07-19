package cn.quickits.polaris.loader

import cn.quickits.polaris.data.FileItem

interface FileItemsLoader {

    fun load(path: String): List<FileItem>?

    fun currentPath(): String

}