package hku.hk.cs.a3330gp

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import hku.hk.cs.a3330gp.ar.AttendanceActivity
import hku.hk.cs.a3330gp.map.NavigationActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import android.util.Log
import com.android.volley.toolbox.JsonArrayRequest
import hku.hk.cs.a3330gp.data.Patient

class MainActivity : AppCompatActivity(), TopAppBarFragment.TopAppBarListener {
    private lateinit var btnAR: Button
    private lateinit var btnMap: Button
    private lateinit var btnTheme: Button
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnAR = findViewById(R.id.btnAR)
        btnTheme = findViewById(R.id.btnTheme)
        btnMap = findViewById(R.id.btnMap)
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)

        btnAR.setOnClickListener { startAr() }
        btnMap.setOnClickListener { startMap() }
        btnTheme.setOnClickListener { switchTheme() }
        val health =  findViewById<Button>(R.id.health)
        val profile =  findViewById<Button>(R.id.profile)
        profile.setOnClickListener{
            val uid:String = "123"
            sendMessage(uid)
        }

        health.setOnClickListener{
            startActivity(Intent(this, Profile::class.java))
        }

        navigationView.menu.findItem(R.id.nav_home).isChecked = true

        navigationView.setNavigationItemSelectedListener { menuItem ->
            // Handle menu item selected
            drawerLayout.close()
            if (menuItem.itemId != R.id.nav_home) {
                navigationView.menu.findItem(R.id.nav_home).isChecked = false
            }

            when (menuItem.itemId) {
                R.id.nav_navigation -> startMap()
                R.id.nav_ar -> startAr()
                R.id.nav_history -> Toast.makeText(this, "You got me!", Toast.LENGTH_SHORT).show()
            }
            true
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.appBarFragment, TopAppBarFragment(), "AppBar")
            .commit()
    }

    override fun onResume() {
        super.onResume()
        navigationView.menu.findItem(R.id.nav_home).isChecked = true
        navigationView.menu.findItem(R.id.nav_navigation).isChecked = false
        navigationView.menu.findItem(R.id.nav_ar).isChecked = false
        navigationView.menu.findItem(R.id.nav_history).isChecked = false
    }

    private fun startAr() {
        val intent = Intent(this, AttendanceActivity::class.java)
        startActivity(intent)
    }
    private fun startMap() {
        val intent = Intent(this, NavigationActivity::class.java)
        startActivity(intent)
    }
    private fun switchTheme() {
        val currentNightMode = this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_NO -> {AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)} // Night mode is not active, we're using the light theme
            Configuration.UI_MODE_NIGHT_YES -> {AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)} // Night mode is active, we're using dark theme
        }
    }

    override fun onNavigationIconClick() {
        drawerLayout.openDrawer(navigationView)
    }

    fun sendMessage(name:String) {
        val url = "http://10.70.21.92:5000/patients"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener<JSONArray> { response ->
                renderProfiles(response)
            },
            Response.ErrorListener { error ->
                Log.e("MyActivity", error.toString())
            }
        )
        Volley.newRequestQueue(this).add(jsonArrayRequest)
    }

    fun renderProfiles(jsonArray: JSONArray){
        val patientsList = arrayListOf<Patient>()
        for (i in 0 until jsonArray.length()){
            val patientObj = jsonArray.getJSONObject(i)
            val id = patientObj.getString("id")
            val name = patientObj.getString("name")
            val sex = patientObj.getString("sex")
            val age = patientObj.getInt("age")
            val address = patientObj.getString("address")
            val tel = patientObj.getString("tel")
            val emergency_contact = patientObj.getString("emergency_contact")
            val emergency_number = patientObj.getString("emergency_number")
            patientsList.add(Patient(id, name, sex, age, address, tel, emergency_contact, emergency_number))
        }
        val intent = Intent(this, Profile::class.java).apply {
            putParcelableArrayListExtra("data", patientsList)
        }
        startActivity(intent)
    }
}