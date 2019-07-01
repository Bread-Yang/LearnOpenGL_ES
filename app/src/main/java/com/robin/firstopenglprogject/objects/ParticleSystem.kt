package com.robin.firstopenglprogject.objects

import android.graphics.Color
import android.opengl.GLES20.GL_POINTS
import android.opengl.GLES20.glDrawArrays
import com.robin.firstopenglprogject.Constants.BYTES_PER_FLOAT
import com.robin.firstopenglprogject.data.VertexArray
import com.robin.firstopenglprogject.programs.ParticleShaderProgram
import com.robin.firstopenglprogject.util.Geometry

/**
 * Created by Robin Yeung on 2019-05-13.
 */
class ParticleSystem(private val maxParticleCount: Int) {

    companion object {
        private const val POSITION_COMPONENT_COUNT = 3
        private const val COLOR_COMPONENT_COUNT = 3
        private const val VECTOR_COMPONENT_COUNT = 3
        private const val PARTICLE_START_TIME_COMPONENT_COUNT = 1

        private const val TOTAL_COMPONENT_COUNT =
            POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT + VECTOR_COMPONENT_COUNT + PARTICLE_START_TIME_COMPONENT_COUNT

        private const val STRIDE = TOTAL_COMPONENT_COUNT * BYTES_PER_FLOAT
    }

    private val particles: FloatArray
    private val vertexArray: VertexArray

    private var currentParticleCount: Int = 0
    private var nextParticle: Int = 0

    init {
        particles = FloatArray(maxParticleCount * TOTAL_COMPONENT_COUNT)
        vertexArray = VertexArray(particles)
    }

    fun addParticle(position: Geometry.Point, color: Int, direction: Geometry.Vector, particleStartTime: Float) {
        val particleOffset = nextParticle * TOTAL_COMPONENT_COUNT

        var currentOffset = particleOffset
        nextParticle++

        if (currentParticleCount < maxParticleCount) {
            currentParticleCount++
        }

        if (nextParticle == maxParticleCount) {
            // Start over at the beginning, but keep currentParticleCount so that all the other particles still get drawn.
            nextParticle = 0
        }

        particles[currentOffset++] = position.x
        particles[currentOffset++] = position.y
        particles[currentOffset++] = position.z

        // Android’s Color class returns components in a range from 0 to 255, while OpenGL expects the color to be
        // from 0 to 1, so we convert from Android to OpenGL by dividing each component by 255
        particles[currentOffset++] = Color.red(color) / 255f
        particles[currentOffset++] = Color.green(color) / 255f
        particles[currentOffset++] = Color.blue(color) / 255f

        particles[currentOffset++] = direction.x
        particles[currentOffset++] = direction.y
        particles[currentOffset++] = direction.z

        particles[currentOffset++] = particleStartTime

        vertexArray.updateBuffer(particles, particleOffset, TOTAL_COMPONENT_COUNT)
    }

    fun bindData(particleProgram: ParticleShaderProgram) {
        var dataOffset = 0

        vertexArray.setVertexAttribPointer(
            dataOffset, particleProgram.getPositionAttributeLocation(),
            POSITION_COMPONENT_COUNT, STRIDE
        )
        dataOffset += POSITION_COMPONENT_COUNT

        vertexArray.setVertexAttribPointer(
            dataOffset, particleProgram.getColorAttributeLocation(),
            COLOR_COMPONENT_COUNT, STRIDE
        )
        dataOffset += COLOR_COMPONENT_COUNT

        vertexArray.setVertexAttribPointer(
            dataOffset, particleProgram.getDirectionVectorAttributeLocation(),
            VECTOR_COMPONENT_COUNT, STRIDE
        )
        dataOffset += VECTOR_COMPONENT_COUNT

        vertexArray.setVertexAttribPointer(
            dataOffset, particleProgram.getParticleStartTimeAttributeLocation(),
            PARTICLE_START_TIME_COMPONENT_COUNT, STRIDE
        )
    }

    fun draw() {
        glDrawArrays(GL_POINTS, 0, currentParticleCount)
    }
}