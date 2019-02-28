package com.cniekirk.kreddit.domain

import com.cniekirk.kreddit.data.Submission
import com.cniekirk.kreddit.utils.Either
import com.cniekirk.kreddit.utils.Failure

class RetrieveSubredditSubmissionsUseCase()
    : BaseUseCase<List<Submission>, String>() {

    override suspend fun run(params: String): Either<Failure, List<Submission>> {

    }


}