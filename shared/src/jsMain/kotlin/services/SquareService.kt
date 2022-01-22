package services

import model.SquareDto
import rpc.Transport
import kotlin.coroutines.CoroutineContext

actual class SquareService(coroutineContext: CoroutineContext) {
    private val transport = Transport(coroutineContext)
    actual suspend fun getSquare(): SquareDto {
        return transport.get(
            "getSquare",
            SquareDto.serializer()
        )
    }
}