package hku.hk.cs.a3330gp

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.fragment.app.DialogFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [VideoPlayerDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VideoPlayerDialogFragment : DialogFragment() {

    private lateinit var videoView: VideoView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_video_player, container, false)
        videoView = view.findViewById(R.id.video_view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val videoUri = Uri.parse("android.resource://${requireContext().packageName}/${arguments?.getInt("video_resource_id")}")
        videoView.setVideoURI(videoUri)
        videoView.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        videoView.stopPlayback()
    }

    companion object {
        fun newInstance(videoResourceId: Int): VideoPlayerDialogFragment {
            val fragment = VideoPlayerDialogFragment()
            val args = Bundle()
            args.putInt("video_resource_id", videoResourceId)
            fragment.arguments = args
            return fragment
        }
    }
}