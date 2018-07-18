package cn.quickits.polaris.ui

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import cn.quickits.polaris.data.FileItem
import cn.quickits.polaris.util.DirUtils
import cn.quickits.polaris.util.FileUtils
import java.io.File
import java.util.*

@SuppressLint("StaticFieldLeak")
class FileItemsLiveData : LiveData<List<FileItem>>() {

    private var task: AsyncTask<String, Void, List<FileItem>>? = null

    private var currentDir: String? = null

    override fun onActive() {
        super.onActive()
        val dir = currentDir ?: DirUtils.rootDirPath()

        if (dir != currentDir) refresh(dir)
    }

    override fun onInactive() {
        super.onInactive()
        cancelTask()
    }

    fun refresh(dir: String) {
        if (hasObservers()) {
            currentDir = dir
            createTask()
            task?.execute(dir)
        }
    }

    private fun cancelTask() {
        if (task?.status == AsyncTask.Status.RUNNING) {
            task?.cancel(true)
        }
        task = null
    }

    private fun createTask() {
        cancelTask()
        task = object : AsyncTask<String, Void, List<FileItem>>() {
            override fun doInBackground(vararg args: String?): List<FileItem>? {
                if (args.size != 1) return null

                val dir: String = args[0] ?: return null

                var listFiles: List<File> = FileUtils.listFilesInDir(dir) ?: return null

                listFiles = listFiles.sortedWith(Comparator { file1, file2 ->
                    if (file1.isDirectory && file2.isFile)
                        return@Comparator -1
                    if (file1.isFile && file2.isDirectory)
                        return@Comparator 1
                    return@Comparator file1.name.compareTo(file2.name)
                })

                val result = arrayListOf<FileItem>()

                if (dir != DirUtils.rootDirPath()) {
                    result.add(FileItem.createRoot(File(dir).parentFile))
                }

                for (file in listFiles) {
                    val fileItem = FileItem.createFileItem(file)
                    result.add(fileItem)
                }

                return result
            }

            override fun onPostExecute(result: List<FileItem>?) {
                value = result
            }
        }
    }

}