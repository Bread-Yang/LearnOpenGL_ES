package com.robin.firstopenglprogject.util

/**
 * Created by Robin Yeung on 3/21/19.
 */
object MatrixHelper {

    fun perspectiveM(m: FloatArray, yFovInDegrees: Float, aspect: Float, n: Float, f: Float) {

        val angleInRadians = (yFovInDegrees * Math.PI / 180.0f).toFloat()

        val a = (1.0f / Math.tan(angleInRadians / 2.0)).toFloat()

        // OpenGL stores matrix data in column-major order, which means that we write out data one column at a time
        // rather than one row at a time, The first four values refer to the first column.
        /**
         * _                                                _
         * | a/aspect       0           0           0       |
         * |    0           a           0           0       |
         * |    0           0      -(f+n)/(f-n)  -(2fn/f-n) |
         * |    0           0          -1           0       |
         * -                                                -
         */
        m[0] = a / aspect
        m[1] = 0f
        m[2] = 0f
        m[3] = 0f

        m[4] = 0f
        m[5] = a
        m[6] = 0f
        m[7] = 0f

        m[8] = 0f
        m[9] = 0f
        m[10] = -((f + n) / (f - n))
        m[11] = -1f

        m[12] = 0f
        m[13] = 0f
        m[14] = -((2f * f * n) / (f - n))
        m[15] = 0f
    }
}