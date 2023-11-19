package hku.hk.cs.a3330gp.data

import android.graphics.Bitmap

data class PhotoRecord(
    val id: Int,
    val dateTaken: String,
    val imageData: Bitmap
)
