package cn.quickits.polaris.ui

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import cn.quickits.polaris.R
import cn.quickits.polaris.data.FileItem
import cn.quickits.polaris.data.SelectedItemCollection
import cn.quickits.polaris.data.SelectionSpec

class FileItemsAdapter(private val onItemClickListener: OnItemClickListener,
                       private val selectedItemCollection: SelectedItemCollection) : RecyclerView.Adapter<FileItemsAdapter.FileItemsViewHolder>() {

    var list: List<FileItem>? = null

    override fun getItemCount(): Int = list?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): FileItemsViewHolder = FileItemsViewHolder(parent)

    override fun onBindViewHolder(holder: FileItemsViewHolder, position: Int) {
        val item = list?.get(position) ?: return
        holder.onBind(item, position == itemCount - 1)
    }

    interface OnItemClickListener {

        fun onParentDirClick(path: String)

        fun onDirClick(path: String)

        fun onFileClick(path: String)
    }

    inner class FileItemsViewHolder(parent: ViewGroup) :
            RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_fileitem, parent, false)) {

        private val iconView: ImageView = itemView.findViewById(R.id.icon_view)

        private val fileNameView: TextView = itemView.findViewById(R.id.file_name_view)

        private val fileDescView: TextView = itemView.findViewById(R.id.file_desc_view)

        private val checkBoxView: CheckBox = itemView.findViewById(R.id.check_box_view)

        private val dividerView: View = itemView.findViewById(R.id.divider_view)

        @SuppressLint("SetTextI18n")
        fun onBind(fileItem: FileItem, isLast: Boolean) {
            fileNameView.text = fileItem.name
            fileDescView.text = if (fileItem.isDir) {
                if (fileItem.isParent) {
                    "上层文件夹"
                } else {
                    DateUtils.formatDateTime(itemView.context, fileItem.lastModifyTime, DateUtils.FORMAT_SHOW_DATE)
                }
            } else {
                "${DateUtils.formatDateTime(itemView.context, fileItem.lastModifyTime, DateUtils.FORMAT_SHOW_DATE)} • ${fileItem.size}"
            }

            if (fileItem.isDir) {
                checkBoxView.visibility = View.GONE
            } else {
                checkBoxView.visibility = View.VISIBLE
                checkBoxView.isChecked = selectedItemCollection.isSelected(fileItem)
            }

            dividerView.visibility = if (isLast) View.GONE else View.VISIBLE

            SelectionSpec.INSTANCE.imageEngine.loadImage(
                    itemView.context,
                    iconView,
                    fileItem.icon,
                    R.drawable.file_extension_others
            )

            itemView.setOnClickListener {
                if (fileItem.isDir) {
                    if (fileItem.isParent) {
                        onItemClickListener.onParentDirClick(fileItem.file.absolutePath)
                    } else {
                        onItemClickListener.onDirClick(fileItem.file.absolutePath)
                    }
                } else {
                    if (selectedItemCollection.isSelected(fileItem)) {
                        checkBoxView.isChecked = false
                        selectedItemCollection.remove(fileItem)
                    } else {
                        checkBoxView.isChecked = true
                        selectedItemCollection.add(fileItem)
                    }

                    onItemClickListener.onFileClick(fileItem.file.absolutePath)
                }
            }

        }
    }

}

