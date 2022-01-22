package model

import kotlinx.serialization.Serializable
import kotlin.math.max

val Square.size get() = content.size

@Serializable
data class SquareDto(
    val content: List<List<Point3D>>
)

class Square(
    private val innerContent: List<MutableList<Point3D>>
) {
    val content get() = innerContent as List<List<Point3D>>

    constructor(content: Collection<Collection<Point3D>>) : this(content.map { it.toMutableList() })

    init {
        check(content.all { it.size == size })
    }

    var goodness = calculateGoodness(Quadratic)
        private set

    fun swap(x1: Int, y1: Int, x2: Int, y2: Int) {

        val metric = Quadratic

        fun localDistance(metric: Point3DMetric, localX: Int, localY: Int): Double {
            return listOf(
                localX - 1 to localY,
                localX + 1 to localY,
                localX to localY - 1,
                localX to localY + 1
            ).filter { (x, y) ->
                x in content.indices && y in content[x].indices
            }.sumOf { (x, y) ->
                metric.distance(content[localX][localY], content[x][y])
            }
        }

        val old = localDistance(metric, x1, y1) + localDistance(metric, x2, y2)

        innerContent[x1][y1] = innerContent[x2][y2].also {
            innerContent[x2][y2] = innerContent[x1][y1]
        }

        val new = localDistance(metric, x1, y1) + localDistance(metric, x2, y2)

        goodness += new - old
    }
}

fun Square.maxPoint() = content.flatten().fold(Point3D.zero) { acc, point ->
    Point3D(
        x = max(acc.x, point.x),
        y = max(acc.y, point.y),
        z = max(acc.z, point.z),
    )
}

fun Square.toColors(): List<List<Color>> {
    val max = maxPoint()
    return content.map { line ->
        line.map { point -> point.colorNormalizeBy(max) }
    }
}

fun Square.step() {
    val goodnessBefore = goodness
    var oldGoodness = goodness
    for (x1 in 0 until size)
        for (y1 in 0 until size)
            for (x2 in 0 until size)
                for (y2 in 0 until size) {
                    if (goodness > 30000) oldGoodness = goodness
                    swap(x1, y1, x2, y2)
                    if (goodness >= oldGoodness) {
                        swap(x1, y1, x2, y2)
                    } else {
//                        println("stopped at $x1, $y1, $x2, $y2")
                        break
                    }
                }
    println("min = $goodness")
    if (goodness == goodnessBefore) error("что-то бесполезное")
}

fun Square.toDto() = SquareDto(content)
fun SquareDto.toSquare() = Square(content)