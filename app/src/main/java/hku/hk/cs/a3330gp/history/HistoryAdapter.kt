package hku.hk.cs.a3330gp.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import hku.hk.cs.a3330gp.R
import hku.hk.cs.a3330gp.data.PhotoRecord

class HistoryAdapter(
    val records: List<PhotoRecord>
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    var selectedRecord = MutableLiveData<PhotoRecord>()
    private var selectedRecordIndex = 0

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivHistory: ImageView = itemView.findViewById(R.id.ivHistory)
        val tvHistory: TextView = itemView.findViewById(R.id.tvHistory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun getItemCount() = records.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.itemView.apply {
            holder.ivHistory.setImageBitmap(records[position].imageData)
            holder.tvHistory.text = records[position].dateTaken

            setOnClickListener {
                selectRecord(holder)
            }
        }
    }

    private fun selectRecord(holder: HistoryViewHolder) {
        if(selectedRecordIndex != holder.layoutPosition) {
            notifyItemChanged(selectedRecordIndex)
            selectedRecordIndex = holder.adapterPosition
            selectedRecord.value = records[holder.layoutPosition]
        }
    }
}