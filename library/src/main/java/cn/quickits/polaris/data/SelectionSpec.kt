package cn.quickits.polaris.data

import cn.quickits.polaris.R
import cn.quickits.polaris.engine.ExtensionIconEngine
import cn.quickits.polaris.engine.ImageEngine
import cn.quickits.polaris.engine.impl.GlideEngine
import cn.quickits.polaris.engine.impl.PolarisExtensionIconEngine

class SelectionSpec {
    var maxSelectable = 0
    var themeId: Int = R.style.Polaris
    var selectedBackgroundRes: Int = R.drawable.bg_item_selected
    var imageEngine: ImageEngine = GlideEngine()
    var extensionIconEngine: ExtensionIconEngine = PolarisExtensionIconEngine()

    private fun reset() {
        maxSelectable = 0
        imageEngine = GlideEngine()
    }

    private object InstanceHolder {
        internal val INSTANCE = SelectionSpec()
    }

    companion object {

        val INSTANCE: SelectionSpec
            get() = InstanceHolder.INSTANCE

        val cleanInstance: SelectionSpec
            get() {
                val selectionSpec = INSTANCE
                selectionSpec.reset()
                return selectionSpec
            }

    }
}