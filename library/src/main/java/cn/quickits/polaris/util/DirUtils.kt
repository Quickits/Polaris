package cn.quickits.polaris.util

import android.os.Environment
import java.io.File

object DirUtils {

    fun rootDir(): File = Environment.getExternalStorageDirectory()

    fun rootDirPath(): String = rootDir().absolutePath

}