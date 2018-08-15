package cn.quickits.polaris.data

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import cn.quickits.polaris.util.FileUtils
import java.io.File

data class FileItem(
        val uri: Uri,
        var name: String,
        val icon: Uri,
        var size: String,
        val lastModifyTime: Long,
        val mimeType: String?,   // file only
        val extension: String?,  // file only
        val absolutePath: String,
        val isDir: Boolean,
        var isParent: Boolean
) {

    override fun hashCode(): Int {
        return uri.path.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is FileItem) {
            return other.uri.path == uri.path
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

            val icon: Uri = if (file.isDirectory) {
                SelectionSpec.INSTANCE.extensionIconEngine?.folderIcon ?: Uri.EMPTY
            } else {
                if (mimeType.startsWith("image")) {
                    Uri.parse("file://${file.absolutePath}")
                } else {
                    SelectionSpec.INSTANCE.extensionIconEngine?.getFileExtensionIcon(extension)
                            ?: Uri.EMPTY
                }
            }

            return FileItem(
                    Uri.fromFile(file),
                    file.name,
                    icon,
                    FileUtils.getFileSize(file),
                    file.lastModified(),
                    mimeType,
                    extension,
                    file.absolutePath,
                    file.isDirectory,
                    false
            )
        }

        fun valueOf(cursor: Cursor): FileItem {
            val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Files.FileColumns._ID))
            val name = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.TITLE))
            val size = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.SIZE))
            val mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE))
            val date = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.DATE_ADDED)) * 1000
            val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

            return FileItem(
                    uri,
                    name,
                    uri,
                    FileUtils.byte2FitMemorySize(size),
                    date,
                    mimeType,
                    "",
                    "",
                    false,
                    false
            )
        }
    }

}