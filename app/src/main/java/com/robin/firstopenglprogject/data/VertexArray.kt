package com.robin.firstopenglprogject.data

import android.opengl.GLES20
import com.robin.firstopenglprogject.Constants.BYTES_PER_FLOAT
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * Created by Robin Yeung on 3/25/19.
 */
class VertexArray {

    private val floatBuffer: FloatBuffer

    constructor(vertextData: FloatArray) {
        floatBuffer = ByteBuffer
            .allocateDirect(vertextData.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertextData)
    }

    fun setVertexAttribPointer(dataOffset: Int, attributeLocation: Int, componentCount: Int, stride: Int) {
        floatBuffer.position(dataOffset)
        GLES20.glVertexAttribPointer(attributeLocation, componentCount, GLES20.GL_FLOAT, false, stride, floatBuffer)
        GLES20.glEnableVertexAttribArray(attributeLocation)

        floatBuffer.position(0)
    }
}