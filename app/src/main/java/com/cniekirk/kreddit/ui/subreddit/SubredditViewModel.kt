package com.cniekirk.kreddit.ui.subreddit

import androidx.lifecycle.MutableLiveData
import com.cniekirk.kreddit.core.platform.BaseViewModel
import com.cniekirk.kreddit.data.Submission
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubredditViewModel @Inject constructor(): BaseViewModel() {

    val submissions: MutableLiveData<List<Submission>> = MutableLiveData()



}