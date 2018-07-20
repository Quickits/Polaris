package cn.quickits.polaris

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v4.app.Fragment
import cn.quickits.polaris.selection.SelectionCreator
import cn.quickits.polaris.ui.PolarisActivity
import java.lang.ref.WeakReference

class Polaris {

    private var mContext: WeakReference<Activity>? = null

    private var mFragment: WeakReference<Fragment>? = null

    private constructor(fragment: Fragment) : this(fragment.activity, fragment)

    private constructor(activity: Activity) : this(activity, null)

    private constructor(activity: Activity?, fragment: Fragment?) {
        mContext = if (activity != null) WeakReference(activity) else null
        mFragment = if (fragment != null) WeakReference(fragment) else null
    }

    companion object {

        @JvmStatic
        fun from(activity: Activity) = SelectionCreator(Polaris(activity))


        @JvmStatic
        fun from(fragment: Fragment) = SelectionCreator(Polaris(fragment))

        @JvmStatic
        fun obtainResult(data: Intent?): ArrayList<Uri>? = data?.getParcelableArrayListExtra(PolarisActivity.EXTRA_RESULT_SELECTION)

        @JvmStatic
        fun obtainResultPath(data: Intent?): ArrayList<String>? = data?.getStringArrayListExtra(PolarisActivity.EXTRA_RESULT_SELECTION_PATH)

    }

    fun getActivity() = mContext?.get()

    fun getFragment() = mFragment?.get()

}