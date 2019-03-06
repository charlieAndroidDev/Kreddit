package com.cniekirk.kreddit.domain

import com.cniekirk.kreddit.data.SubmissionRequestOptions
import com.cniekirk.kreddit.ui.subreddit.uimodel.SubmissionUiModel
import com.cniekirk.kreddit.data.reddit.jraw.JrawReddit
import com.cniekirk.kreddit.utils.Either
import com.cniekirk.kreddit.utils.Failure
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class RetrieveFrontPageSubmissionsUseCase
@Inject constructor(val reddit: JrawReddit)
    : BaseUseCase<List<SubmissionUiModel>, SubmissionRequestOptions>() {

    override suspend fun run(params: SubmissionRequestOptions): Either<Failure, List<SubmissionUiModel>> {
        return reddit.subreddit().getSubmissions(params)
    }

}