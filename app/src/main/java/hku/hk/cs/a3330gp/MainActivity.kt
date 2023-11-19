package hku.hk.cs.a3330gp

import android.app.ActivityOptions
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import hku.hk.cs.a3330gp.ar.AttendanceActivity
import hku.hk.cs.a3330gp.data.CareTaking
import hku.hk.cs.a3330gp.data.Patient
import hku.hk.cs.a3330gp.data.PatientHealthStatistics
import hku.hk.cs.a3330gp.history.HistoryActivity
import hku.hk.cs.a3330gp.map.NavigationActivity
import hku.hk.cs.a3330gp.util.Constants
import hku.hk.cs.a3330gp.util.NavigationUtil
import org.json.JSONArray


class MainActivity : AppCompatActivity(), TopAppBarFragment.TopAppBarListener {
    private lateinit var btnAR: Button
    private lateinit var btnMap: Button
    private lateinit var btnCalendar: Button
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var btnCareList: Button
    private lateinit var userId: String
    private lateinit var options:ActivityOptions

    private var transition = Constants.TRANSITION_MORPH

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userId = "1"
//        userId = intent.extras?.getString("id").toString()

        btnAR = findViewById(R.id.btnAR)
        btnCalendar = findViewById(R.id.calendar)
        btnMap = findViewById(R.id.btnMap)
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        btnCareList = findViewById(R.id.jobs)

        btnAR.setOnClickListener { startAr() }
        btnMap.apply {
            this.transitionName = "shared_map"
            this.setOnClickListener{
                options = ActivityOptions.makeSceneTransitionAnimation(
                    this@MainActivity,
                    this@apply,
                    "shared_map"
                )
                setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
                transition = Constants.TRANSITION_MORPH
                startMap()
            }
        }
        btnCareList.setOnClickListener{
            startShowList()
        }
        btnCalendar.setOnClickListener{
            startCalendar()
        }

        val health =  findViewById<Button>(R.id.health)
        val profile =  findViewById<Button>(R.id.profile)
        profile.setOnClickListener{
            val uid:String = "1"
            sendMessage(uid)
        }

        health.setOnClickListener{
            val uid:String = "1"
            getHealthStatistics(uid)
        }

        navigationView.menu.findItem(R.id.nav_home).isChecked = true

        NavigationUtil.setupNavigationView(navigationView, drawerLayout, this)

        supportFragmentManager.beginTransaction()
            .replace(R.id.appBarFragment, TopAppBarFragment(), "AppBar")
            .commit()

    }

    private fun startCareTakingVideo() {
        startActivity(Intent(this, CareTakingVideosActivity::class.java))
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
        intent.putExtra(Constants.TRANSITION, transition)
        startActivity(intent, options.toBundle())
    }
    private fun startHistory() {
        val intent = Intent(this, HistoryActivity::class.java)
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

    private fun getHealthStatistics(carer_id: String) {
        val url = "${getString(R.string.server_ip)}/health_statistics?carer_id=$carer_id"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                renderHealthStatistics(response)
            },
            { error ->
                Log.e("MainActivity", error.toString())
            }
        )
        Volley.newRequestQueue(this).add(jsonArrayRequest)
    }

    private fun renderHealthStatistics(jsonArray: JSONArray) {
        val patientsList = arrayListOf<PatientHealthStatistics>()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val patient = PatientHealthStatistics.fromJson(jsonObject) // assuming PatientHealthStatistics.fromJson() exists
            patientsList.add(patient)
        }
        val intent = Intent(this, HealthStatistics::class.java).apply {
            putParcelableArrayListExtra("data", patientsList)
        }
        startActivity(intent)
    }


    fun sendMessage(carer_id:String) {
        val url = "${getString(R.string.server_ip)}/patients?carer_id=$carer_id"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                renderProfiles(response)
            },
            { error ->
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

    private fun startShowList() {
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.constraintlayout1, HomeFragment(), "Caretaking")
//            .commit()
        val url = "${getString(R.string.server_ip)}/jobs"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener<JSONArray> { response ->
                renderJobList(response)
            },
            Response.ErrorListener { error ->
                Log.e("MyCareTakingListActivity", error.toString())
            }
        )
        Volley.newRequestQueue(this).add(jsonArrayRequest)
    }

    private fun renderJobList(jsonArray: JSONArray) {
        val careTakingList = arrayListOf<CareTaking>()
        for (i in 0 until jsonArray.length()){
            val careTakingObj = jsonArray.getJSONObject(i)
            val id = careTakingObj.getString("id")
            val jobTitle = careTakingObj.getString("jobTitle")
            val uuid = careTakingObj.getString("userId")
            val place = careTakingObj.getString("place")
            val jobDetails = careTakingObj.getString("jobDetails")
            val jobTime = careTakingObj.getString("jobTime")
            val salary = careTakingObj.getString("salary")
            careTakingList.add(CareTaking(id, uuid, jobTitle, place, jobDetails, jobTime, salary))
        }
        val intent = Intent(this, CareTakingListActivity::class.java).apply {
            putParcelableArrayListExtra("data", careTakingList)
            putExtra("id", userId)
        }
        startActivity(intent)
    }

    private fun startCalendar() {
        val url = "${getString(R.string.server_ip)}/users?userId=$userId"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener<JSONArray> { response ->
                renderCalendar(response)
            },
            Response.ErrorListener { error ->
                Log.e("CalendarActivity", error.toString())
            }
        )
        Volley.newRequestQueue(this).add(jsonArrayRequest)


    }

    private fun renderCalendar(jsonArray: JSONArray) {

        val careTakingList = arrayListOf<CareTaking>()
        for (i in 0 until jsonArray.length()){
            val careTakingObj = jsonArray.getJSONObject(i)
            val id = careTakingObj.getString("id")
            val jobTitle = careTakingObj.getString("jobTitle")
            val uuid = careTakingObj.getString("userId")
            val place = careTakingObj.getString("place")
            val jobDetails = careTakingObj.getString("jobDetails")
            val jobTime = careTakingObj.getString("jobTime")
            val salary = careTakingObj.getString("salary")
            careTakingList.add(CareTaking(id, uuid, jobTitle, place, jobDetails, jobTime, salary))
        }
        val intent = Intent(this, GoogleCalendarView::class.java).apply {
            putParcelableArrayListExtra("data", careTakingList)
            putExtra("id", userId)
        }
        startActivity(intent)
    }
}