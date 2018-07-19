package cn.quickits.polaris.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import cn.quickits.polaris.R
import cn.quickits.polaris.data.FileItem
import cn.quickits.polaris.data.PageStack
import cn.quickits.polaris.data.SelectedItemCollection
import cn.quickits.polaris.util.DisplayTextUtils
import kotlinx.android.synthetic.main.activity_polaris.*


class PolarisActivity : AppCompatActivity() {

    private lateinit var adapter: FileItemsAdapter

    private lateinit var fileItemsViewModel: FileItemsViewModel

    private val selectedItemCollection = SelectedItemCollection()

    private val pageStack = PageStack()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectedItemCollection.onCreate(savedInstanceState)

        setContentView(R.layout.activity_polaris)

        adapter = FileItemsAdapter(onItemClickListener, selectedItemCollection)
        content_view.adapter = adapter

        showLoading()

        fileItemsViewModel = ViewModelProviders.of(this).get(FileItemsViewModel::class.java)
        fileItemsViewModel.fileItemsLiveData.observe(this, Observer {
            loading_view.visibility = View.GONE
            if (it == null) {
                showError()
            } else {
                showContent(it)
            }
        })

        apply_view.setOnClickListener {
            val result = Intent()
            result.putStringArrayListExtra(EXTRA_RESULT_SELECTION_PATH, ArrayList(selectedItemCollection.asListOfString()))
            setResult(RESULT_OK, result)
            finish()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp)

        select_count_view.text = DisplayTextUtils.selectProgress(this@PolarisActivity,
                selectedItemCollection.count())
    }

    private val onItemClickListener: FileItemsAdapter.OnItemClickListener by lazy {
        object : FileItemsAdapter.OnItemClickListener {

            override fun onParentDirClick(path: String) {
                fileItemsViewModel.fileItemsLiveData.refresh(path)
                pageStack.pop()
            }

            override fun onDirClick(path: String) {
                fileItemsViewModel.fileItemsLiveData.refresh(path)
                pageStack.peek()?.pagePosition = content_view.computeVerticalScrollOffset()
                pageStack.push(path)
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
        const val EXTRA_RESULT_SELECTION_PATH = "EXTRA_RESULT_SELECTION_PATH"
    }
}
