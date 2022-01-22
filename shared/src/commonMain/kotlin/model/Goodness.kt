package model

fun Square.calculateGoodness(point3DMetric: Point3DMetric): Double {
    val horizontal = content.flatMap { line ->
        line.zipWithNext()
    }

    val vertical = content.zipWithNext()
        .flatMap { (high, low) -> high.zip(low) }

    return (vertical + horizontal).sumOf { (first, second) ->
        point3DMetric.distance(first, second)
    }
}