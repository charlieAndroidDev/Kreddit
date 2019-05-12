package com.cniekirk.kreddit.utils

/**
 * Class representing a Failure
 */
sealed class Failure {

    class NetworkConnectionError: Failure()
    class ServerError(): Failure()

    abstract class FeatureFailure: Failure()

}