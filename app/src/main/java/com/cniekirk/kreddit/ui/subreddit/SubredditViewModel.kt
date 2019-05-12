package com.cniekirk.kreddit.ui.subreddit

import androidx.lifecycle.MutableLiveData
import com.cniekirk.kreddit.core.platform.BaseViewModel
import com.cniekirk.kreddit.data.SubmissionRequestOptions
import com.cniekirk.kreddit.ui.subreddit.uimodel.SubmissionUiModel
import com.cniekirk.kreddit.domain.RetrieveFrontPageSubmissionsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import javax.inject.Singleton

/**
 * ViewModel to provide access to Reddit Submission data
 */
@ExperimentalCoroutinesApi
@Singleton
class SubredditViewModel @Inject constructor(
    val retrieveFrontPageSubmissionsUseCase: RetrieveFrontPageSubmissionsUseCase
    ): BaseViewModel() {

    val submissions: MutableLiveData<List<SubmissionUiModel>> = MutableLiveData()
    val clickedSubmission: MutableLiveData<SubmissionUiModel> = MutableLiveData()
    val isLoadingSubmissions: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * Load submissions from the network and execute [handleSubmissions] is successful
     * @param params: The [SubmissionRequestOptions] associated with this request for data
     */
    fun loadSubredditSubmissions(params: SubmissionRequestOptions) {

        isLoadingSubmissions.value = true
        retrieveFrontPageSubmissionsUseCase(params) { it.either(::handleFailure, ::handleSubmissions) }

    }

    /**
     * Handle a click on a submission
     */
    fun clickSubmission(submissionIndex: Int) {

        clickedSubmission.value = submissions.value?.get(submissionIndex)

    }

    /**
     * Changes the loading state for the UI updates the [submissions] LiveData with data
     */
    private fun handleSubmissions(submissionUiModels: List<SubmissionUiModel>) {

        isLoadingSubmissions.value = false
        this.submissions.value = submissionUiModels

    }

}