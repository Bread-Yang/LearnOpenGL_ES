package com.robin.firstopenglprogject.objects

import android.opengl.GLES20
import com.robin.firstopenglprogject.Constants
import com.robin.firstopenglprogject.data.VertexArray
import com.robin.firstopenglprogject.programs.TextureShaderProgram

/**
 * Created by Robin Yeung on 3/25/19.
 */
class Table {

    private companion object {
        const val POSITION_COMPONENT_COUNT = 2
        const val TEXTURE_COORDINATES_COMPONENT_COUNT = 2
        const val STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT
    }

    private val VERTEX_DATA = floatArrayOf(
        // Order of coordinates: X, Y, S, T

        // Triangle Fan
        0f, 0f, 0.5f, 0.5f,
        -0.5f, -0.8f, 0f, 0.9f,
        0.5f, -0.8f, 1f, 0.9f,
        0.5f, 0.8f, 1f, 0.1f,
        -0.5f, 0.8f, 0f, 0.1f,
        -0.5f, -0.8f, 0f, 0.9f
    )

    private val vertexArray: VertexArray

    constructor() {
        vertexArray = VertexArray(VERTEX_DATA)
    }

    fun bindData(textureProgram: TextureShaderProgram) {
        vertexArray.setVertexAttribPointer(
            0, textureProgram.getPositionAttributeLocation(),
            POSITION_COMPONENT_COUNT, STRIDE
        )

        vertexArray.setVertexAttribPointer(
            POSITION_COMPONENT_COUNT, textureProgram.getTextureCoordinatesAttributeLocation(),
            TEXTURE_COORDINATES_COMPONENT_COUNT, STRIDE
        )
    }

    fun draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6)
    }
}