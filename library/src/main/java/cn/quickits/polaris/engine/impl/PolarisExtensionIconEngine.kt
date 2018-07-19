package cn.quickits.polaris.engine.impl

import cn.quickits.polaris.engine.ExtensionIconEngine

class PolarisExtensionIconEngine : ExtensionIconEngine {

    override fun getFolderIcon(): String =
            "file:///android_asset/file_extension_icons/file_extension_folder.png"

    override fun getFileExtendsionIcon(extension: String): String =
            "file:///android_asset/file_extension_icons/file_extension_${extension.toLowerCase()}.png"
}