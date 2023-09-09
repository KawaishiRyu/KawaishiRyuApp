package app.kawaishiryu.jiujitsu.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException

object ImageManipulationUtil {

    //Bajamos la calidad de la imagen
    fun compressBitmap(uri: Uri, context: Context, quality: Int = 100): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            BitmapFactory.decodeStream(inputStream, null, options)?.let { bitmap ->
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                BitmapFactory.decodeByteArray(
                    outputStream.toByteArray(),
                    0,
                    outputStream.toByteArray().size
                )
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }
    }

}