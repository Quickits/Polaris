package cn.quickits.polaris.selection

import android.content.Intent
import cn.quickits.polaris.Polaris
import cn.quickits.polaris.data.SelectionSpec
import cn.quickits.polaris.engine.ImageEngine
import cn.quickits.polaris.ui.PolarisActivity

class SelectionCreator constructor(private val polaris: Polaris) {

    private val selectionSpec = SelectionSpec.cleanInstance

    fun imageEngine(imageEngine: ImageEngine): SelectionCreator {
        selectionSpec.imageEngine = imageEngine
        return this
    }

    fun maxSelectable(maxCount: Int): SelectionCreator {
        selectionSpec.maxSelectable = maxCount
        return this
    }

    fun selectedBackgroundRes(res: Int): SelectionCreator {
        selectionSpec.selectedBackgroundRes = res
        return this
    }

    fun forResult(requestCode: Int) {
        val activity = polaris.getActivity()

        val intent = Intent(activity, PolarisActivity::class.java)

        val fragment = polaris.getFragment()
        if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode)
        } else {
            activity?.startActivityForResult(intent, requestCode)
        }
    }

}