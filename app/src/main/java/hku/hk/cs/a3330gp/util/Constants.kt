package hku.hk.cs.a3330gp.util

import hku.hk.cs.a3330gp.data.User

object Constants {
    fun getUsers(): List<User> {
        var userList = listOf<User>(
            User(
                424,
                "Gordan Chan",
                "profileImages/care_back.png",
                "augmentedImages/care_back.png",
                "models/Error.gltf"
            ),
            User(
                425,
                "Mary Ng",
                "profileImages/care_back",
                "augmentedImages/care_back.png",
                "models/Pin.gltf"
            )
        )
        return userList
    }
}