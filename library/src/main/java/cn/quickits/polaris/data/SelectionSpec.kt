package cn.quickits.polaris.data

import android.content.pm.ActivityInfo
import cn.quickits.polaris.R
import cn.quickits.polaris.engine.ExtensionIconEngine
import cn.quickits.polaris.engine.ImageEngine
import cn.quickits.polaris.engine.impl.GlideEngine
import cn.quickits.polaris.engine.impl.PolarisExtensionIconEngine

class SelectionSpec {

    var orientation = 0
    var isShowProgressRate = false
    var maxSelectable = 0
    var gridExpectedSize = 0
    var themeId = R.style.Polaris
    var imageEngine: ImageEngine = GlideEngine()
    var extensionIconEngine: ExtensionIconEngine = PolarisExtensionIconEngine()

    private fun reset() {
        orientation = 0
        isShowProgressRate = false
        maxSelectable = 0
        gridExpectedSize = 0
        imageEngine = GlideEngine()
    }

    fun needOrientationRestriction(): Boolean {
        return orientation != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
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