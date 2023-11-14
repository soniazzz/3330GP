package hku.hk.cs.a3330gp

import CareTakingAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hku.hk.cs.a3330gp.data.CareTaking
import hku.hk.cs.a3330gp.data.Patient

class CareTakingListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val careTakingList: ArrayList<CareTaking> = intent.getParcelableArrayListExtra<CareTaking>("data") ?: arrayListOf()
        setContentView(R.layout.activity_care_taking_list)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CareTakingAdapter(careTakingList)

    }
}