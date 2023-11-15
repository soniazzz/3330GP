package hku.hk.cs.a3330gp.data
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.json.JSONObject
@Parcelize
data class HealthStatistics(
    val height: String,
    val weight: String,
    val bloodPressure: String,
    val pulseRate: String,
    val bloodOxygen: String,
    val medicalHistory: List<String>
) : Parcelable {
    companion object {
        fun fromJson(json: JSONObject): HealthStatistics {
            val medicalHistory = mutableListOf<String>()
            val medicalHistoryJsonArray = json.getJSONArray("medical_history")
            for (i in 0 until medicalHistoryJsonArray.length()) {
                medicalHistory.add(medicalHistoryJsonArray.getString(i))
            }
            return HealthStatistics(
                height = json.getString("height"),
                weight = json.getString("weight"),
                bloodPressure = json.getString("blood_pressure"),
                pulseRate = json.getString("pulse_rate"),
                bloodOxygen = json.getString("blood_oxygen"),
                medicalHistory = medicalHistory
            )
        }
    }
}

@Parcelize
data class PatientHealthStatistics(
    val id: String,
    val name: String,
    val gender: String,
    val age: String,
    val healthStatistics: HealthStatistics
) : Parcelable {
    companion object {
        fun fromJson(json: JSONObject): PatientHealthStatistics {
            return PatientHealthStatistics(
                id = json.getString("id"),
                name = json.getString("name"),
                gender = json.getString("gender"),
                age = json.getString("age"),
                healthStatistics = HealthStatistics.fromJson(json.getJSONObject("health_statistics"))
            )
        }
    }
}