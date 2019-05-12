package com.cniekirk.kreddit.utils

/**
 * An Either monad implemented in kotlin
 * NOT MY WORK
 * @link https://github.com/android10/Android-CleanArchitecture-Kotlin/blob/master/app/src/main/kotlin/com/fernandocejas/sample/core/functional/Either.kt
 */
sealed class Either<out Failure, out Success> {

    /** * Represents the left side of [Either] class which by convention is a "Failure". */
    data class Left<out Failure>(val a: Failure) : Either<Failure, Nothing>()
    /** * Represents the right side of [Either] class which by convention is a "Success". */
    data class Right<out Success>(val b: Success) : Either<Nothing, Success>()

    val isRight get() = this is Right<Success>
    val isLeft get() = this is Left<Failure>

    fun <Failure> left(a: Failure) = Either.Left(a)
    fun <Success> right(b: Success) = Either.Right(b)

    fun either(fncL: (Failure) -> Any, fncR: (Success) -> Any): Any =
        when(this) {
            is Either.Left -> fncL(a)
            is Either.Right -> fncR(b)
        }

}

// Composes 2 functions
fun <A, B, C> ((A) -> B).c(f: (B) -> C): (A) -> C = {
    f(this(it))
}