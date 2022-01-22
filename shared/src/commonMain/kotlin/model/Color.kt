package model

import kotlinx.serialization.Serializable

@Serializable
data class Color(
    val r: Int,
    val g: Int,
    val b: Int
) {
    init {
        check(r in 0..255)
        check(g in 0..255)
        check(b in 0..255)
    }
}

val Color.asPoint: Point3D
    get() = Point3D(r, g, b)