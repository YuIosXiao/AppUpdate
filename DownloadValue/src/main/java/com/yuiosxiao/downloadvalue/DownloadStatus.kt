package com.yuiosxiao.downloadvalue

import java.io.File

sealed class DownloadStatus {

    object None: DownloadStatus()//空状态
    data class OnCancel(val response: String): DownloadStatus()
    data class OnDownloading(val boolean: Boolean): DownloadStatus() //是否开始下载
    data class Progress(val value: Int): DownloadStatus()//下载进度
    data class Error(val throwable: Throwable): DownloadStatus()//错误
    data class Done(val file: File): DownloadStatus()//下载完成

}