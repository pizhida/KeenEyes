package com.google.firebase.codelab.mlkit.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import com.google.firebase.codelab.mlkit.interfaces.ActionCallback
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class FileManager {

    val myDirectory: String
        get() {
//            if (UserManager.getInstance().isAuthorized) {
//                val userId = UserManager.getInstance().profile!!.accountId
//                return Environment.getExternalStorageDirectory().path + "/" + AppInfoUtils.getInstance().APP_NAME + "/." + userId
//            }
            run { return Environment.getExternalStorageDirectory().path + "/" + "test" + "/.guest" }
        }

    fun createFile(directory: String, fileName: String, fileMime: String): Boolean {
        val dir = File(directory)
        if (dir.exists() || dir.mkdirs()) {
            val fullFile = File(dir, fileName + fileMime)
            try {
                return fullFile.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return false
    }

    fun createFile(file: File): Boolean {
        if (file.parentFile.exists() || file.parentFile.mkdirs()) {
            try {
                return file.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return false
    }


    fun deleteFile(file: File?): Boolean {
        return file != null && file.exists() && file.delete()
    }

    fun renameFile(file: File, newFile: File): File? {
        if (file.exists()) {
            if (file.renameTo(newFile)) {
                return newFile
            }
        }
        return null
    }

    fun getFileInDirectory(dirName: String): List<File> {
        val files = ArrayList<File>()
        val file = File(dirName)
        val filesArr = file.list()
        for (s in filesArr) {
            val _file = File(s)
            if (!_file.isDirectory) {
                files.add(_file)
            }
        }
        return files
    }

    fun getFileDuration(pointedFile: File, callback: ActionCallback<Int>) {
        val player = MediaPlayer()
        try {
            player.setDataSource(pointedFile.path)
            player.setOnPreparedListener { mediaPlayer ->
                callback.onSuccess(mediaPlayer.duration)
                mediaPlayer.release()
            }
            player.prepareAsync()
        } catch (e: IOException) {
            e.printStackTrace()
            callback.onSuccess(0)
        }

    }

    fun createVideoThumbnail(context: Context, uri: Uri): String? {
        val file = File(getPathFromUri(context, uri))
        val outputDir = context.cacheDir
        val resultFile = File(outputDir, "temp_thumbnail_" + Date().time + ".jpg")
        var out: FileOutputStream? = null
        try {
            val original = ThumbnailUtils.createVideoThumbnail(file.path, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND)
            val defH = original.height.toFloat()
            val defW = original.width.toFloat()
            val ratio = defW / defH
            val expectH = NumberUtils.toPixcel(context, 190f).toFloat()
            val expectW = (expectH * ratio).toInt().toFloat()
            val bitmap = Bitmap.createScaledBitmap(original, expectW.toInt(), expectH.toInt(), true)
            out = FileOutputStream(resultFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
            bitmap.recycle()
            original.recycle()
            return resultFile.path
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (out != null) {
                    out.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return null
    }

    private fun getPathFromUri(context: Context, uri: Uri): String {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, filePathColumn, null, null, null)
                ?: return uri.path
        cursor.moveToFirst()
        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        val picturePath = cursor.getString(columnIndex)
        cursor.close()
        return picturePath
    }

    fun saveBitmap(imageBitmap: Bitmap, callback: ActionCallback<Uri>)
    {
        Observable.fromCallable {
            val resultFile = File(myDirectory, "sss" + Date().time + ".jpg")
            if (resultFile.parentFile.exists() || resultFile.parentFile.mkdirs()) {
                val out = FileOutputStream(resultFile)
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out)
                imageBitmap.recycle()
                Uri.fromFile(resultFile)
            } else {
                null
            }
        }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { uri -> callback.onSuccess(uri!!) }, Consumer { throwable ->
                    if (true) {
                        callback.onFailed(throwable.message ?: "")
                    } else {
                        callback.onFailed("Take a photo failed")
                    }
                })

    }

    companion object {

        val PICKFILE_REQUEST_CODE = 1020
        private var INSTANCE: FileManager? = null
        private var mCallback: ActionCallback<Boolean>? = null

        val instance: FileManager
            @Synchronized get() {
                if (INSTANCE == null) {
                    INSTANCE = FileManager()
                }
                return INSTANCE!!
            }

        fun requestPermission(activity: Activity, callback: ActionCallback<Boolean>) {
            mCallback = callback
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PICKFILE_REQUEST_CODE)
        }

        fun isHavePermissions(activity: Activity): Boolean {
            val res = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            return res == PackageManager.PERMISSION_GRANTED
        }

        fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {
            if (requestCode == PICKFILE_REQUEST_CODE) {
                if (grantResults.size > 0) {
                    if (mCallback != null) {
                        mCallback!!.onSuccess(true)
                    }
                } else {
                    if (mCallback != null) {
                        mCallback!!.onSuccess(false)
                    }
                }
            }
        }
    }
}