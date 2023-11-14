package hku.hk.cs.a3330gp.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CareTaking(
    val id: String,
    val jobTitle: String,
    val place: String,
    val jobDetails: String,
    val jobTime: String,
    val salary: String
) : Parcelable
