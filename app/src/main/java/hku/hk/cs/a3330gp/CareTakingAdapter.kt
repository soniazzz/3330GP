package hku.hk.cs.a3330gp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import hku.hk.cs.a3330gp.data.CareTaking
import org.json.JSONArray
import org.json.JSONObject

class CareTakingAdapter(private val list: List<CareTaking>, private val userId: String,  private val context: Context) : RecyclerView.Adapter<CareTakingAdapter.ViewHolder>() {

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
        holder.acceptButton.text = if (currentItem.userId == "null") "Accept" else "Cancel"
        // Set an OnClickListener for your button if needed
        holder.acceptButton.setOnClickListener {
            // Handle button click here
            sendRequest(holder, currentItem)
        }
    }

    private fun sendRequest(holder: ViewHolder, currentItem: CareTaking) {
//        val url = "${getString(R.string.server_ip)}/jobs"
//        val jsonArrayRequest = JsonArrayRequest(
//            Request.Method.POST, url, null,
//            Response.Listener<JSONArray> { response ->
//                holder.acceptButton.text =
//                    if (holder.acceptButton.text == "Accept") "Cancel" else "Accept"
//            },
//            Response.ErrorListener { error ->
//                Log.e("CareTakingAdapter", error.toString())
//            }
//        )
//        Volley.newRequestQueue(context).add(jsonArrayRequest)
        val url = "${context.getString(R.string.server_ip)}/jobs"
        val queue = Volley.newRequestQueue(context)

        val request = object : StringRequest(
            Method.POST,
            url,
            Response.Listener<String> { response ->
                // handle your response
                holder.acceptButton.text = if (holder.acceptButton.text == "Accept") "Cancel" else "Accept"

            },
            Response.ErrorListener { error ->
                error.printStackTrace()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["userId"] = userId
                params["careTakingId"] = currentItem.id
                return params
            }
        }

        queue.add(request)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}