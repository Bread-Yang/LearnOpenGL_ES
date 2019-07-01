package com.robin.firstopenglprogject.util

/**
 * Created by Robin Yeung on 3/26/19.
 */
class Geometry {

    companion object {
        fun vectorBetween(from: Point, to: Point): Vector {
            return Vector(to.x - from.x, to.y - from.y, to.z - from.z)
        }

        fun intersects(sphere: Sphere, ray: Ray): Boolean {
            return distanceBetween(sphere.center, ray) < sphere.radius
        }

        fun distanceBetween(point: Point, ray: Ray): Float {
            val p1ToPoint = vectorBetween(ray.point, point)
            val p2ToPoint = vectorBetween(ray.point.translate(ray.vector), point)

            // The length of the cross product gives the area of an imaginary parallelogram having the two vectors
            // as sides. A parallelogram can be thought of as consisting of two triangles, so this is the same as
            // twice the area of the triangle defined by the two vectors.
            // http://en.wikipedia.org/wiki/Cross_product#Geometric_meaning
            // Calculating the cross product will give us a third vector that is perpendicular to the first two vectors,
            // but more importantly for us, the length of this vector will be equal to twice the area of the triangle
            // defined by the first two vectors

            // 向量积|c|=|a×b|=|a||b|sin<a，b>
            // 即c的长度在数值上等于以a，b，夹角为θ组成的平行四边形的面积。
            val areaOfTriangleTimesTwo = p1ToPoint.crossProduct(p2ToPoint).length()
            val lengthOfBase = ray.vector.length()

            // The area of a triangle is also equal to (base * height) / 2. In other words, the height is equal to
            // (area * 2) / base. The height of this triangle is the distance from the point to the ray.
            return areaOfTriangleTimesTwo / lengthOfBase
        }

        fun intersectionPoint(ray: Ray, plane: Plane): Point {
            val rayToPlaneVector = vectorBetween(ray.point, plane.point)

            val scaleFactor = rayToPlaneVector.dotProduct(plane.normal) / ray.vector.dotProduct(plane.normal)
            return ray.point.translate(ray.vector.scale(scaleFactor))
        }
    }

    open class Point(val x: Float, val y: Float, val z: Float) {

        fun translateY(distance: Float): Point {
            return Point(x, y + distance, z)
        }

        fun translate(vector: Vector): Point {
            return Point(x + vector.x, y + vector.y, z + vector.z)
        }
    }

    open class Circle(val center: Point, val radius: Float) {

        fun scale(scale: Float): Circle {
            return Circle(center, radius * scale)
        }
    }

    open class Cylinder(val center: Point, val radius: Float, val height: Float)

    open class Ray(val point: Point, val vector: Vector)

    open class Vector(val x: Float, val y: Float, val z: Float) {

        fun length(): Float {
            return Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
        }

        // 叉乘几何意义 :
        // 在三维几何中，向量a和向量b的叉乘结果是一个向量，更为熟知的叫法是法向量，该向量垂直于a和b向量构成的平面。
        // 在3D图像学中，叉乘的概念非常有用，可以通过两个向量的叉乘，生成第三个垂直于a，b的法向量，从而构建X、Y、Z坐标系.
        // 在二维空间中，叉乘还有另外一个几何意义就是：a * b等于由向量a和向量b构成的平行四边形的面积
        fun crossProduct(other: Vector): Vector {
            return Vector(
                (y * other.z) - (z * other.y),
                (z * other.x) - (x * other.z),
                (x * other.y) - (y * other.x)
            )
        }

        // 内积（点乘）的几何意义包括：
        //      表征或计算两个向量之间的夹角
        //      b向量在a向量方向上的投影
        fun dotProduct(other: Vector): Float {
            return x * other.x + y * other.y + z * other.z
        }

        fun scale(f: Float): Vector {
            return Vector(x * f, y * f, z * f)
        }
    }

    open class Sphere(val center: Point, val radius: Float)

    open class Plane(val point: Point, val normal: Vector)
}