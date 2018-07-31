package cn.quickits.polaris.sample

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import cn.quickits.polaris.Polaris
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hello.setOnClickListener {
            RxPermissions(this)
                    .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe {
                        Polaris.from(this)
//                                .iconPacksEngine(object : IconPacksEngine {
//                                    override fun getFolderIcon(): String {
//                                        return "https://wx1.sinaimg.cn/mw690/51ff3e45gy1ftt4koe4ifj20zk0qodlj.jpg"
//                                    }
//
//                                    override fun getFileExtensionIcon(p0: String?): String {
//                                        return "https://wx1.sinaimg.cn/mw690/51ff3e45gy1ftt4koe4ifj20zk0qodlj.jpg"
//                                    }
//
//                                })
                                .forResult(101)
                    }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            val list = Polaris.obtainResultPath(data) ?: return

            for (path in list) {
                println(path)
            }

        }
    }
}
