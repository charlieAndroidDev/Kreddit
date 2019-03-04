package com.cniekirk.kreddit.domain

import com.cniekirk.kreddit.data.SubmissionUiModel
import com.cniekirk.kreddit.data.reddit.jraw.JrawReddit
import com.cniekirk.kreddit.utils.Either
import com.cniekirk.kreddit.utils.Failure
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.dean.jraw.models.SubredditSort
import net.dean.jraw.models.TimePeriod
import javax.inject.Inject

@ExperimentalCoroutinesApi
class RetrieveSubredditSubmissionsUseCase
@Inject constructor(val reddit: JrawReddit)
    : BaseUseCase<List<SubmissionUiModel>, String>() {

    override suspend fun run(params: String): Either<Failure, List<SubmissionUiModel>> {
        return reddit.subreddit().getSubmissions(params, SubredditSort.HOT, TimePeriod.DAY, true, 50)
    }

}