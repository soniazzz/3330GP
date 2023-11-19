package hku.hk.cs.a3330gp.history

import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.carousel.CarouselLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.google.android.material.carousel.FullScreenCarouselStrategy
import com.google.android.material.navigation.NavigationView
import hku.hk.cs.a3330gp.DatabaseHelper
import hku.hk.cs.a3330gp.R
import hku.hk.cs.a3330gp.TopAppBarFragment
import hku.hk.cs.a3330gp.data.PhotoRecord
import hku.hk.cs.a3330gp.util.Constants
import hku.hk.cs.a3330gp.util.NavigationUtil
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


class HistoryActivity : AppCompatActivity(), TopAppBarFragment.TopAppBarListener, HistoryDialogFragment.HistoryDialogListener {

    private lateinit var databaseHelper: DatabaseHelper
//    private val photoManager = PhotoManager(this)
    private lateinit var missingContainer: LinearLayout
    private lateinit var rvHistory:RecyclerView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private var historyRecords = mutableListOf<PhotoRecord>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        databaseHelper = DatabaseHelper(this)
        missingContainer = findViewById(R.id.missingContainer)
        rvHistory = findViewById(R.id.rvHistory)
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        val recordId = intent.getLongExtra(Constants.RECORD_ID, -1L)
        if (recordId != -1L) {
            showDialog(recordId)
        }

        loadImage()

        navigationView.menu.findItem(R.id.nav_history).isChecked = true

        NavigationUtil.setupNavigationView(navigationView, drawerLayout, this)

        supportFragmentManager.beginTransaction()
            .replace(R.id.appBarFragment, TopAppBarFragment(), "AppBar")
            .commit()
    }

    private fun loadImage() {
        val cursor: Cursor? = databaseHelper.readImage()

        val idIndex = cursor!!.getColumnIndex(Constants.HISTORY_ID)
        val dateIndex = cursor!!.getColumnIndex(Constants.HISTORY_DATE)
        val imageIndex = cursor!!.getColumnIndex(Constants.HISTORY_IMAGE)
        // Move to the first row
        Log.d("curse size", "${cursor.count}")
        if (cursor!!.moveToFirst() && idIndex != -1 && dateIndex != -1 && imageIndex != -1) {
            do {
                // Perform operations with cursor data
                val id = cursor.getInt(idIndex)
                val date = cursor.getString(dateIndex)
                val imageData = cursor.getBlob(imageIndex)
                lifecycleScope.launch {
                    val image = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)
                    val record = PhotoRecord(id, date, image)
                    historyRecords.add(record)
                }

                // Move to the next row
            } while (cursor.moveToNext())
            addPlaceholderRecords()
            rvHistory.layoutManager = CarouselLayoutManager(FullScreenCarouselStrategy(), RecyclerView.VERTICAL)
            rvHistory.adapter = HistoryAdapter(historyRecords)
            val snapHelper = CarouselSnapHelper()
            snapHelper.attachToRecyclerView(rvHistory)
        }
        else {
            missingContainer.visibility = View.VISIBLE
        }

// Close the cursor when done
        cursor.close()
    }

    private fun showDialog(recordId:Long) {
        val cursor: Cursor? = databaseHelper.readImage(recordId)
        val dateIndex = cursor!!.getColumnIndex(Constants.HISTORY_DATE)
        val imageIndex = cursor!!.getColumnIndex(Constants.HISTORY_IMAGE)
        Log.d("dialog curse", cursor.count.toString())
        if (cursor!!.moveToFirst()) {
            val date = cursor.getString(dateIndex)
            val formatter = SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.getDefault())
            val dateTime = formatter.parse(date)

            val imageData = cursor.getBlob(imageIndex)
            Log.d("date", "$date")
            val historyDialogFragment = HistoryDialogFragment.newInstance(
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(dateTime!!),
                SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(dateTime),
                imageData
            )
            historyDialogFragment.show(supportFragmentManager, "HISTORY_DIALOG")
        }
    }

    private fun addPlaceholderRecords() {
        historyRecords.addAll(Constants.getPlaceholderRecords(this))
    }

    override fun onResume() {
        super.onResume()
        navigationView.menu.findItem(R.id.nav_home).isChecked = false
        navigationView.menu.findItem(R.id.nav_navigation).isChecked = false
        navigationView.menu.findItem(R.id.nav_ar).isChecked = false
        navigationView.menu.findItem(R.id.nav_history).isChecked = true
    }

    override fun onNavigationIconClick() {
        drawerLayout.openDrawer(navigationView)
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        Log.d("Positive click", "Dialog checked")
    }
}