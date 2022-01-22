package model

import kotlinx.serialization.Serializable

@Serializable
data class Point3D(
    val x: Int,
    val y: Int,
    val z: Int
) {
    companion object {
        val zero = Point3D(0, 0, 0)
    }
}

operator fun Point3D.minus(other: Point3D): Point3D {
    return Point3D(x - other.x, y - other.y, z - other.z)
}

infix fun Point3D.hadamardTimes(other: Point3D) = Point3D(
    x * other.x, y * other.y, z * other.z
)

infix fun Point3D.hadamardDiv(other: Point3D) = Point3D(
    x / other.x, y / other.y, z / other.z
)

val Point3D.abs get() = Euclidean.norm(this)

fun Point3D.distanceTo(other: Point3D) = (this - other).abs

val Point3D.asColor: Color
    get() = Color(x, y, z)

fun Point3D.colorNormalizeBy(max: Point3D): Color {
    val maxColorPoint = Point3D(255, 255, 255)
    val colorPoint = this hadamardTimes maxColorPoint hadamardDiv max
    return colorPoint.asColor
}