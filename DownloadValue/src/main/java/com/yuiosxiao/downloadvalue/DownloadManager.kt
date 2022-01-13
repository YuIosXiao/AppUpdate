package com.yuiosxiao.downloadvalue

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.*
import java.io.File
import java.io.IOException

object DownloadManager {

    private val callList = arrayListOf<Call>()

    /**
     * 文件下载
     */
    fun download(url: String, file: File): Flow<DownloadStatus> {
        return flow {
            val request = Request.Builder().url(url).get().build()
            val okCall = OkHttpClient.Builder().build().newCall(request)
            callList.add(okCall)
            val response = okCall.execute()
            if (response.isSuccessful) {
                emit(DownloadStatus.OnDownloading(true))
                response.body()!!.let { body ->
                    val total = body.contentLength()
                    //文件读写
                    file.outputStream().use { output ->
                        val input = body.byteStream()
                        var emittedProgress = 0L
                        input.copyTo(output) { bytesCopied ->
                            //获取下载进度百分比
                            val progress = bytesCopied * 100 /total
                            if (progress - emittedProgress > 5) {
                                delay(100)
                                emit(DownloadStatus.Progress(progress.toInt()))
                                emittedProgress = progress
                            }
                        }
                    }
                }
                emit(DownloadStatus.Done(file))
                //安装apk
            } else {
                if (okCall.isCanceled) {
                    emit(DownloadStatus.OnCancel(response.toString()))
                } else {
                    emit(DownloadStatus.OnDownloading(false))
                }
                throw IOException(response.toString())
            }
        }.catch {
            //下载失败，删除改文件，并发送失败通知
            file.delete()
            emit(DownloadStatus.Error(it))
        }.flowOn(Dispatchers.IO)
    }
    /**
     * 取消下载
     */
    fun cancel() {
        callList.forEach {
            if (!it.isCanceled) {
                it.cancel()
            }
        }
    }
}