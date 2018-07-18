package cn.quickits.polaris.engine

import android.content.Context
import android.widget.ImageView

interface ImageEngine {

    fun loadImage(context: Context, imageView: ImageView, uri: String, placeholder: Int)

}