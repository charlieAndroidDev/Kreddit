package com.cniekirk.kreddit.domain

import com.cniekirk.kreddit.data.SubmissionRequestOptions
import com.cniekirk.kreddit.data.reddit.Reddit
import com.cniekirk.kreddit.ui.subreddit.uimodel.SubmissionUiModel
import com.cniekirk.kreddit.utils.Either
import com.cniekirk.kreddit.utils.Failure
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

/**
 * UseCase to get the list of Reddit submission from the front page
 * @param reddit: The [Reddit] instance which provides functions to get Reddit data
 */
@ExperimentalCoroutinesApi
class RetrieveFrontPageSubmissionsUseCase
@Inject constructor(val reddit: Reddit)
    : BaseUseCase<List<SubmissionUiModel>, SubmissionRequestOptions>() {

    /**
     * Long running operation to get the list of Reddit submission from the front page
     * @param params: The [SubmissionRequestOptions] model which represents our request
     */
    override suspend fun run(params: SubmissionRequestOptions): Either<Failure, List<SubmissionUiModel>> {
        return reddit.subreddit().getSubmissions(params)
    }

}