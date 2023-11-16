package hku.hk.cs.a3330gp.ar

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.ar.core.Anchor
import com.google.ar.core.Config
import com.google.ar.core.Plane
import hku.hk.cs.a3330gp.R
import hku.hk.cs.a3330gp.util.Constants
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.ar.node.AugmentedImageNode
import io.github.sceneview.math.Position
import io.github.sceneview.node.ModelNode
import kotlinx.coroutines.launch

class AttendanceArFragment : Fragment(R.layout.fragment_attendance_ar) {

    private lateinit var sceneView:ARSceneView
    private lateinit var loadingView:FrameLayout

    private val augmentedImageNodes = mutableListOf<AugmentedImageNode>()
    private var anchorNode: AnchorNode? = null
        set(value) {
            if (field != value) {
                field = value
            }
        }

    private var modelNode: ModelNode? = null

    private val userList = Constants.getUsers()

    private var isLoading = false
        set(value) {
            field = value
            loadingView.isGone = !value
        }

    private var arListener: ARListener? = null
    interface ARListener {
        fun onModelClick()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingView = view.findViewById(R.id.loadingView)
        loadingView.bringToFront()
        isLoading = true

        sceneView = view.findViewById<ARSceneView>(R.id.arSceneView).apply {
            planeRenderer.isEnabled = true
            configureSession { session, config ->
                config.depthMode = when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                    true -> Config.DepthMode.AUTOMATIC
                    else -> Config.DepthMode.DISABLED
                }
                config.instantPlacementMode = Config.InstantPlacementMode.DISABLED
                config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
            }
            onSessionUpdated = { _, frame ->
                if (anchorNode == null) {
                    frame.getUpdatedPlanes()
                        .firstOrNull { it.type == Plane.Type.HORIZONTAL_UPWARD_FACING }
                        ?.let { plane ->
                            addAnchorNode(plane.createAnchor(plane.centerPose))
                        }
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ARListener) {
            arListener = context
        } else {
            throw RuntimeException("$context must implement ARListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        arListener = null
    }

    private fun addAnchorNode(anchor: Anchor) {

        sceneView.addChildNode(
            AnchorNode(sceneView.engine, anchor)
                .apply {
                    isEditable = true
                    lifecycleScope.launch {
                        sceneView.modelLoader.createModelInstance(
                            "models/Error.glb"
                        ).let { modelInstance ->
                            modelNode = RotatingNode(
                                degreesPerSecond = 20f,
                                modelInstance = modelInstance,
                                // Scale to fit in a 0.5 meters cube
                                scaleToUnits = 0.5f,
                                // Bottom origin instead of center so the model base is on floor
                                centerOrigin = Position(y = -0.5f)
                            ).apply {
                                isEditable = true
                                isScaleEditable = false
                                startAnimation()
                            }
                            addChildNode(modelNode!!)
                            modelNode?.onSingleTapConfirmed = {
                                arListener?.onModelClick()
                                true
                            }
                        }
                        isLoading = false
                    }
                    anchorNode = this
                }
        )
    }
}