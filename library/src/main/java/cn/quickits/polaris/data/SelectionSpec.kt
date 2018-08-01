package cn.quickits.polaris.data

import cn.quickits.polaris.R
import cn.quickits.polaris.engine.ImageEngine
import cn.quickits.polaris.engine.impl.GlideEngine
import cn.quickits.polaris.iconpacks.DefaultIconPacksEngine
import cn.quickits.polaris.iconpacks.core.IconPacksEngine

class SelectionSpec {
    var maxSelectable = 0
    var themeId: Int = R.style.Polaris
    var imageEngine: ImageEngine = GlideEngine()
    var extensionIconEngine: IconPacksEngine? = null
        get() {
            if (field == null) extensionIconEngine = DefaultIconPacksEngine()
            return field
        }

    private fun reset() {
        maxSelectable = 0
        themeId = R.style.Polaris
        extensionIconEngine = null
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