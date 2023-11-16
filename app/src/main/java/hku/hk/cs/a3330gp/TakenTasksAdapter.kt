package hku.hk.cs.a3330gp

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import hku.hk.cs.a3330gp.data.CareTaking
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class TakenTasksAdapter(private val list: List<CareTaking>, private val userId: String, private val context: Context) : RecyclerView.Adapter<TakenTasksAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val jobID: TextView = itemView.findViewById(R.id.jobID)
        val jobTitle: TextView = itemView.findViewById(R.id.jobTitle)
        val place: TextView = itemView.findViewById(R.id.place)
        val jobDetails: TextView = itemView.findViewById(R.id.jobDetails)
        val jobTime: TextView = itemView.findViewById(R.id.jobTime)
        val addCalendarButton: Button = itemView.findViewById(R.id.addCalendar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.google_calendar_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        holder.jobID.text = currentItem.id
        holder.jobTitle.text = currentItem.jobTitle
        holder.place.text = currentItem.place
        holder.jobDetails.text = currentItem.jobDetails
        holder.jobTime.text = currentItem.jobTime
//        holder.addCalendarButton.text =
        // Set an OnClickListener for your button if needed
        holder.addCalendarButton.setOnClickListener {
            // Handle button click here
            sendRequest(holder, currentItem, context)
        }
    }

    private fun sendRequest(holder: ViewHolder, currentItem: CareTaking, context: Context) {
          holder.addCalendarButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, currentItem.jobTitle)
                .putExtra(CalendarContract.Events.DESCRIPTION, currentItem.jobDetails)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, currentItem.place)
                .putExtra(CalendarContract.Events.EVENT_TIMEZONE, "GMT+08:00")

            // Parse the date and time string
              val originalFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)

              var date = originalFormat.parse(currentItem.jobTime.dropLast(3))


              Log.e("button", date.toString())
              if (date != null) {
                  val startTime = Calendar.getInstance()
                  startTime.time = date
                  startTime.timeZone = TimeZone.getTimeZone("GMT+8")
                  intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime.timeInMillis)
              context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}