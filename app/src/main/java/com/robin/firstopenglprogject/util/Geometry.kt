package com.robin.firstopenglprogject.util

/**
 * Created by Robin Yeung on 3/26/19.
 */
class Geometry {

    open class Point(val x: Float, val y: Float, val z: Float) {

        fun translateY(distance: Float): Point {
            return Point(x, y + distance, z)
        }
    }

    open class Circle(val center: Point, val radius: Float) {

        fun scale(scale: Float): Circle {
            return Circle(center, radius * scale)
        }
    }

    open class Cylinder(val center: Point, val radius: Float, val height: Float)
}