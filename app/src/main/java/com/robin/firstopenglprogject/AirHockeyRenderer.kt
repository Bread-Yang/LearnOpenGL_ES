package com.robin.firstopenglprogject

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
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

    private val POSITION_COMPONENT_COUNT = 2
    private val BYTES_PER_FLOAT: Int = 4
    private val vertexDate: FloatBuffer

    private var program: Int = 0

    // Vertices shader variables
    private val A_POSITION = "a_Position"
    private var aPositionLocation: Int = 0
    private val simpleVertexShaderGLSL = """
        attribute vec4 $A_POSITION;

        void main()
        {
            gl_Position = $A_POSITION;
            gl_PointSize = 10.0;
        }
    """

    // Fragment shader variables
    private val U_COLOR = "u_Color"
    private var uColorLocation: Int = 0
    private val simpleFragmentShaderGLSL = """
        precision mediump float;

        uniform vec4 $U_COLOR;

        void main()
        {
            gl_FragColor = $U_COLOR;
        }
    """

    private val tableVerticesWithTriangles = floatArrayOf(
        // Triangle 1
        -0.5f, -0.5f,
        0.5f, 0.5f,
        -0.5f, 0.5f,

        // Triangle 2
        -0.5f, -0.5f,
        0.5f, -0.5f,
        0.5f, 0.5f,

        // Line 1
        -0.5f, 0f,
        0.5f, 0f,

        // Mallets
        0f, -0.25f,
        0f, 0.25f,

        // Puck
        0f, 0f
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

        uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR)
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION)

        // set the position to the beginning of $vertexData
        vertexDate.position(0)
        // tell OpenGL that if can find the data for a_Position in the buffer vertexData
        GLES20.glVertexAttribPointer(
            aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
            false, 0, vertexDate
        )

        // with this final call, OpenGL now konws where th find all the data it needs.
        GLES20.glEnableVertexAttribArray(aPositionLocation)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        // Set the OpenGL ViewPort to fill the entire surface.
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        // Clear the rendering surface.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        // update the value of u_Color in fragment shader code by calling glUniform4f().
        GLES20.glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f)

        // First argument tells OpenGL we want to draw triangle. The second argument tells OpenGL to read in
        // vertices starting at the beginning of our vertex array, and the third argument tells OpenGL to read
        // in six vertices.Since there are three vertices per triangle, this call will end up drawing two triangles.
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6)

        // Draw the center dividing line
        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f)
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2)

        // Draw the first mallet blue
        GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f)
        GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1)

        // Draw the second mallet red.
        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f)
        GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1)

        // Draw the puck
        GLES20.glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f)
        GLES20.glDrawArrays(GLES20.GL_POINTS, 10, 1)
    }
}