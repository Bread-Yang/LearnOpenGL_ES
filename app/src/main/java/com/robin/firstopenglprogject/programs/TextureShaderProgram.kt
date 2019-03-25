package com.robin.firstopenglprogject.programs

import android.content.Context
import android.opengl.GLES20.*
import com.robin.firstopenglprogject.R

/**
 * Created by Robin Yeung on 3/25/19.
 */
class TextureShaderProgram : ShaderProgram {

    // Uniform locations
    private val uMatrixLocation: Int
    private val uTextureUnitLocation: Int

    // Attribute locations
    private val aPositionLocation: Int
    private val aTextureCoordinatesLocation: Int

    constructor(context: Context) : super(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader) {

        // Retrieve uniform locations for the shader program.
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX)
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT)

        // Retrieve attribute locations for the shader program.
        aPositionLocation = glGetAttribLocation(program, A_POSITION)
        aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES)
    }

    fun setUniforms(matrix: FloatArray, textureId: Int) {
        // Pass the matrix into the shader program.
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)

        // Set the active texture unit to texture unit 0.
        glActiveTexture(GL_TEXTURE0)

        // When we draw using textures in OpenGL, we don’t pass the texture directly in to the shader. Instead, we use a texture unit to hold the texture.
        // Bind the texture to this unit.
        glBindTexture(GL_TEXTURE_2D, textureId)

        // We then pass in the selected texture unit to u_TextureUnit in the fragment shader by calling glUniform1i(uTextureUnitLocation, 0)
        // Tell the texture uniform sampler to use this texture in the shader by telling it to read from texture unit 0
        glUniform1i(uTextureUnitLocation, 0)
    }

    fun getPositionAttributeLocation(): Int {
        return aPositionLocation
    }

    fun getTextureCoordinatesAttributeLocation(): Int {
        return aTextureCoordinatesLocation
    }
}