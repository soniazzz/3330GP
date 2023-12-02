package hku.hk.cs.a3330gp.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.provider.ContactsContract.Contacts.Photo
import com.mapbox.geojson.Point
import hku.hk.cs.a3330gp.R
import hku.hk.cs.a3330gp.data.PhotoRecord
import hku.hk.cs.a3330gp.data.User

object Constants {
    const val TRANSITION = "transition"
    const val TRANSITION_MORPH = "t_morph"
    const val TRANSITION_FADE = "t_fade"
    const val HISTORY_TABLE = "history"
    const val HISTORY_ID = "id"
    const val HISTORY_DATE = "date_taken"
    const val HISTORY_IMAGE = "image_data"
    const val RECORD_ID = "record_id"

    fun getUsers(): List<User> {
        var userList = listOf<User>(
            User(
                424,
                "Gordan Chan",
                Point.fromLngLat(114.1412900657731, 22.28483675491897),
                "profileImages/care_back.png",
                "augmentedImages/care_back.png",
                "models/Error.gltf"
            ),
            User(
                425,
                "Mary Ng",
                Point.fromLngLat(114.1376394638438, 22.28483769988147),
                "profileImages/care_back",
                "augmentedImages/care_back.png",
                "models/Pin.gltf"
            ),
            User(
                426,
                "Billy Fischer",
                Point.fromLngLat(114.135833, 22.284367),
                "profileImages/care_back",
                "augmentedImages/care_back.png",
                "models/Pin.gltf"
            )
        )
        return userList
    }

    fun getPlaceholderRecords(context: Context): List<PhotoRecord> {
        val placeholderBmpList = listOf<Bitmap>(
            (context.resources.getDrawable(R.drawable.record_placeholder_1, null) as BitmapDrawable).bitmap,
            (context.resources.getDrawable(R.drawable.record_placeholder_2, null) as BitmapDrawable).bitmap,
            (context.resources.getDrawable(R.drawable.record_placeholder_3, null) as BitmapDrawable).bitmap,
            (context.resources.getDrawable(R.drawable.record_placeholder_4, null) as BitmapDrawable).bitmap
        )
        return listOf<PhotoRecord>(
            PhotoRecord(
                -1,
                "Irving (24 October 2023)",
                placeholderBmpList[0]
            ),
            PhotoRecord(
                -2,
                "Milton (14 August 2023)",
                placeholderBmpList[1]
            ),
            PhotoRecord(
                -3,
                "Raymond (27 July 2023)",
                placeholderBmpList[2]
            ),
            PhotoRecord(
                -4,
                "Walter (01 June 2023)",
                placeholderBmpList[3]
            ),
        )
    }
}