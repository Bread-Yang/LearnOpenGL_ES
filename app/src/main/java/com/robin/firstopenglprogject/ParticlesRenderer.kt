package com.robin.firstopenglprogject

import android.content.Context
import android.graphics.Color
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.robin.firstopenglprogject.objects.ParticleShooter
import com.robin.firstopenglprogject.objects.ParticleSystem
import com.robin.firstopenglprogject.programs.ParticleShaderProgram
import com.robin.firstopenglprogject.util.Geometry
import com.robin.firstopenglprogject.util.MatrixHelper
import com.robin.firstopenglprogject.util.TextureHelper
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


/**
 * Created by Robin Yeung on 3/15/19.
 */
class ParticlesRenderer(val context: Context) : GLSurfaceView.Renderer {

    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val viewProjectionMatrix = FloatArray(16)

    private var texture: Int = 0
    private lateinit var particleProgram: ParticleShaderProgram
    private lateinit var particleSystem: ParticleSystem
    private lateinit var redParticleShooter: ParticleShooter
    private lateinit var greenParticleShooter: ParticleShooter
    private lateinit var blueParticleShooter: ParticleShooter
    private var globalStartTime: Long = 0L
    private val angleVarianceInDegree = 5f
    private val speedVariance = 1f

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        glEnable(GL_BLEND)
        // OpenGL’s default blending equation : output = (source factor * source fragment) + (destination factor * destination fragment)
        // we called glBlendFunc() with each factor set to GL_ONE, which changes the blending equation as follows:
        // output = (GL_ONE * source fragment) + (GL_ONE * destination fragment)
        glBlendFunc(GL_ONE, GL_ONE)

        particleProgram = ParticleShaderProgram(context)
        particleSystem = ParticleSystem(10000)
        globalStartTime = System.nanoTime()

        val particleDirection = Geometry.Vector(0f, 0.5f, 0f)

        redParticleShooter = ParticleShooter(
            Geometry.Point(-1f, 0f, 0f), particleDirection, Color.rgb(255, 50, 5), angleVarianceInDegree, speedVariance
        )
        greenParticleShooter = ParticleShooter(
            Geometry.Point(0f, 0f, 0f), particleDirection, Color.rgb(25, 255, 25), angleVarianceInDegree, speedVariance
        )
        blueParticleShooter = ParticleShooter(
            Geometry.Point(1f, 0f, 0f), particleDirection, Color.rgb(5, 50, 255), angleVarianceInDegree, speedVariance
        )

        texture = TextureHelper.loadTexture(context, R.drawable.particle_texture)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0, 0, width, height)

        MatrixHelper.perspectiveM(projectionMatrix, 45f, width.toFloat() / height.toFloat(), 1f, 10f)

        Matrix.setIdentityM(viewMatrix, 0)
        Matrix.translateM(viewMatrix, 0, 0f, -1.5f, -5f)
        Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
    }

    override fun onDrawFrame(gl: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)

        val currentTime = (System.nanoTime() - globalStartTime) / 1000000000f

        redParticleShooter.addParticles(particleSystem, currentTime, 5)
        greenParticleShooter.addParticles(particleSystem, currentTime, 5)
        blueParticleShooter.addParticles(particleSystem, currentTime, 5)

        particleProgram.useProgram()
        particleProgram.setUniforms(viewProjectionMatrix, currentTime,texture)
        particleSystem.bindData(particleProgram)
        particleSystem.draw()
    }
}