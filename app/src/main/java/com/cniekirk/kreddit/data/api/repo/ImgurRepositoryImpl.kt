package com.cniekirk.kreddit.data.api.repo

import android.util.Log
import com.cniekirk.kreddit.core.platform.NetworkHandler
import com.cniekirk.kreddit.data.api.ImgurApi
import com.cniekirk.kreddit.data.models.ImageInformation
import com.cniekirk.kreddit.utils.Either
import com.cniekirk.kreddit.utils.Failure
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of ImgurRepository
 * @see ImgurRepository
 */
@Singleton
class ImgurRepositoryImpl @Inject constructor(private val imgurApi: ImgurApi,
                                              private val networkHandler: NetworkHandler): ImgurRepository {

    /**
     * @param imageHash: The hash associated with the image
     * @return An [Either] monad representing a [Failure] or an [ImageInformation] response
     */
    override fun getImageInformation(imageHash: String): Either<Failure, ImageInformation> {

        return when (networkHandler.isConnected) {
            true -> request(imgurApi.getImageInformation(imageHash)) {it}
            false, null -> Either.Left(Failure.NetworkConnectionError())
        }

    }

    /**
     * @param call: The retrofit call
     * @param transform: The transformation function to apply if needed
     * @return An [Either] monad representing a [Failure] or an [R] response
     */
    private fun <T, R> request(call: Call<T>, transform: (T) -> R): Either<Failure, R> {
        return try {
            val response = call.execute()
            when (response.isSuccessful) {
                true -> {
                    response.body()?.let {
                        Either.Right(transform(it))
                    } ?: Either.Left(Failure.ServerError())
                }
                false -> {
                    Either.Left(Failure.ServerError())
                }
            }
        } catch (exception: Throwable) {
            Log.e("IMGUR", "Error: $exception")
            Either.Left(Failure.ServerError())
        }
    }

}