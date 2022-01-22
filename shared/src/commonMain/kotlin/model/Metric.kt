package model

import kotlin.math.pow

interface Metric<T> {
    val distance: (p1: T, p2: T) -> Double
}

sealed class Point3DMetric(
    override val distance: (p1: Point3D, p2: Point3D) -> Double
) : Metric<Point3D>

val Point3DMetric.norm: (Point3D) -> Double
    get() = { point -> distance(point, Point3D.zero) }

object Quadratic : Point3DMetric({ p1, p2 ->
    (p1 - p2).run { x * x + y * y + z * z }.toDouble()
})

object Euclidean : Point3DMetric({ p1, p2 ->
    (p1 - p2).run { x * x + y * y + z * z }.toDouble().pow(0.5)
})