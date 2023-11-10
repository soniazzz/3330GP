package hku.hk.cs.a3330gp.ar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import hku.hk.cs.a3330gp.R

class AttendanceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance)

        supportFragmentManager.commit {
            add(R.id.containerFragment, AttendanceArFragment::class.java, Bundle())
        }
    }

}