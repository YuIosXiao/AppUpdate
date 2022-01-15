# AppUpdate
AppUpdate协程下载应用升级库


# Dependency
step1.
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

step2.
```
implementation 'com.github.YuIosXiao:AppUpdate:2.0.0'
```
## 接入demo示例
```
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
```
