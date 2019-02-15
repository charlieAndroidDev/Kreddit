package com.cniekirk.kreddit.domain

import com.cniekirk.kreddit.utils.Either
import com.cniekirk.kreddit.utils.Failure
import kotlinx.coroutines.*

abstract class BaseUseCase<out Type, in Params> where Type: Any {

    abstract suspend fun run(params: Params): Either<Failure, Type>

    operator fun invoke(params: Params, onResult: (Either<Failure, Type>) -> Unit = {}) {

        val job = GlobalScope.async(Dispatchers.IO, CoroutineStart.DEFAULT) { run(params) }
        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) { onResult.invoke(job.await()) }

    }

}