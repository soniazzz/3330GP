package hku.hk.cs.a3330gp

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import hku.hk.cs.a3330gp.data.Patient
import android.Manifest
import android.widget.Toast

class PatientProfileDetails : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.patient_profile_details)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val patient: Patient? = intent.getParcelableExtra("patient")

        val callPatientButton = findViewById<Button>(R.id.call_patient_button)
        val callEmergencyButton = findViewById<Button>(R.id.call_emergency_button)



        findViewById<TextView>(R.id.name).text = patient?.name
        findViewById<TextView>(R.id.sex).text = patient?.sex
        findViewById<TextView>(R.id.age).text = patient?.age.toString()
        findViewById<TextView>(R.id.address).text = patient?.address
        findViewById<TextView>(R.id.tel).text = patient?.tel
        findViewById<TextView>(R.id.emergency_contact).text = patient?.emergency_contact
        findViewById<TextView>(R.id.emergency_number).text = patient?.emergency_number


        callPatientButton.setOnClickListener {
            startCall(patient?.tel)
        }
        callEmergencyButton.setOnClickListener {
            startCall(patient?.emergency_number)
        }
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

    private fun startCall(number: String?) {
        val callIntent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$number")
        }
        startActivity(callIntent)
    }



}