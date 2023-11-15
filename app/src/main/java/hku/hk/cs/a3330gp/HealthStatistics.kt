package hku.hk.cs.a3330gp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hku.hk.cs.a3330gp.data.PatientHealthStatistics

class HealthStatistics:  AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.health_statistics)

        val patientsList = intent.getParcelableArrayListExtra<PatientHealthStatistics>("data") ?: arrayListOf()
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewManager = LinearLayoutManager(this)
        viewAdapter = PatientAdapter(patientsList)

        recyclerView = findViewById<RecyclerView>(R.id.health_statistics_recycler_view).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    inner class PatientViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name)
        val gender: TextView = view.findViewById(R.id.gender)
        val age: TextView = view.findViewById(R.id.age)
    }

    inner class PatientAdapter(private val myDataset: ArrayList<PatientHealthStatistics>) :
        RecyclerView.Adapter<PatientViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.health_statistics_card, parent, false)

            return PatientViewHolder(view)
        }

        override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
            holder.name.text = myDataset[position].name
            holder.gender.text = myDataset[position].gender
            holder.age.text = myDataset[position].age.toString()
        }

        override fun getItemCount() = myDataset.size
    }
}