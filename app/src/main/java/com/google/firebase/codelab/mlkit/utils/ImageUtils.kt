package com.google.firebase.codelab.mlkit.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import com.google.firebase.codelab.mlkit.interfaces.ImageUtilsCallback
import com.google.firebase.codelab.mlkit.interfaces.OnpermissionResultListener
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.File
import java.util.*

class ImageUtils<T : View> {

    private var currentView: T? = null
    private var callback: ImageUtilsCallback<T>? = null
    private var mCropShape: CropImageView.CropShape = CropImageView.CropShape.RECTANGLE
    private var mFixAspectRatio = false
    private var mAspectW = 0
    private var mAspectH = 0
    private var currentTakePictureFile: File? = null

    fun pickImage(activity: Activity)
    {
        if (PermissionManager.getInstance().isHavePermission(activity, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
            currentTakePictureFile = null
            //currentView = view
            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            activity.startActivityForResult(photoPickerIntent, REQ_CODE)
        } else {
            PermissionManager.getInstance().setPermissionResultListener(object : OnpermissionResultListener {
                override fun onResult(permission: List<String>, grandted: List<Boolean>) {
                    if (grandted.onEach { it }.size == grandted.size) {
                        pickImage(activity)
                    }
                }
            })
            PermissionManager.getInstance().requestPermission(activity, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
        }
    }

    fun takeAPicture(activity: Activity)
    {
        currentTakePictureFile = null
        if (PermissionManager.getInstance().isHavePermission(activity, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA))) {
            //currentView = view
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
                currentTakePictureFile = File(FileManager.instance.myDirectory, "${Date().time}.jpg")
                if (FileManager.instance.createFile(currentTakePictureFile!!)) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, UriCompat.fromFile(activity, currentTakePictureFile!!))
                    activity.startActivityForResult(takePictureIntent, REQ_CODE)
                }
            }
        } else {
            PermissionManager.getInstance().setPermissionResultListener(object : OnpermissionResultListener {
                override fun onResult(permission: List<String>, grandted: List<Boolean>) {
                    if (grandted.onEach { it }.size == grandted.size) {
                        //takeAPicture(activity, view)
                        takeAPicture(activity)
                    }
                }
            })
            PermissionManager.getInstance().requestPermission(activity, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA))
        }
    }

    fun setConfig(fixAspectRatio: Boolean, cropShape: CropImageView.CropShape, aspectW: Int, aspectH: Int): ImageUtils<T> {
        mFixAspectRatio = fixAspectRatio
        mCropShape = cropShape
        mAspectW = aspectW
        mAspectH = aspectH
        return this
    }

    fun cropImage(resultCode: Int, activity: Activity, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (data != null && data.data != null) {
                val builder = CropImage.activity(data.data)
//                if ((mAspectW > 0) or (mAspectH > 0)) {
//                    builder.setAspectRatio(mAspectW, mAspectH)
//                }
//                builder.setFixAspectRatio(mFixAspectRatio)
                builder.setCropShape(mCropShape)
                builder.start(activity)
            } else if (currentTakePictureFile != null) {

                galleryAddPic(activity, currentTakePictureFile!!)

                val builder = CropImage.activity(Uri.fromFile(currentTakePictureFile))
//                if ((mAspectW > 0) or (mAspectH > 0)) {
//                    builder.setAspectRatio(mAspectW, mAspectH)
//                }
//                builder.setFixAspectRatio(mFixAspectRatio)
                builder.setCropShape(mCropShape)
                builder.start(activity)

            } else {
                reset()
            }
        } else {
            reset()
        }
    }

    private fun galleryAddPic(activity: Activity, file: File) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val f = File(file.path)
        val contentUri = Uri.fromFile(f)
        mediaScanIntent.data = contentUri
        activity.sendBroadcast(mediaScanIntent)
    }


    fun setCallback(callback: ImageUtilsCallback<T>) {
        this.callback = callback
    }

    fun onCropImageResult(resultCode: Int, data: Intent?) {
        if (resultCode != CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            if (callback != null) {
                val result = CropImage.getActivityResult(data)
                if (result != null) {
                    if (result.isSuccessful) {
                        callback!!.onPicked(currentView, result.uri)
                    }
                }
            }
        }
        reset()
    }

    fun reset() {
        mCropShape = CropImageView.CropShape.RECTANGLE
        mFixAspectRatio = false
        mAspectW = 0
        mAspectH = 0
    }

    companion object {
        var REQ_CODE = 1019
    }

}