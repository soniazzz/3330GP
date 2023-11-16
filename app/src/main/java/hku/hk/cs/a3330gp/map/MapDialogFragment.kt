package hku.hk.cs.a3330gp.map

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import hku.hk.cs.a3330gp.R
import hku.hk.cs.a3330gp.util.Constants

class MapDialogFragment : DialogFragment() {
    private lateinit var listener: MapDialogListener
    interface MapDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNeutralClick(dialog: DialogFragment)
        fun onChooseItem(dialog: DialogFragment, choice:Int)
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = MaterialAlertDialogBuilder(it)
            // Get the layout inflater.
            val inflater = requireActivity().layoutInflater;

            builder.setTitle(resources.getString(R.string.mapDialogTitle))
                .setNeutralButton(resources.getString(R.string.mapDialogCancel)) { dialog, which ->
                    listener.onDialogNeutralClick(this)
                }
                .setPositiveButton(resources.getString(R.string.mapDialogSelect)
                ) { dialog, id ->
                    listener.onDialogPositiveClick(this)
                }
                .setSingleChoiceItems(
                    Constants.getUsers().map { it.name }.toTypedArray() , 0
                ) { dialog, which ->
                    listener.onChooseItem(this, which)
                }
            // Create the AlertDialog object and return it.
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface.
        try {
            listener = context as MapDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException((context.toString() +
                    " must implement MapDialogListener"))
        }
    }
}