package cn.quickits.polaris.engine

import android.content.Context
import android.net.Uri
import android.widget.ImageView

interface ImageEngine {

    fun loadImage(context: Context, imageView: ImageView, uri: Uri)

    fun loadFileIcon(context: Context, imageView: ImageView, uri: Uri, placeholder: Int)

}