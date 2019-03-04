package com.cniekirk.kreddit.ui.subreddit

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.cniekirk.kreddit.core.platform.BaseViewModel
import com.cniekirk.kreddit.data.SubmissionUiModel
import com.cniekirk.kreddit.domain.RetrieveSubredditSubmissionsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
class SubredditViewModel @Inject constructor(
    val retrieveSubredditSubmissionsUseCase: RetrieveSubredditSubmissionsUseCase
    ): BaseViewModel() {

    val submissions: MutableLiveData<List<SubmissionUiModel>> = MutableLiveData()

    fun loadSubredditSubmissions(params: String) {

        retrieveSubredditSubmissionsUseCase(params) { it.either(::handleFailure, ::handleSubmissions) }

    }

    private fun handleSubmissions(submissionUiModels: List<SubmissionUiModel>) {
        this.submissions.value = submissionUiModels
    }

}