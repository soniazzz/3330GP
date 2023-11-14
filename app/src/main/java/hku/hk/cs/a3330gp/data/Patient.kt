package hku.hk.cs.a3330gp.data
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Patient(
    val id: String,
    val name: String,
    val sex: String,
    val age: Int,
    val address: String,
    val tel: String,
    val emergency_contact: String,
    val emergency_number: String
) : Parcelable

