package hku.hk.cs.a3330gp.ar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import hku.hk.cs.a3330gp.R
import hku.hk.cs.a3330gp.history.HistoryActivity

class AttendanceActivity : AppCompatActivity(), AttendanceArFragment.ARListener {
    private lateinit var photoSaver: PhotoSaver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance)

        photoSaver = PhotoSaver(this)

        supportFragmentManager.commit {
            add(R.id.containerFragment, AttendanceArFragment::class.java, Bundle())
        }
    }

    override fun onModelClick() {
        Log.d("Node Tap", "sup")
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
    }

    fun takePhoto() {

    }

}