package com.robin.firstopenglprogject

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.robin.firstopenglprogject.util.ShaderHelper
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Created by Robin Yeung on 3/15/19.
 */
class AirHockeyRenderer(val context: Context) : GLSurfaceView.Renderer {

    private val BYTES_PER_FLOAT: Int = 4

    private val POSITION_COMPONENT_COUNT = 2
    private val COLOR_COMPONENT_COUNT = 3
    private val STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT

    private val projectionMatrix = FloatArray(16)
    private val U_MATRIX = "u_Matrix"
    private var uMatrixLocation: Int = 0

    private val vertexDate: FloatBuffer

    private var program: Int = 0

    // Vertices shader variables
    private val A_POSITION = "a_Position"
    private val A_COLOR = "a_Color"
    private var aPositionLocation: Int = 0
    private val simpleVertexShaderGLSL = """
        uniform mat4 $U_MATRIX;

        attribute vec4 $A_POSITION;
        attribute vec4 $A_COLOR;

        varying vec4 v_Color;

        void main()
        {
            v_Color = $A_COLOR;

            gl_Position = $U_MATRIX * $A_POSITION;
            gl_PointSize = 10.0;
        }
    """

    // Fragment shader variables
    private val V_COLOR = "v_Color"
    private var aColorLocation: Int = 0
    private val simpleFragmentShaderGLSL = """
        precision mediump float;

        varying vec4 $V_COLOR;

        void main()
        {
            gl_FragColor = $V_COLOR;
        }
    """

    private val tableVerticesWithTriangles = floatArrayOf(
        // Order of coordinates: X, Y, R, G, B

        // Triangle Fan
        0f,    0f,   1f,   1f,   1f,
        -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
        0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
        0.5f,  0.8f, 0.7f, 0.7f, 0.7f,
        -0.5f,  0.8f, 0.7f, 0.7f, 0.7f,
        -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,

        // Line 1
        -0.5f, 0f, 1f, 0f, 0f,
        0.5f, 0f, 1f, 0f, 0f,

        // Mallets
        0f, -0.4f, 0f, 0f, 1f,
        0f,  0.4f, 1f, 0f, 0f
    )

    init {
        vertexDate = ByteBuffer
            .allocateDirect(tableVerticesWithTriangles.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()

        vertexDate.put(tableVerticesWithTriangles)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        val vertexShader = ShaderHelper.compileVertexShader(simpleVertexShaderGLSL)
        val fragmentShader = ShaderHelper.compileFragmentShader(simpleFragmentShaderGLSL)

        program = ShaderHelper.linkProgram(vertexShader, fragmentShader)
        ShaderHelper.validateProgram(program)

        GLES20.glUseProgram(program)

        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX)

        aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR)
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION)

        // set the position to the beginning of $vertexData
        vertexDate.position(0)
        // tell OpenGL that if can find the data for a_Position in the buffer vertexData
        GLES20.glVertexAttribPointer(
            aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
            false, STRIDE, vertexDate
        )
        // with this final call, OpenGL now knows where th find all the data it needs.
        GLES20.glEnableVertexAttribArray(aPositionLocation)

        vertexDate.position(POSITION_COMPONENT_COUNT)
        GLES20.glVertexAttribPointer(
            aColorLocation, COLOR_COMPONENT_COUNT, GLES20.GL_FLOAT,
            false, STRIDE, vertexDate
        )
        GLES20.glEnableVertexAttribArray(aColorLocation)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        // Set the OpenGL ViewPort to fill the entire surface.
        GLES20.glViewport(0, 0, width, height)

        val aspectRatio =
            if (width > height) {
                width.toFloat() / height.toFloat()
            } else {
                height.toFloat() / width.toFloat()
            }

        if (width > height) {
            // Landscape
            Matrix.orthoM(
                projectionMatrix, 0, -aspectRatio, aspectRatio,
                -1f, 1f, -1f, 1f
            )
        } else {
            // Portrait or square
            Matrix.orthoM(
                projectionMatrix, 0, -1f, 1f,
                -aspectRatio, aspectRatio, -1f, 1f
            )
        }
    }

    override fun onDrawFrame(gl: GL10?) {
        // Clear the rendering surface.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        // send the orthographic projection matrix to the shader
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0)

        // First argument tells OpenGL we want to draw triangle. The second argument tells OpenGL to read in
        // vertices starting at the beginning of our vertex array, and the third argument tells OpenGL to read
        // in six vertices.Since there are three vertices per triangle, this call will end up drawing two triangles.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6)

        // Draw the center dividing line
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2)

        // Draw the first mallet blue
        GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1)

        // Draw the second mallet red.
        GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1)
    }
}