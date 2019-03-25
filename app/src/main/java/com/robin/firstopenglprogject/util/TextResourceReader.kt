package com.robin.firstopenglprogject.util

import android.content.Context
import android.content.res.Resources
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Created by Robin Yeung on 3/18/19.
 */
object TextResourceReader {

    fun readTextFromResource(context: Context, resourceId: Int): String {
        val stringBuilder = StringBuffer()

        try {
            val inputStream = context.resources.openRawResource(resourceId)
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)

            var nextLine: String?

            while (kotlin.run { nextLine = bufferedReader.readLine(); nextLine != null }) {
                stringBuilder.append(nextLine)
                stringBuilder.append("\n")
            }
        } catch (e: Throwable) {
            throw RuntimeException("Could not open resource: $resourceId", e)
        } catch (nfe: Resources.NotFoundException) {
            throw RuntimeException("Resource not found: $resourceId", nfe)
        }

        return stringBuilder.toString()
    }
}