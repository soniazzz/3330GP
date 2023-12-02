package hku.hk.cs.a3330gp.ar

import android.app.Activity
import android.graphics.Bitmap
import android.os.Handler
import android.os.HandlerThread
import android.view.PixelCopy
import android.widget.Toast
import hku.hk.cs.a3330gp.DatabaseHelper
import io.github.sceneview.ar.ARSceneView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PhotoSaver(
    private val activity: Activity
) {
    private val date = SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.getDefault()).format(Date())
    private lateinit var databaseHelper: DatabaseHelper

    fun takePhoto(arSceneView: ARSceneView, callback: (Long) -> Unit): Long {
        databaseHelper = DatabaseHelper(activity)
        val bmp = Bitmap.createBitmap(arSceneView.width, arSceneView.height, Bitmap.Config.ARGB_8888)
        val handlerThread = HandlerThread("PixelCopyThread")
        var recordId = 0L
        handlerThread.start()

        PixelCopy.request(arSceneView, bmp, { result ->
            if(result == PixelCopy.SUCCESS) {
                recordId = databaseHelper.savePhoto(bmp, date)
                activity.runOnUiThread {
                    Toast.makeText(activity, "Successfully took photo!", Toast.LENGTH_LONG).show()
                }
            } else {
                activity.runOnUiThread {
                    Toast.makeText(activity, "Failed to take photo.", Toast.LENGTH_LONG).show()
                }
            }
            handlerThread.quitSafely()
            callback(recordId)
        }, Handler(handlerThread.looper))
        return recordId
    }

}