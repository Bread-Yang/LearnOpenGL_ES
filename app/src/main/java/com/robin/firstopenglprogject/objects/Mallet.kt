package com.robin.firstopenglprogject.objects

import com.robin.firstopenglprogject.data.VertexArray
import com.robin.firstopenglprogject.programs.ColorShaderProgram
import com.robin.firstopenglprogject.util.Geometry

/**
 * Created by Robin Yeung on 3/25/19.
 */
class Mallet(val radius: Float, val height: Float, private val numPointsAroundMallet: Int) {

    private companion object {
        const val POSITION_COMPONENT_COUNT = 3
    }

    private val vertexArray: VertexArray

    private val drawList: List<ObjectBuilder.DrawCommand>

    init {
        val generatedData =
            ObjectBuilder.createMallet(Geometry.Point(0f, 0f, 0f), radius, height, numPointsAroundMallet)

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