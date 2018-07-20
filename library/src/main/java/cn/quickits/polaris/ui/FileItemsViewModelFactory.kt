package cn.quickits.polaris.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import cn.quickits.polaris.loader.FileItemsLoader

@Suppress("UNCHECKED_CAST")
class FileItemsViewModelFactory(private val loader: FileItemsLoader) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FileItemsViewModel::class.java)) {
            return FileItemsViewModel(loader) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}