package com.yuiosxiao.downloadvalue

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import java.lang.NullPointerException

class AppUpdateQuick(private val context: Context) {

    private lateinit var url: String
    private lateinit var apkName: String

    /**
     * 设置请求url
     */
    fun url(url: String): AppUpdateQuick {
        this.url = url
        return this
    }

    /**
     * 下载文件名
     */
    fun apkName(name: String): AppUpdateQuick {
        this.apkName = name
        return this
    }

    /**
     * 开始更新
     */
    fun start(downloadListener: DownloadListener) {
        if (url.isNullOrEmpty()) {
            throw NullPointerException("url cannt be empty")
        }
        if (apkName.isNullOrEmpty()) {
            apkName = "/update.apk"
        }
        val file = File(context.getExternalFilesDir(null)?.path, apkName)
        GlobalScope.launch(context = Dispatchers.IO) {
            context.apply {
                DownloadManager.download(url, file).collect { status ->
                    when (status) {
                        is DownloadStatus.Progress -> {
                            downloadListener.onProgress(status.value)
                        }
                        is DownloadStatus.Error -> {
                            downloadListener.onError(status.throwable)
                        }
                        is DownloadStatus.Done -> {
                            downloadListener.onFinish(file)
                            installApk(file)
                        }
                        is DownloadStatus.OnDownloading -> {
                            downloadListener.onDownloading(status.boolean)
                        }
                        is DownloadStatus.OnCancel -> {
                            downloadListener.onCancel()
                        }
                    }
                }
            }
        }
    }
    /**
     * 取消下载
     */
    fun cancel() {
        DownloadManager.cancel()
    }
    /**
     * 安装apk
     */
    private fun installApk(file: File) {
        if (!file.exists()) {
            Log.e("AppUpdate","apkfile not exists")
            return
        }
        val uri = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Uri.fromFile(file)
        } else {
            val authority: String = context.packageName + ".updateFileProvider"
            FileProvider.getUriForFile(context, authority, file)
        }
        val intent = Intent(Intent.ACTION_VIEW)
        val type = "application/vnd.android.package-archive"
        intent.setDataAndType(uri, type)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

}