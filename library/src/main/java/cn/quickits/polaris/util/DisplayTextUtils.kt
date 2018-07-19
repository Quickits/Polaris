package cn.quickits.polaris.util

import android.content.Context
import cn.quickits.polaris.R

object DisplayTextUtils {

    fun selectProgressWithMax(context: Context, selectCount: Int, max: Int): String =
            context.getString(R.string.select_progress_with_max, selectCount, max)

    fun selectProgress(context: Context, selectCount: Int): String =
            context.getString(R.string.select_progress, selectCount)

    fun parentFolder(context: Context): String =
            context.getString(R.string.parent_folder)

    fun fileItemDesc(context: Context, time: String): String =
            context.getString(R.string.file_item_desc1, time)

    fun fileItemDesc(context: Context, time: String, size: String): String =
            context.getString(R.string.file_item_desc2, time, size)
}