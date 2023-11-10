package hku.hk.cs.a3330gp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar

class TopAppBarFragment : Fragment(R.layout.fragment_top_app_bar) {
    private lateinit var topAppBar:MaterialToolbar
    private var topAppBarListener: TopAppBarListener? = null

    interface TopAppBarListener {
        fun onNavigationIconClick()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        topAppBar = view.findViewById(R.id.topAppBar)

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profile -> {
                    Toast.makeText(requireContext(), "Profile clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        topAppBar.setNavigationOnClickListener {
            topAppBarListener?.onNavigationIconClick()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is TopAppBarListener) {
            topAppBarListener = context
        } else {
            throw RuntimeException("$context must implement TopAppBarListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        topAppBarListener = null
    }
}