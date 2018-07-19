package cn.quickits.polaris.loader.impl

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import cn.quickits.polaris.data.FileItem
import cn.quickits.polaris.loader.FileItemsLoader

class MediaLoader private constructor(
        private val context: Context,
        private val uri: Uri,
        private val projection: Array<String>,
        private val selection: String,
        private val selectionArgs: Array<String>,
        private val sortOrder: String
) : FileItemsLoader {

    companion object {

        private val IMAGE_QUERY_URI = MediaStore.Files.getContentUri("external")
        private const val IMAGES_ORDER_BY = "${MediaStore.Images.Media.DATE_TAKEN} DESC"
        private const val IMAGES_SELECTION = "${MediaStore.Files.FileColumns.MEDIA_TYPE} = ? AND ${MediaStore.MediaColumns.SIZE} > 0"
        private val IMAGE_SELECTION_ARGS = arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString())
        private val IMAGES_PROJECTION = arrayOf(
                MediaStore.Files.FileColumns._ID,
                MediaStore.MediaColumns.TITLE,
                MediaStore.MediaColumns.MIME_TYPE,
                MediaStore.MediaColumns.SIZE,
                MediaStore.MediaColumns.DATE_ADDED
        )

        @JvmStatic
        fun newInstance(context: Context): MediaLoader =
                MediaLoader(context, IMAGE_QUERY_URI, IMAGES_PROJECTION, IMAGES_SELECTION, IMAGE_SELECTION_ARGS, IMAGES_ORDER_BY)

    }

    override fun load(path: String): List<FileItem>? {
        val cursor: Cursor = context.contentResolver.query(
                uri,
                projection,
                selection,
                selectionArgs,
                sortOrder
        )

        val result = arrayListOf<FileItem>()

        while (cursor.moveToNext()) {
            val fileItem = FileItem.valueOf(cursor)
            result.add(fileItem)
        }

        cursor.close()

        return result
    }

    override fun currentPath(): String = ""
}