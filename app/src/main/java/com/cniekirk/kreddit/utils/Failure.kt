package com.cniekirk.kreddit.utils

sealed class Failure {

    class NetworkConnection: Failure()
    class ServerError(): Failure()

    abstract class FeatureFailure: Failure()

}