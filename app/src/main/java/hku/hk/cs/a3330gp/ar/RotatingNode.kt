package hku.hk.cs.a3330gp.ar

import android.animation.ObjectAnimator
import android.view.animation.LinearInterpolator
import io.github.sceneview.collision.Quaternion
import io.github.sceneview.collision.QuaternionEvaluator
import io.github.sceneview.collision.Vector3
import io.github.sceneview.math.Position
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.ModelNode

class RotatingNode(
    private val degreesPerSecond: Float, modelInstance: ModelInstance, scaleToUnits: Float?, centerOrigin: Position
) : ModelNode(modelInstance, scaleToUnits = scaleToUnits, centerOrigin = centerOrigin) {

    private var objectAnimator: ObjectAnimator? = null

    fun startAnimation() {
        objectAnimator = objectAnimator ?: createObjectAnimator().apply {
            target = this@RotatingNode
            duration = (1000 * 360 / degreesPerSecond).toLong()
            start()
        }
    }

    private fun stopAnimation() {
        objectAnimator?.cancel()
        objectAnimator = null
    }

    private fun createObjectAnimator(): ObjectAnimator {
        val animationValues = arrayOf(
            Quaternion.axisAngle(Vector3.up(), 0f),
            Quaternion.axisAngle(Vector3.up(), 180f),
            Quaternion.axisAngle(Vector3.up(), 360f)
        )
        return ObjectAnimator().apply {
            setObjectValues(*animationValues)
            setPropertyName("localRotation")
            setEvaluator(QuaternionEvaluator())
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART
            interpolator = LinearInterpolator()
        }
    }
}