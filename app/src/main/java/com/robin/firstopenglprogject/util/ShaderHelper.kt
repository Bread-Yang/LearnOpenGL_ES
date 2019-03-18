package com.robin.firstopenglprogject.util

import android.opengl.GLES20
import android.opengl.GLES20.*
import android.util.Log

/**
 * Created by Robin Yeung on 3/18/19.
 */
object ShaderHelper {
    private val TAG = "ShaderHelper"

    fun linkProgram(vertextShaderId: Int, fragmentShaderId: Int): Int {
        // create a new program object and store the ID of that object in $ProgramObjectId
        val programObjectId = GLES20.glCreateProgram()

        if (programObjectId == 0) {
            Log.w(TAG, "Could not create new program.")
            return 0
        }

        // attach vertex and fragment shaders
        GLES20.glAttachShader(programObjectId, vertextShaderId)
        GLES20.glAttachShader(programObjectId, fragmentShaderId)

        GLES20.glLinkProgram(programObjectId)

        val linkStatus = IntArray(1)
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_LINK_STATUS, linkStatus, 0)

        // Print the program info log to the Android log output.
        Log.v(TAG, "Results of linking program:\n" + glGetProgramInfoLog(programObjectId))

        if (linkStatus[0] == 0) {
            // If it failed, delete the program object.
            glDeleteProgram(programObjectId)
            Log.w(TAG, "Linking of program failed.")
            return 0
        }
        return programObjectId
    }

    fun validateProgram(programObjectId: Int): Boolean {
        glValidateProgram(programObjectId)

        val validateStatus = IntArray(1)
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_VALIDATE_STATUS, validateStatus, 0)

        Log.v(
            TAG, "Results of validating program: " + validateStatus[0]
                    + "\nLog:" + glGetProgramInfoLog(programObjectId)
        )
        return validateStatus[0] != 0
    }

    fun compileVertexShader(shaderCode: String): Int {
        return compileShader(GLES20.GL_VERTEX_SHADER, shaderCode)
    }

    fun compileFragmentShader(shaderCode: String): Int {
        return compileShader(GLES20.GL_FRAGMENT_SHADER, shaderCode)
    }

    private fun compileShader(type: Int, shaderCode: String): Int {
        // create a shader object and store the ID of that object in $shaderObjectId.
        val shaderObjectId = GLES20.glCreateShader(type)

        if (shaderObjectId == 0) {
            Log.w(TAG, "Could not create new shader.")
            return 0
        }

        // upload shader source code into the shader object
        GLES20.glShaderSource(shaderObjectId, shaderCode)

        // compile the source code that was previously uploaded to shaderObjectId.
        GLES20.glCompileShader(shaderObjectId)

        // read the compile status associated with $shaderObjectId and write it to the 0th element of $compileStatus.
        val compileStatus = IntArray(1)
        GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS, compileStatus, 0)

        // Print the shader info log
        Log.v(
            TAG, """Results of compiling source :
            $shaderCode
            :
            ${GLES20.glGetShaderInfoLog(shaderObjectId)}
        """
        )

        if (compileStatus[0] == 0) {
            // If it failed, delete the shader object.
            GLES20.glDeleteShader(shaderObjectId)

            Log.w(TAG, "Compilation of shader failed.")
            return 0
        }

        return shaderObjectId
    }
}