package cn.quickits.polaris.ui

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
import cn.quickits.polaris.util.DisplayTextUtils

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
            RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.polaris_item_fileitem, parent, false)) {

        private val iconView: ImageView = itemView.findViewById(R.id.icon_view)

        private val fileNameView: TextView = itemView.findViewById(R.id.file_name_view)

        private val fileDescView: TextView = itemView.findViewById(R.id.file_desc_view)

        private val checkBoxView: CheckBox = itemView.findViewById(R.id.check_box_view)

        private val dividerView: View = itemView.findViewById(R.id.divider_view)

        fun onBind(fileItem: FileItem, isLast: Boolean) {
            fileNameView.text = fileItem.name
            fileDescView.text = if (fileItem.isDir) {
                if (fileItem.isParent) {
                    DisplayTextUtils.parentFolder(itemView.context)
                } else {
                    DisplayTextUtils.fileItemDesc(itemView.context, DateUtils.formatDateTime(itemView.context, fileItem.lastModifyTime, DateUtils.FORMAT_SHOW_DATE))
                }
            } else {
                DisplayTextUtils.fileItemDesc(itemView.context, DateUtils.formatDateTime(itemView.context, fileItem.lastModifyTime, DateUtils.FORMAT_SHOW_DATE), fileItem.size)
            }

            if (fileItem.isDir) {
                checkBoxView.visibility = View.GONE
                checkBoxView.isChecked = false
            } else {
                checkBoxView.visibility = View.VISIBLE
                checkBoxView.isChecked = selectedItemCollection.isSelected(fileItem)
            }

            dividerView.visibility = if (isLast) View.GONE else View.VISIBLE

            updateCheckedStyle()

            if (fileItem.mimeType?.startsWith("image") == true) {
                SelectionSpec.INSTANCE.imageEngine.loadImage(
                        itemView.context,
                        iconView,
                        fileItem.icon
                )
            } else {
                SelectionSpec.INSTANCE.imageEngine.loadFileIcon(
                        itemView.context,
                        iconView,
                        fileItem.icon,
                        R.drawable.polaris_file_extension_others
                )
            }

            itemView.setOnClickListener {
                if (fileItem.isDir) {
                    if (fileItem.isParent) {
                        onItemClickListener.onParentDirClick(fileItem.absolutePath)
                    } else {
                        onItemClickListener.onDirClick(fileItem.absolutePath)
                    }
                } else {
                    if (selectedItemCollection.isSelected(fileItem)) {
                        checkBoxView.isChecked = false
                        selectedItemCollection.remove(fileItem)
                    } else {
                        checkBoxView.isChecked = true
                        selectedItemCollection.add(fileItem)
                    }

                    updateCheckedStyle()
                    onItemClickListener.onFileClick(fileItem.absolutePath)
                }
            }

        }

        private fun updateCheckedStyle() {
            itemView.isSelected = checkBoxView.isChecked
        }
    }

}

