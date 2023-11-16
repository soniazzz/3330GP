package hku.hk.cs.a3330gp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import hku.hk.cs.a3330gp.data.CareTaking
import java.text.SimpleDateFormat
import java.time.YearMonth


class CalendarActivity : AppCompatActivity() {
    private val tasks = mutableMapOf<String, List<String>>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)  // Adjust as needed
        val endMonth = currentMonth.plusMonths(100)  // Adjust as needed
        val firstDayOfWeek = firstDayOfWeekFromLocale() // Available from the library

        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        calendarView.setup(startMonth, endMonth, firstDayOfWeek)
        calendarView.scrollToMonth(currentMonth)
        calendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.textView.text = data.date.dayOfMonth.toString()
            }
        }

//        val calendarView: CalendarView = findViewById(R.id.calendarView)
//        val listView: ListView = findViewById(R.id.listView)
//        var userId = intent.extras?.getString("id").toString()
//        val careTakingList: ArrayList<CareTaking> = intent.getParcelableArrayListExtra<CareTaking>("data") ?: arrayListOf()
//
//        calendarView.dayBinder = object : DayBinder<DayViewContainer> {
//            override fun create(view: View) = DayViewContainer(view)
//
//            override fun bind(container: DayViewContainer, day: CalendarDay) {
//                container.textView.text = day.date.dayOfMonth.toString()
//                if (tasks.containsKey(day.date)) {
//                    container.textView.setBackgroundResource(R.drawable.circle)
//                } else {
//                    container.textView.background = null
//                }
//            }
//        }
    }
}

class DayViewContainer(view: View) : ViewContainer(view) {
    val textView = view.findViewById<TextView>(R.id.calendarDayText)

    // With ViewBinding
    // val textView = CalendarDayLayoutBinding.bind(view).calendarDayText
}



