package com.robin.firstopenglprogject.objects

import android.opengl.Matrix
import com.robin.firstopenglprogject.util.Geometry
import java.util.*

/**
 * Created by Robin Yeung on 2019-05-14.
 */
class ParticleShooter(
    private val position: Geometry.Point, private val direction: Geometry.Vector, private val color: Int,
    private val angleVariance: Float, private val speedVariance: Float
) {

    private val random = Random()

    private val rotationMatrix = FloatArray(16)

    private val directionVector = FloatArray(16)

    private val resultVector = FloatArray(16)

    init {
        directionVector[0] = direction.x
        directionVector[1] = direction.y
        directionVector[2] = direction.z
    }

    fun addParticles(particleSystem: ParticleSystem, currentTime: Float, count: Int) {

        for (i in 0 until count) {
            Matrix.setRotateEulerM(
                rotationMatrix, 0,
                (random.nextFloat() - 0.5f) * angleVariance,
                (random.nextFloat() - 0.5f) * angleVariance,
                (random.nextFloat() - 0.5f) * angleVariance
            )

            Matrix.multiplyMV(resultVector, 0, rotationMatrix, 0, directionVector, 0)

            val speedAdjustment = 1f + random.nextFloat() * speedVariance

            val thisDirection = Geometry.Vector(
                resultVector[0] * speedAdjustment,
                resultVector[1] * speedAdjustment,
                resultVector[2] * speedAdjustment
            )

            particleSystem.addParticle(position, color, thisDirection, currentTime)
        }
    }
}