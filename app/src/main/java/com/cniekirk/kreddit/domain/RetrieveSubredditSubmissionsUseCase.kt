package com.cniekirk.kreddit.domain

import com.cniekirk.kreddit.data.Submission
import com.cniekirk.kreddit.data.reddit.jraw.JrawReddit
import com.cniekirk.kreddit.utils.Either
import com.cniekirk.kreddit.utils.Failure
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class RetrieveSubredditSubmissionsUseCase @ExperimentalCoroutinesApi
@Inject constructor(val reddit: JrawReddit)
    : BaseUseCase<List<Submission>, String>() {

    override suspend fun run(params: String): Either<Failure, List<Submission>> {
        return Either.Left(Failure.ServerError())
    }

}