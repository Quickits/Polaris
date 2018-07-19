package cn.quickits.polaris.engine.impl

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import cn.quickits.polaris.engine.ImageEngine
import com.bumptech.glide.Glide

class GlideEngine : ImageEngine {

    override fun loadImage(context: Context, imageView: ImageView, uri: Uri, placeholder: Int) {
        Glide.with(context)
                .load(uri)
                .error(placeholder)
                .centerCrop()
                .into(imageView)
    }

}