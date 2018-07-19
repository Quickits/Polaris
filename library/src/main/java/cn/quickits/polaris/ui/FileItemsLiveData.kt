package cn.quickits.polaris.ui

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.os.AsyncTask
import cn.quickits.polaris.data.FileItem
import cn.quickits.polaris.loader.FileItemsLoader

@SuppressLint("StaticFieldLeak")
class FileItemsLiveData(var loader: FileItemsLoader) : LiveData<List<FileItem>>() {

    private var task: AsyncTask<String, Void, List<FileItem>>? = null

    override fun onActive() {
        super.onActive()
        refresh(loader.currentPath())
    }

    override fun onInactive() {
        super.onInactive()
        cancelTask()
    }

    fun refresh(dir: String) {
        if (hasObservers()) {
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

                return loader.load(dir)
            }

            override fun onPostExecute(result: List<FileItem>?) {
                value = result
            }
        }
    }

}