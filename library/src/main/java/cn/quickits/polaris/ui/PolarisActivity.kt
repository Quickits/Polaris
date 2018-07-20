package cn.quickits.polaris.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.ListPopupWindow
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import cn.quickits.polaris.R
import cn.quickits.polaris.data.FileItem
import cn.quickits.polaris.data.PageStack
import cn.quickits.polaris.data.SelectedItemCollection
import cn.quickits.polaris.data.SelectionSpec
import cn.quickits.polaris.loader.impl.FileLoader
import cn.quickits.polaris.loader.impl.MediaLoader
import cn.quickits.polaris.util.DisplayTextUtils
import kotlinx.android.synthetic.main.polaris_activity_polaris.*


class PolarisActivity : AppCompatActivity() {

    private lateinit var selectionSpec: SelectionSpec

    private lateinit var selectedItemCollection: SelectedItemCollection

    private lateinit var adapter: FileItemsAdapter

    private lateinit var fileLoader: FileLoader

    private lateinit var mediaLoader: MediaLoader

    private lateinit var fileItemsViewModel: FileItemsViewModel

    private lateinit var listPopupWindow: ListPopupWindow

    private val pageStack = PageStack()

    private var isFileSystemMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        initObject()

        setTheme(selectionSpec.themeId)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.polaris_activity_polaris)

        initView()

        showLoading()

        fileItemsViewModel.fileItemsLiveData.observe(this, Observer {
            if (it == null) {
                showError()
            } else {
                showContent(it)
            }
        })

        apply_view.setOnClickListener {
            val result = Intent()
            result.putParcelableArrayListExtra(EXTRA_RESULT_SELECTION, ArrayList(selectedItemCollection.asListOfUri()))
            result.putStringArrayListExtra(EXTRA_RESULT_SELECTION_PATH, ArrayList(selectedItemCollection.asListOfPath()))
            setResult(RESULT_OK, result)
            finish()
        }

        listPopupWindow.setOnItemClickListener { _, _, position, _ ->
            when (position) {
                0 -> {
                    toolbar_title_view.setText(R.string.category_image)
                    fileItemsViewModel.fileItemsLiveData.loader = mediaLoader
                    fileItemsViewModel.fileItemsLiveData.refresh(mediaLoader.currentPath())
                    isFileSystemMode = false
                }
                1 -> {
                    toolbar_title_view.setText(R.string.category_file)
                    fileItemsViewModel.fileItemsLiveData.loader = fileLoader
                    fileItemsViewModel.fileItemsLiveData.refresh(fileLoader.currentPath())
                    isFileSystemMode = true
                }
            }
            listPopupWindow.dismiss()
        }
    }

    private fun initObject() {
        selectionSpec = SelectionSpec.INSTANCE

        selectedItemCollection = SelectedItemCollection(this)

        adapter = FileItemsAdapter(onItemClickListener, selectedItemCollection)

        fileLoader = FileLoader()
        mediaLoader = MediaLoader.newInstance(this)

        fileItemsViewModel = ViewModelProviders.of(this, FileItemsViewModelFactory(mediaLoader)).get(FileItemsViewModel::class.java)
    }

    private fun initView() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.polaris_ic_close_white_24dp)

        toolbar_title_view.setText(R.string.category_image)
        toolbar_title_view.setOnClickListener {
            listPopupWindow.show()
        }

        listPopupWindow = ListPopupWindow(this)
        listPopupWindow.isModal = true
        val density = resources.displayMetrics.density
        listPopupWindow.setContentWidth((160 * density).toInt())
        listPopupWindow.horizontalOffset = (-8 * density).toInt()
        listPopupWindow.verticalOffset = (-48 * density).toInt()
        listPopupWindow.anchorView = toolbar_title_view
        listPopupWindow.setAdapter(ArrayAdapter.createFromResource(this, R.array.polaris_category, R.layout.polaris_item_category))

        content_view.adapter = adapter

        select_count_view.text = DisplayTextUtils.selectProgress(this@PolarisActivity, selectedItemCollection.count())
    }

    private val onItemClickListener: FileItemsAdapter.OnItemClickListener by lazy {
        object : FileItemsAdapter.OnItemClickListener {

            override fun onParentDirClick(path: String) {
                fileItemsViewModel.fileItemsLiveData.refresh(path)
                if (isFileSystemMode) {
                    pageStack.pop()
                }
            }

            override fun onDirClick(path: String) {
                fileItemsViewModel.fileItemsLiveData.refresh(path)
                if (isFileSystemMode) {
                    pageStack.peek()?.pagePosition = content_view.computeVerticalScrollOffset()
                    pageStack.push(path)
                }
            }

            override fun onFileClick(path: String) {
                select_count_view.text = DisplayTextUtils.selectProgress(this@PolarisActivity,
                        selectedItemCollection.count())
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (!isFileSystemMode) {
            finish()
            return
        }

        pageStack.pop()
        val page = pageStack.peek()
        if (page != null) {
            fileItemsViewModel.fileItemsLiveData.refresh(page.page)
        } else {
            finish()
        }
    }

    private fun showLoading() {
        loading_view.visibility = View.VISIBLE
        content_view.visibility = View.GONE
        error_view.visibility = View.GONE
    }

    private fun showContent(list: List<FileItem>) {
        loading_view.visibility = View.GONE
        content_view.visibility = View.VISIBLE
        error_view.visibility = View.GONE

        adapter.list = list
        adapter.notifyDataSetChanged()

        val page = pageStack.peek()
        content_view.scrollBy(0, page?.pagePosition ?: 0)
    }

    private fun showError() {
        loading_view.visibility = View.GONE
        content_view.visibility = View.GONE
        error_view.visibility = View.VISIBLE
    }

    companion object {
        const val EXTRA_RESULT_SELECTION = "EXTRA_RESULT_SELECTION"
        const val EXTRA_RESULT_SELECTION_PATH = "EXTRA_RESULT_SELECTION_PATH"
    }
}
