package com.robin.firstopenglprogject.programs

import android.content.Context
import android.opengl.GLES20.*
import com.robin.firstopenglprogject.R


/**
 * Created by Robin Yeung on 2019-05-13.
 */
class ParticleShaderProgram(val context: Context) :
    ShaderProgram(context, R.raw.particle_vertex_shader, R.raw.particle_fragment_shader) {

    // Uniform locations
    private var uMatrixLocation: Int
    private var uTimeLocation: Int
    private var uTextureUnitLocation: Int

    // Attribute locations
    private var aPositionLocation: Int
    private var aColorLocation: Int
    private var aDirectionVectorLocation: Int
    private var aParticleStartTimeLocation: Int

    init {
        // Retrieve uniform locations for the shader program.
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX)
        uTimeLocation = glGetUniformLocation(program, U_TIME)
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT)

        // Retrieve attribute locations for the shader program.
        aPositionLocation = glGetAttribLocation(program, A_POSITION)
        aColorLocation = glGetAttribLocation(program, A_COLOR)
        aDirectionVectorLocation = glGetAttribLocation(program, A_DIRECTION_VECTOR)
        aParticleStartTimeLocation = glGetAttribLocation(program, A_PARTICLE_START_TIME)
    }

    fun setUniforms(matrix: FloatArray, elapsedTime: Float, textureId: Int) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
        glUniform1f(uTimeLocation, elapsedTime)
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, textureId)
        glUniform1i(uTextureUnitLocation, 0)
    }

    fun getPositionAttributeLocation(): Int {
        return aPositionLocation
    }

    fun getColorAttributeLocation(): Int {
        return aColorLocation
    }

    fun getDirectionVectorAttributeLocation(): Int {
        return aDirectionVectorLocation
    }

    fun getParticleStartTimeAttributeLocation(): Int {
        return aParticleStartTimeLocation
    }
}