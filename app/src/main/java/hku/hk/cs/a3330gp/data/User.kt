package hku.hk.cs.a3330gp.data

import com.mapbox.geojson.Point

data class User(
    val id: Int,
    val name: String,
    val location:Point,
    val profileImg: String,
    val augmentedImg: String,
    val model: String
)
