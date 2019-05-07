package com.robin.firstopenglprogject.objects

import com.robin.firstopenglprogject.data.VertexArray
import com.robin.firstopenglprogject.programs.ColorShaderProgram
import com.robin.firstopenglprogject.util.Geometry

/**
 * Created by Robin Yeung on 4/11/19.
 */
class Puck(val radius: Float, val height: Float, val numPointsAroundPuck: Int) {

    companion object {
        private const val POSITION_COMPONENT_COUNT = 3
    }

    private val vertexArray: VertexArray

    private val drawList: List<ObjectBuilder.DrawCommand>

    init {
        val generatedData =
            ObjectBuilder.createPuck(Geometry.Cylinder(Geometry.Point(0f, 0f, 0f), radius, height), numPointsAroundPuck)

        vertexArray = VertexArray(generatedData.vertexData)
        drawList = generatedData.drawList
    }

    fun bindData(colorProgram: ColorShaderProgram) {
        vertexArray.setVertexAttribPointer(0, colorProgram.getPositionAttributeLocation(),
            POSITION_COMPONENT_COUNT, 0)
    }

    fun draw() {
        for (drawCommand in drawList) {
            drawCommand.draw()
        }
    }
}