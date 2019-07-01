package com.robin.firstopenglprogject.programs

import android.content.Context
import android.opengl.GLES20
import com.robin.firstopenglprogject.util.ShaderHelper
import com.robin.firstopenglprogject.util.TextResourceReader

/**
 * Created by Robin Yeung on 3/25/19.
 */
open class ShaderProgram {

    protected companion object {
        // Uniform constants
        const val U_MATRIX = "u_Matrix"
        const val U_COLOR = "u_Color"
        const val U_TEXTURE_UNIT = "u_TextureUnit"
        const val U_TIME = "u_Time"

        // Attribute constants
        const val A_POSITION = "a_Position"
        const val A_COLOR = "a_Color"
        const val A_TEXTURE_COORDINATES = "a_TextureCoordinates"

        const val A_DIRECTION_VECTOR = "a_DirectionVector"
        const val A_PARTICLE_START_TIME = "a_ParticleStartTime"
    }

    // Shader program
    protected val program: Int

    constructor(context: Context, vertextShaderResourceId: Int, fragmentShaderResourceId: Int) {
        val vertexShaderGLSL = TextResourceReader.readTextFromResource(context, vertextShaderResourceId)
        val fragmentShaderGLSL = TextResourceReader.readTextFromResource(context, fragmentShaderResourceId)

        // Compile the shaders and link the program
        program = ShaderHelper.buildProgram(vertexShaderGLSL, fragmentShaderGLSL)
    }

    fun useProgram() {
        // Set the current OpenGL shader program to this program.
        GLES20.glUseProgram(program)
    }
}