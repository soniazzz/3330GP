package hku.hk.cs.a3330gp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.view.View
import android.widget.TextView
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import hku.hk.cs.a3330gp.data.Video

class CareTakingVideosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.care_taking_videos)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val videos = listOf(
            Video("Caring for the Elderly from WTO", R.raw.video1),
            Video("Elderly Carer Survival Guide", R.raw.video2),
            Video("Bathing&Dressing Tutorial", R.raw.video3)

            // Add more videos here
        )

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = VideoAdapter(videos) { resourceId ->
            // Handle play button click
            val dialog = VideoPlayerDialogFragment.newInstance(resourceId)
            dialog.show(supportFragmentManager, "VideoPlayer")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}

class VideoAdapter(private val videos: List<Video>, private val onPlayClicked: (Int) -> Unit) :
    RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    class VideoViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val videoName: TextView = view.findViewById(R.id.video_name)
        val playButton: Button = view.findViewById(R.id.play_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = videos[position]
        holder.videoName.text = video.name
        holder.playButton.setOnClickListener { onPlayClicked(video.resourceId) }
    }

    override fun getItemCount() = videos.size
}