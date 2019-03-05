package com.cniekirk.kreddit.ui.subreddit

import androidx.lifecycle.MutableLiveData
import com.cniekirk.kreddit.core.platform.BaseViewModel
import com.cniekirk.kreddit.data.SubmissionUiModel
import com.cniekirk.kreddit.domain.RetrieveFrontPageSubmissionsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
class SubredditViewModel @Inject constructor(
    val retrieveFrontPageSubmissionsUseCase: RetrieveFrontPageSubmissionsUseCase
    ): BaseViewModel() {

    val submissions: MutableLiveData<List<SubmissionUiModel>> = MutableLiveData()

    fun loadSubredditSubmissions(params: String) {

        retrieveFrontPageSubmissionsUseCase(params) { it.either(::handleFailure, ::handleSubmissions) }

    }

    private fun handleSubmissions(submissionUiModels: List<SubmissionUiModel>) {
        this.submissions.value = submissionUiModels
    }

}