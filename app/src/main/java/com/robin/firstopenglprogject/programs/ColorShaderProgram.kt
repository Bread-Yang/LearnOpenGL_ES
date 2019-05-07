package com.robin.firstopenglprogject.programs

import android.content.Context
import android.opengl.GLES20.*
import com.robin.firstopenglprogject.R

/**
 * Created by Robin Yeung on 3/25/19.
 */
class ColorShaderProgram : ShaderProgram {

    // Uniform locations
    private val uMatrixLocation: Int
    private val uColorLocation: Int

    // Attribute locations
    private val aPositionLocation: Int

    constructor(context: Context) : super(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader) {
        // Retrieve uniform locations for the shader program.
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX)
        uColorLocation = glGetUniformLocation(program, U_COLOR)

        // Retrieve attribute locations for the shader program.
        aPositionLocation = glGetAttribLocation(program, A_POSITION)
    }

    fun setUniforms(matrix: FloatArray, r: Float, g: Float, b: Float) {
        // Pass the matrix into the shader program.
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
        glUniform4f(uColorLocation, r, g, b, 1f)
    }

    fun getPositionAttributeLocation(): Int {
        return aPositionLocation
    }
}