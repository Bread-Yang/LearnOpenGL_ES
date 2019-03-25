package com.robin.firstopenglprogject.util

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLUtils
import android.util.Log

/**
 * Created by Robin Yeung on 3/25/19.
 */
object TextureHelper {

    private val TAG = "TextureHelper"

    /**
     * Read an image file from resources folder and load the image data into OpenGL.
     */
    fun loadTexture(context: Context, resourceId: Int): Int {
        val textureObjectIds = IntArray(1)
        GLES20.glGenTextures(1, textureObjectIds, 0)

        if (textureObjectIds[0] == 0) {
            Log.w(TAG, "Could not generate a new OpenGL texture object.")
            return 0
        }

        val options = BitmapFactory.Options()
        options.inScaled = false

        val bitmap = BitmapFactory.decodeResource(context.resources, resourceId, options)

        if (bitmap == null) {
            Log.w(TAG, "Resource ID $resourceId could not be decoded.")

            GLES20.glDeleteTextures(1, textureObjectIds, 0)
            return 0
        }

        // The first parameter, GL_TEXTURE_2D, tells OpenGL that this should be treated as a two-dimensional texture,
        // and the second parameter tells OpenGL which texture object ID to bind to
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjectIds[0])
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)

        // This call tells OpenGL to read in the bitmap data defined by bitmap and copy it over into the texture object that is currently bound
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
        bitmap.recycle()

        // Tell OpenGL to generate all of the necessary levels.
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D)

        // Unbind from the texture so that we don't accidentally make further changes to this texture
        // Passing 0 to glBindTexture() unbinds from the current texture.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)

        return textureObjectIds[0]
    }
}