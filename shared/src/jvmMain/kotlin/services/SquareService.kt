package services

import model.*

val square = SquareFactory.createWithRandomFill(16)

actual class SquareService {
    actual suspend fun getSquare(): SquareDto {
        return square.toDto()
    }
}