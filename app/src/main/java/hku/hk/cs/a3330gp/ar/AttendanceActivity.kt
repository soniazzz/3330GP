package hku.hk.cs.a3330gp.ar

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import hku.hk.cs.a3330gp.R
import hku.hk.cs.a3330gp.history.HistoryActivity
import hku.hk.cs.a3330gp.util.Constants
import io.github.sceneview.ar.ARSceneView
import kotlinx.coroutines.launch

class AttendanceActivity : AppCompatActivity(), AttendanceArFragment.ARInterface {
    private lateinit var photoSaver: PhotoSaver
    private lateinit var sceneView: ARSceneView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance)

        photoSaver = PhotoSaver(this)

        supportFragmentManager.commit {
            add(R.id.containerFragment, AttendanceArFragment::class.java, Bundle())
        }
    }

    override fun onSceneViewInstanceReady(arSceneView: ARSceneView) {
        sceneView = arSceneView
    }

    override fun onModelClick() {
        lifecycleScope.launch {
            photoSaver.takePhoto(sceneView) { recordId ->
                val intent = Intent(this@AttendanceActivity, HistoryActivity::class.java)
                intent.putExtra(Constants.RECORD_ID, recordId)
                startActivity(intent)
                finish()
            }
        }
    }
}