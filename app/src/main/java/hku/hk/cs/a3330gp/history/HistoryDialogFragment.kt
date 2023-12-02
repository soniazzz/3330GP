package hku.hk.cs.a3330gp.history

import android.app.Dialog
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import hku.hk.cs.a3330gp.R

class HistoryDialogFragment: DialogFragment() {
    companion object {
        fun newInstance(date: String, time: String, imageData:ByteArray): HistoryDialogFragment {
            val fragment = HistoryDialogFragment()
            val args = Bundle()
            args.putString("DATE", date)
            args.putString("TIME", time)
            args.putByteArray("IMAGE_DATA", imageData)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var listener: HistoryDialogListener
    interface HistoryDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = MaterialAlertDialogBuilder(it)
            // Get the layout inflater.
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_history, null)
            view.findViewById<TextView>(R.id.tvDate).text = "Date: ${arguments?.getString("DATE")}"
            view.findViewById<TextView>(R.id.tvTime).text = "Time: ${arguments?.getString("TIME")}"
            val imageView = view.findViewById<ImageView>(R.id.ivRecord)

            // Get the image data from arguments
            val imageData = arguments?.getByteArray("IMAGE_DATA")
            val imageBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData?.size ?: 0)

            // Set the image in the ImageView
            imageView.setImageBitmap(imageBitmap)

            builder.setView(view)
                .setTitle(resources.getString(R.string.dialog_history_saved))
                .setPositiveButton("OK") { dialog, id ->
                    listener.onDialogPositiveClick(this)
                }
            // Create the AlertDialog object and return it.
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface.
        try {
            listener = context as HistoryDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() +
                    " must implement HistoryDialogListener"))
        }
    }
}