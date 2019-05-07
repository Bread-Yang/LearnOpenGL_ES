package com.robin.firstopenglprogject

import android.content.Context
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.robin.firstopenglprogject.objects.Mallet
import com.robin.firstopenglprogject.objects.Puck
import com.robin.firstopenglprogject.objects.Table
import com.robin.firstopenglprogject.programs.ColorShaderProgram
import com.robin.firstopenglprogject.programs.TextureShaderProgram
import com.robin.firstopenglprogject.util.MatrixHelper
import com.robin.firstopenglprogject.util.TextureHelper
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Created by Robin Yeung on 3/15/19.
 */
class AirHockeyRenderer(val context: Context) : GLSurfaceView.Renderer {

    private val projectionMatrix = FloatArray(16)
    private val modelMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val viewProjectionMatrix = FloatArray(16)
    private val modelViewProjectionMatrix = FloatArray(16)

    private lateinit var table: Table
    private lateinit var mallet: Mallet
    private lateinit var puck: Puck

    private lateinit var textureProgram: TextureShaderProgram
    private lateinit var colorProgram: ColorShaderProgram

    private var texture: Int = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        table = Table()
        mallet = Mallet(0.08f, 0.15f, 32)
        puck = Puck(0.06f, 0.02f, 32)

        textureProgram = TextureShaderProgram(context)
        colorProgram = ColorShaderProgram(context)

        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        // Set the OpenGL ViewPort to fill the entire surface.
        glViewport(0, 0, width, height)

        MatrixHelper.perspectiveM(
            projectionMatrix, 45f,
            width.toFloat() / height.toFloat(), 1f, 10f
        )

        /**
         *  setLookAtM(float[] rm, int rmOffset, float eyeX, float eyeY, float eyeZ, float centerX, float centerY, float centerZ, float upX, float upY, float upZ)
         *
         *  float[] rm      :       This is the destination array. This array’s length should be at least sixteen elements so that it can store the view matrix.
         *  int rmOffset    :       setLookAtM() will begin writing the result at this offset into rm.
         *  float eyeX, eyeY, eyeZ      :       This is where the eye will be. Everything in the scene will appear as if we’re viewing it from this point.
         *  float centerX, centerY, centerZ     :       This is where the eye is looking; this position will appear in the center of the scene.
         *  float upX, upY, upZ         :       If we were talking about your eyes, then this is where your head would be pointing. An upY of 1 means your head would be pointing straight up.
         *
         */
        Matrix.setLookAtM(
            viewMatrix, 0,
            0f, 1.2f, 2.2f,
            0f, 0f, 0f,
            0f, 1f, 0f
        )
    }

    override fun onDrawFrame(gl: GL10?) {
        // Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT)

        // This call will cache the results of multiplying the projection and view matrices together into viewProjectionMatrix.
        Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        // Draw the table.
        positionTableInScene()
        textureProgram.useProgram()
        textureProgram.setUniforms(modelViewProjectionMatrix, texture)
        table.bindData(textureProgram)
        table.draw()

        // Draw first mallets.
        positionObjectInScene(0f, mallet.height / 2f, -0.4f)
        colorProgram.useProgram()
        colorProgram.setUniforms(modelViewProjectionMatrix, 1f, 0f, 0f)
        mallet.bindData(colorProgram)
        mallet.draw()

        // Draw the puck.
        positionObjectInScene(0f, puck.height / 2f, 0f)
        colorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f)
        puck.bindData(colorProgram)
        puck.draw()

        // Draw second mallets.
        positionObjectInScene(0f, mallet.height / 2f, 0.4f)
        colorProgram.setUniforms(modelViewProjectionMatrix, 0f, 0f, 1f)
        mallet.bindData(colorProgram)
        mallet.draw()
    }

    private fun positionTableInScene() {
        // The table is defined in terms of X & Y coordinates, so we rotate it
        // 90 degrees to lie flat on the XZ plane.
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.rotateM(modelMatrix, 0, -90f, 1f, 0f, 0f)  // a : angleInDegrees
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0)
    }

    private fun positionObjectInScene(x: Float, y: Float, z: Float) {
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(modelMatrix, 0, x, y, z)
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0)
    }
}