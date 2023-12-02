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
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.ModelNode
import kotlinx.coroutines.launch

class AttendanceArFragment : Fragment(R.layout.fragment_attendance_ar) {

    private lateinit var sceneView:ARSceneView
    private lateinit var loadingView:FrameLayout

    private var anchorNode: AnchorNode? = null
        set(value) {
            if (field != value) {
                field = value
            }
        }

    private var modelNode: ModelNode? = null


    private var isLoading = false
        set(value) {
            field = value
            loadingView.isGone = !value
        }

    private var arInterface: ARInterface? = null
    interface ARInterface {
        fun onSceneViewInstanceReady(arSceneView: ARSceneView)
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
        arInterface?.onSceneViewInstanceReady(sceneView)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ARInterface) {
            arInterface = context
        } else {
            throw RuntimeException("$context must implement ARListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        arInterface = null
    }

    private fun addAnchorNode(anchor: Anchor) {

        sceneView.addChildNode(
            AnchorNode(sceneView.engine, anchor)
                .apply {
                    isEditable = true
                    lifecycleScope.launch {
                        sceneView.modelLoader.createModelInstance(
                            "models/shiba.glb"
                        ).let { modelInstance ->
                            modelNode = ModelNode(
                                modelInstance = modelInstance,
                                // Scale to fit in a 0.5 meters cube
                                scaleToUnits = 0.5f,
                                // Bottom origin instead of center so the model base is on floor
                                centerOrigin = Position(y = -0.5f)
                            ).apply {
                                isEditable = true
                                rotation = Rotation(x = 270.0f, y = 0.0f, z = 0.0f)
                            }
                            addChildNode(modelNode!!)
                            modelNode?.onSingleTapConfirmed = {
                                isLoading = true
                                arInterface?.onModelClick()
                                isLoading = false
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