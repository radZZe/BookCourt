package com.example.bookcourt.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

class BitmapUtils {
    companion object{
        /**
         * caches given image in external cache (jpeg format) and returns its uri
         */
        fun getBitmapUri(
            context: Context,
            inImage: Bitmap,
            imageName:String,
            path:String
        ): Uri {

            val cachePath = File(context.externalCacheDir,path)
            cachePath.mkdir()
            val file = File(cachePath,"$imageName.jpeg")
            val fileOut = FileOutputStream(file)
            inImage.compress(Bitmap.CompressFormat.JPEG, 100, fileOut)
            fileOut.close()
            return FileProvider.getUriForFile(context,context.packageName+".provider",file)
        }
    }
}