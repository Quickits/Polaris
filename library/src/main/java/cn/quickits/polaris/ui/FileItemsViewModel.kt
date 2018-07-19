package cn.quickits.polaris.ui

import android.arch.lifecycle.ViewModel
import cn.quickits.polaris.loader.FileItemsLoader

class FileItemsViewModel(loader: FileItemsLoader) : ViewModel() {

    val fileItemsLiveData: FileItemsLiveData = FileItemsLiveData(loader)

}