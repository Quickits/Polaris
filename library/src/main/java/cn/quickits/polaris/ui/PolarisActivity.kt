package cn.quickits.polaris.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import cn.quickits.polaris.R
import cn.quickits.polaris.data.FileItem
import cn.quickits.polaris.data.SelectedItemCollection
import kotlinx.android.synthetic.main.activity_polaris.*


class PolarisActivity : AppCompatActivity() {

    private lateinit var adapter: FileItemsAdapter

    private lateinit var fileItemsViewModel: FileItemsViewModel

    private val selectedItemCollection = SelectedItemCollection()

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
    }

    private val onItemClickListener: FileItemsAdapter.OnItemClickListener by lazy {
        object : FileItemsAdapter.OnItemClickListener {
            override fun onDirClick(path: String) {
                showLoading()
                fileItemsViewModel.fileItemsLiveData.refresh(path)
            }

            override fun onFileClick(path: String) {
                select_count_view.text = "${selectedItemCollection.count()}"
            }
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
