package cn.quickits.polaris.engine

interface ExtensionIconEngine {

    fun getFolderIcon(): String

    fun getFileExtendsionIcon(extension: String): String

}