package hku.hk.cs.a3330gp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import hku.hk.cs.a3330gp.data.Patient
import org.json.JSONArray
import org.json.JSONObject


class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

        val patientsList: ArrayList<Patient> = intent.getParcelableArrayListExtra<Patient>("data") ?: arrayListOf()

        val listView: ListView = findViewById(R.id.list_view)
        listView.adapter = PatientsAdapter(this, patientsList)

        listView.setOnItemClickListener { parent, view, position, id ->
            val patient = patientsList[position]
            // Handle list item click
            startActivity(Intent(this, Profile::class.java))
        }
    }


}