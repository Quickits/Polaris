package cn.quickits.polaris.loader.impl

import cn.quickits.polaris.data.FileItem
import cn.quickits.polaris.loader.FileItemsLoader
import cn.quickits.polaris.util.DirUtils
import cn.quickits.polaris.util.FileUtils
import java.io.File

class FileLoader : FileItemsLoader {

    private var path: String = DirUtils.rootDirPath()

    override fun load(path: String): List<FileItem>? {
        var listFiles: List<File> = FileUtils.listFilesInDir(path) ?: return null

        this.path = path

        listFiles = listFiles.sortedWith(Comparator { file1, file2 ->
            if (file1.isDirectory && file2.isFile)
                return@Comparator -1
            if (file1.isFile && file2.isDirectory)
                return@Comparator 1
            return@Comparator file1.name.compareTo(file2.name)
        })

        val result = arrayListOf<FileItem>()

        if (path != DirUtils.rootDirPath()) {
            result.add(FileItem.createRoot(File(path).parentFile))
        }

        for (file in listFiles) {
            val fileItem = FileItem.createFileItem(file)
            result.add(fileItem)
        }

        return result
    }

    override fun currentPath(): String = path

}