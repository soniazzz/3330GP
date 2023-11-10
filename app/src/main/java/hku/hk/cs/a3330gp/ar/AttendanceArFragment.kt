package hku.hk.cs.a3330gp.ar

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import hku.hk.cs.a3330gp.R
import hku.hk.cs.a3330gp.util.Constants
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.arcore.addAugmentedImage
import io.github.sceneview.ar.arcore.getUpdatedAugmentedImages
import io.github.sceneview.ar.node.AugmentedImageNode
import io.github.sceneview.math.Position
import io.github.sceneview.node.ModelNode

class AttendanceArFragment : Fragment(R.layout.fragment_attendance_ar) {

    private lateinit var sceneView:ARSceneView

    private val augmentedImageNodes = mutableListOf<AugmentedImageNode>()

    private val userList = Constants.getUsers()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sceneView = view.findViewById<ARSceneView>(R.id.arSceneView).apply {
            configureSession { session, config ->
                for(user in userList) {
                    config.addAugmentedImage(
                        session, "${user.name} ${user.id}", // "Gordan Chan 424"
                        requireContext().assets.open(user.augmentedImg)
                            .use(BitmapFactory::decodeStream)
                    )
                }
            }
            onSessionUpdated = { session, frame ->
                Log.d("AI Node", "size is ${augmentedImageNodes.size}")
                frame.getUpdatedAugmentedImages().forEach { augmentedImage ->
                    if (augmentedImageNodes.none { it.imageName == augmentedImage.name }) {
                        val augmentedImageNode = AugmentedImageNode(engine, augmentedImage).apply {
                            when (augmentedImage.name) {
                                "Gordan Chan 424" -> addChildNode(ModelNode(
                                        modelInstance = modelLoader.createModelInstance("models/camera.glb"),
                                        scaleToUnits = 0.2f,
                                        centerOrigin = Position(z=5f)
                                    ).apply {
                                        isEditable = true
                                        isScaleEditable = true
                                        val camPos = sceneView.cameraNode.worldPosition
                                        val mNodePos = this.worldPosition
                                        val dir = camPos - mNodePos
                                        this.worldRotation = dir
                                    })





                                "Mary Ng 425" -> addChildNode(
                                    ModelNode(
                                        modelInstance = modelLoader.createModelInstance(
                                            assetFileLocation = "models/camera.glb"
                                        ),
                                        scaleToUnits = 0.2f,
                                        centerOrigin = Position(z=5f)
                                    ).apply {
                                        isEditable = true
                                        isScaleEditable = true
                                        val camPos = sceneView.cameraNode.worldPosition
                                        val mNodePos = this.worldPosition
                                        val dir = camPos - mNodePos
                                        this.worldRotation = dir
                                    }
                                )
                            }
                        }
                        addChildNode(augmentedImageNode)
                        augmentedImageNodes += augmentedImageNode
                    }
                }
            }
        }
    }
}