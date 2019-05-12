package com.cniekirk.kreddit.ui.subreddit

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.cniekirk.kreddit.R
import com.cniekirk.kreddit.core.extensions.inTransaction
import com.cniekirk.kreddit.ui.submission.FragmentSubmission
import com.cniekirk.kreddit.ui.subreddit.uimodel.SubmissionUiModel
import com.cniekirk.kreddit.ui.subreddit.uimodel.SubredditRequests
import com.cniekirk.kreddit.viewmodel.AppViewModelFactory
import com.cniekirk.kreddit.utils.animation.SubmissionListItemAnimtor
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

/**
 * The main Activity of the application
 *
 * A list of Subreddit submissions are displayed in a list and expanded into the [submissionFragment] on click
 */
@ExperimentalCoroutinesApi
class SubredditActivity : AppCompatActivity(), HasSupportFragmentInjector, SubmissionItemClickListener {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    private var submissionFragment = FragmentSubmission()
    private var submissionsAdapter = SubmissionsAdapter(this, emptyList())
    private lateinit var subredditViewModel: SubredditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(flex_toolbar)

        setupSubmissionList()
        setupSubmissionPage()

        subredditViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(SubredditViewModel::class.java)
        subredditViewModel.submissions.observe(this, Observer { updateSubmissionsList(it) })
        subredditViewModel.clickedSubmission.observe(this, Observer { submissionFragment.populateUi(it) })
        subredditViewModel.isLoadingSubmissions.observe(this, Observer { isLoading ->
            when {
                isLoading -> progress_bar.visibility = View.VISIBLE
                else -> progress_bar.visibility = View.GONE
            }
        })
        subredditViewModel.loadSubredditSubmissions(SubredditRequests.frontPage())

    }

    /**
     * Setup the page required to display a submission
     */
    private fun setupSubmissionPage() {

        submission_page.parentToolbar = flex_toolbar

        supportFragmentManager.inTransaction {
            replace(submission_page.id, submissionFragment)
        }

    }

    /**
     * Setup the list of submissions in the Subreddit
     */
    private fun setupSubmissionList() {

        submissions_list.itemAnimator = SubmissionListItemAnimtor(0)
            .withInterpolator(FastOutSlowInInterpolator())
            .withAddDuration(250)
            .withRemoveDuration(250)
        val layoutManager = LinearLayoutManager(this)
        submissions_list.layoutManager = layoutManager
        submissions_list.setExpandablePage(submission_page)
        submissions_list.adapter = submissionsAdapter

    }

    /**
     * Update the list of submissions list when we actually have data to display
     */
    private fun updateSubmissionsList(submissionUiModels: List<SubmissionUiModel>) {

        submissionsAdapter = SubmissionsAdapter(this, submissionUiModels)
        submissions_list.adapter = submissionsAdapter

    }

    /**
     * Capture back presses and collapse the [submissionFragment] is it is visible
     */
    override fun onBackPressed() {

        if(submission_page.isExpandedOrExpanding) {
            submissions_list.collapse()
        } else {
            super.onBackPressed()
        }

    }

    /**
     * Handle Submission item clicks
     */
    override fun onItemClick(submission: Int) {
        subredditViewModel.clickSubmission(submission)
        submissions_list.expandItem(submissionsAdapter.getItemId(submission))
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

}
