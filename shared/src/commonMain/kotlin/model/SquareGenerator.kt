package model

import utils.fullJoin
import kotlin.math.pow

object SquareFactory {
    fun createWithRandomFill(length: Int): Square {
        val gradationSize = length.toDouble().pow(2.0 / 3).toInt() + 1
        val gradation = (0 until gradationSize).toList()
        val colors = gradation.fullJoin(gradation)
            .toList()
            .fullJoin(gradation) { (r, g), b ->
//                Point3D(r % 2, g % 2, b % 2)
                Point3D(r, g, b)
            }.take(length * length)
            .shuffled()
            .chunked(length)
            .toList()
        return Square(colors)
    }

}