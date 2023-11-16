package hku.hk.cs.a3330gp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import hku.hk.cs.a3330gp.data.PatientHealthStatistics
import org.json.JSONObject

class HealthStatisticsDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_statistics_detail)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val patient = intent.getParcelableExtra<PatientHealthStatistics>("patientData")

        val medicalHistory = patient?.healthStatistics?.medicalHistory?.joinToString(", ")
        findViewById<TextView>(R.id.MedicalHistory).text = medicalHistory

        findViewById<TextView>(R.id.name).text = patient?.name
        findViewById<TextView>(R.id.gender).text = patient?.gender
        findViewById<TextView>(R.id.age).text = patient?.age
        findViewById<TextView>(R.id.height).text = patient?.healthStatistics?.height
        findViewById<TextView>(R.id.weight).text = patient?.healthStatistics?.weight
        findViewById<TextView>(R.id.pulse_rate).text = patient?.healthStatistics?.pulseRate
        findViewById<TextView>(R.id.blood_oxygen).text = patient?.healthStatistics?.bloodOxygen
        findViewById<TextView>(R.id.blood_pressure).text = patient?.healthStatistics?.bloodPressure


        findViewById<Button>(R.id.update_weight).setOnClickListener {
            updateWeight(patient?.id ?: "1")
        }
        findViewById<Button>(R.id.update_pulse_rate).setOnClickListener {

            updatePulseRate(patient?.id ?: "1")
        }
        findViewById<Button>(R.id.update_blood_oxygen).setOnClickListener {
            updateBloodOxygen(patient?.id ?: "1")
        }
        findViewById<Button>(R.id.update_blood_pressure).setOnClickListener {
            updateBloodPressure(patient?.id ?: "1")
        }

    }




    private fun updateWeight(patientId: String) {
        // Replace this URL with your actual endpoint
        val url = "http://10.70.21.92:5000/update_health_statistics?patient_id=$patientId"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val updatedWeight = response.getJSONObject("health_statistics").getString("weight")
                findViewById<TextView>(R.id.weight).text = updatedWeight
            },
            { error ->
                Log.e("HealthStatisticsDetailActivity", error.toString())
            }
        )

        Volley.newRequestQueue(this).add(jsonObjectRequest)
    }


    private fun updatePulseRate(patientId: String) {
        // Replace this URL with your actual endpoint
        val url = "http://10.70.21.92:5000/update_health_statistics?patient_id=$patientId"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val updatedData = response.getJSONObject("health_statistics").getString("pulse_rate")
                findViewById<TextView>(R.id.pulse_rate).text = updatedData
            },
            { error ->
                Log.e("HealthStatisticsDetailActivity", error.toString())
            }
        )

        Volley.newRequestQueue(this).add(jsonObjectRequest)
    }

    private fun updateBloodOxygen(patientId: String) {
        // Replace this URL with your actual endpoint
        val url = "http://10.70.21.92:5000/update_health_statistics?patient_id=$patientId"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val updatedData = response.getJSONObject("health_statistics").getString("blood_oxygen")
                findViewById<TextView>(R.id.blood_oxygen).text = updatedData
            },
            { error ->
                Log.e("HealthStatisticsDetailActivity", error.toString())
            }
        )

        Volley.newRequestQueue(this).add(jsonObjectRequest)
    }

    private fun updateBloodPressure(patientId: String) {
        // Replace this URL with your actual endpoint
        val url = "http://10.70.21.92:5000/update_health_statistics?patient_id=$patientId"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val updatedData = response.getJSONObject("health_statistics").getString("blood_pressure")
                findViewById<TextView>(R.id.blood_pressure).text = updatedData
            },
            { error ->
                Log.e("HealthStatisticsDetailActivity", error.toString())
            }
        )

        Volley.newRequestQueue(this).add(jsonObjectRequest)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Respond to the action bar's Up/Home button
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}