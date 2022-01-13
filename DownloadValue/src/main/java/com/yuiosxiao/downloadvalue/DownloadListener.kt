package com.yuiosxiao.downloadvalue

import java.io.File

interface DownloadListener {

    fun onProgress(progress: Int)

    fun onDownloading(boolean: Boolean)

    fun onFinish(file: File)

    fun onError(throwable: Throwable)

    fun onCancel()

}