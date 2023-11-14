import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hku.hk.cs.a3330gp.R
import hku.hk.cs.a3330gp.data.CareTaking

class CareTakingAdapter(private val list: List<CareTaking>) : RecyclerView.Adapter<CareTakingAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val jobID: TextView = itemView.findViewById(R.id.jobID)
        val jobTitle: TextView = itemView.findViewById(R.id.jobTitle)
        val place: TextView = itemView.findViewById(R.id.place)
        val jobDetails: TextView = itemView.findViewById(R.id.jobDetails)
        val jobTime: TextView = itemView.findViewById(R.id.jobTime)
        val acceptButton: Button = itemView.findViewById(R.id.accept)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.caretaking_list_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]
        holder.jobID.text = currentItem.id
        holder.jobTitle.text = currentItem.jobTitle
        holder.place.text = currentItem.place
        holder.jobDetails.text = currentItem.jobDetails
        holder.jobTime.text = currentItem.jobTime
        // Set an OnClickListener for your button if needed
        holder.acceptButton.setOnClickListener {
            // Handle button click here

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}