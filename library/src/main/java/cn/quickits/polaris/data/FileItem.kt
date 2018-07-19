package cn.quickits.polaris.data

import android.webkit.MimeTypeMap
import cn.quickits.polaris.util.FileUtils
import java.io.File

data class FileItem(
        val file: File,
        var name: String,
        val icon: String,
        var size: String,
        val lastModifyTime: Long,
        val mimeType: String,   // file only
        val extension: String,  // file only
        val isDir: Boolean,
        var isParent: Boolean
) {

    override fun hashCode(): Int {
        return file.absolutePath.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is FileItem) {
            return other.file.absolutePath == file.absolutePath
        }
        return false
    }

    companion object {
        fun createRoot(file: File): FileItem {
            val fileItem = createFileItem(file)
            fileItem.name = ".."
            fileItem.isParent = true
            return fileItem
        }

        fun createFileItem(file: File): FileItem {
            val extension = FileUtils.getFileExtension(file) ?: ""
            val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) ?: ""

            val icon = if (file.isDirectory) {
                "file:///android_asset/file_extension_icons/file_extension_folder.png"
            } else {
                if (mimeType.startsWith("image")) {
                    "file://${file.absolutePath}"
                } else {
                    "file:///android_asset/file_extension_icons/file_extension_${extension.toLowerCase()}.png"
                }
            }

            return FileItem(
                    file,
                    file.name,
                    icon,
                    FileUtils.getFileSize(file),
                    file.lastModified(),
                    mimeType,
                    extension,
                    file.isDirectory,
                    false
            )
        }

    }

}