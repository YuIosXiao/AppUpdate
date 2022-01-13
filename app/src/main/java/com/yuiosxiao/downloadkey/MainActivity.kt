package com.yuiosxiao.downloadkey

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yuiosxiao.downloadvalue.AppUpdateQuick
import com.yuiosxiao.downloadvalue.DownloadListener
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File

class MainActivity : AppCompatActivity() {

    val URL = "https://*******.apk"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_down.setOnClickListener {
            AppUpdateQuick(this)
                .url(URL)
                .apkName("***.apk")
                .start(object : DownloadListener{
                    override fun onProgress(progress: Int) {
                        progress_bar.progress = progress
                    }

                    override fun onDownloading(boolean: Boolean) {

                    }

                    override fun onFinish(file: File) {
                        progress_bar.progress = 100
                    }

                    override fun onError(throwable: Throwable) {

                    }

                    override fun onCancel() {

                    }

                })
        }

    }
}