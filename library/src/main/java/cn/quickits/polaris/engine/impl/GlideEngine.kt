package cn.quickits.polaris.engine.impl

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import cn.quickits.polaris.engine.ImageEngine
import com.bumptech.glide.Glide

class GlideEngine : ImageEngine {

    override fun loadFileIcon(context: Context, imageView: ImageView, uri: Uri, placeholder: Int) {
        Glide.with(context)
                .load(uri)
                .error(placeholder)
                .fitCenter()
                .into(imageView)
    }

    override fun loadImage(context: Context, imageView: ImageView, uri: Uri) {
        Glide.with(context)
                .load(uri)
                .centerCrop()
                .into(imageView)
    }

}