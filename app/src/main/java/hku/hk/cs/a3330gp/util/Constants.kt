package hku.hk.cs.a3330gp.util

import com.mapbox.geojson.Point
import hku.hk.cs.a3330gp.data.User

object Constants {
    const val TRANSITION = "transition"
    const val TRANSITION_MORPH = "t_morph"
    const val TRANSITION_FADE = "t_fade"

    fun getUsers(): List<User> {
        var userList = listOf<User>(
            User(
                424,
                "Gordan Chan",
                Point.fromLngLat(114.137339, 22.281667),
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
}