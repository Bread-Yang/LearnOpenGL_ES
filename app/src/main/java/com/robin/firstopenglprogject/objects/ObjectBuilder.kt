package com.robin.firstopenglprogject.objects

import android.opengl.GLES20.*
import com.robin.firstopenglprogject.util.Geometry

/**
 * Created by Robin Yeung on 3/26/19.
 */
class ObjectBuilder(private val sizeInVertices: Int) {

    companion object {
        private const val FLOATS_PER_VERTEX = 3     // how many floats we need for a vertex.

        fun createPuck(puck: Geometry.Cylinder, numPoints: Int): GeneratedData {
            val verticesNum = sizeOfCircleInVertices(numPoints) + sizeOfOpenCylinderInVertices(numPoints)

            val builder = ObjectBuilder(verticesNum)

            val puckTop = Geometry.Circle(puck.center.translateY(puck.height / 2f), puck.radius)

            // A puck is built out of one cylinder top (equivalent to a circle) and one cylinder side.
            builder.appendCircle(puckTop, numPoints)
            builder.appendOpenCylinder(puck, numPoints)

            return builder.build()
        }

        fun createMallet(center: Geometry.Point, radius: Float, height: Float, numPoints: Int): GeneratedData {
            val size = sizeOfCircleInVertices(numPoints) * 2 + sizeOfOpenCylinderInVertices(numPoints) * 2

            val builder = ObjectBuilder(size)

            // First, generate the mallet base.
            val baseHeight = height * 0.25f

            val baseCircle = Geometry.Circle(center.translateY(-baseHeight), radius)
            val baseCylinder =
                Geometry.Cylinder(baseCircle.center.translateY(-baseHeight / 2f), radius, baseHeight)

            builder.appendCircle(baseCircle, numPoints)
            builder.appendOpenCylinder(baseCylinder, numPoints)

            val handleHeight = height * 0.75f
            val handleRadius = radius / 3f

            val handleCircle = Geometry.Circle(center.translateY(height * 0.5f), handleRadius)
            val handleCylinder =
                Geometry.Cylinder(handleCircle.center.translateY(-handleHeight / 2f), handleRadius, handleHeight)

            builder.appendCircle(handleCircle, numPoints)
            builder.appendOpenCylinder(handleCylinder, numPoints)

            return builder.build()
        }

        /**
         * A cylinder top is a circle built out of a triangle fan; it has one vertex in the center, one vertex for each
         * point around the circle, and the first vertex around the circle is repeated twice so that we can close the
         * circle off.
         */
        private fun sizeOfCircleInVertices(numPoints: Int): Int {
            return 1 + (numPoints + 1)
        }

        /**
         * A cylinder side is a rolled-up rectangle built out of a triangle strip, with two vertices for each point
         * around the circle, and with the first two vertices repeated twice so that we can close off the tube.
         */
        private fun sizeOfOpenCylinderInVertices(numPoints: Int): Int {
            return (numPoints + 1) * 2
        }
    }

    private val vertexData: FloatArray = FloatArray(sizeInVertices * FLOATS_PER_VERTEX)
    private var offset: Int = 0                     // Keep track of the position in the array for the next vertex.
    private val drawList = ArrayList<DrawCommand>()

    fun appendCircle(circle: Geometry.Circle, numPoints: Int) {
        val startVertex = offset / FLOATS_PER_VERTEX
        val numVertices = sizeOfCircleInVertices(numPoints)

        // Center point of fan
        vertexData[offset++] = circle.center.x
        vertexData[offset++] = circle.center.y
        vertexData[offset++] = circle.center.z

        // Fan around center point. <= is used because we want to generate
        // the point at the starting angle twice to complete the fan.
        for (i in 0..numPoints) {
            val angleInRadians = i.toFloat() / numPoints.toFloat() * (Math.PI * 2f)

            vertexData[offset++] = circle.center.x + circle.radius * Math.cos(angleInRadians).toFloat()
            vertexData[offset++] = circle.center.y
            vertexData[offset++] = circle.center.z + circle.radius * Math.sin(angleInRadians).toFloat()
        }

        drawList.add(object : DrawCommand {
            override fun draw() {
                glDrawArrays(GL_TRIANGLE_FAN, startVertex, numVertices)
            }

        })
    }

    fun appendOpenCylinder(cylinder: Geometry.Cylinder, numPoints: Int) {
        val startVertex = offset / FLOATS_PER_VERTEX
        val numVertices = sizeOfOpenCylinderInVertices(numPoints)
        val yStart = cylinder.center.y - (cylinder.height / 2f)
        val yEnd = cylinder.center.y + (cylinder.height / 2f)

        // Generate strip around center point. <= is used because we want to
        // generate the points at the starting angle twice, to complete the
        // strip.
        for (i in 0..numPoints) {
            val angleInRadians = i.toFloat() / numPoints.toFloat() * (Math.PI * 2f)

            val xPosition = cylinder.center.x + cylinder.radius * Math.cos(angleInRadians).toFloat()
            val zPosition = cylinder.center.z + cylinder.radius * Math.sin(angleInRadians).toFloat()

            vertexData[offset++] = xPosition
            vertexData[offset++] = yStart
            vertexData[offset++] = zPosition

            vertexData[offset++] = xPosition
            vertexData[offset++] = yEnd
            vertexData[offset++] = zPosition
        }

        drawList.add(object : DrawCommand {
            override fun draw() {
                glDrawArrays(GL_TRIANGLE_STRIP, startVertex, numVertices)
            }
        })
    }

    fun build(): GeneratedData {
        return GeneratedData(vertexData, drawList)
    }

    interface DrawCommand {
        fun draw()
    }

    data class GeneratedData(val vertexData: FloatArray, val drawList: ArrayList<DrawCommand>)
}