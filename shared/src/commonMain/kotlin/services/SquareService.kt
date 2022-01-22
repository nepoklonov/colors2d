package services

import model.SquareDto

expect class SquareService {
    suspend fun getSquare(): SquareDto
}