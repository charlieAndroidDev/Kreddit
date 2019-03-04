package com.cniekirk.kreddit.ui.subreddit

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cniekirk.kreddit.R
import com.cniekirk.kreddit.core.extensions.inTransaction
import com.cniekirk.kreddit.data.SubmissionUiModel
import com.cniekirk.kreddit.utils.AppViewModelFactory
import com.cniekirk.kreddit.utils.animation.SubmissionListItemAnimtor
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

@ExperimentalCoroutinesApi
class SubredditActivity : AppCompatActivity(), HasSupportFragmentInjector, SubmissionItemClickListener {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

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
        subredditViewModel.loadSubredditSubmissions("")

    }

    private fun setupSubmissionPage() {
        var submissionFragment = supportFragmentManager.findFragmentById(submission_page.id) as FragmentSubmission?
        if(submissionFragment == null) {
            submissionFragment = FragmentSubmission()
        }

        supportFragmentManager.inTransaction {
            replace(submission_page.id, submissionFragment)
        }

    }

    private fun updateSubmissionsList(submissionUiModels: List<SubmissionUiModel>) {

        submissionsAdapter = SubmissionsAdapter(this, submissionUiModels)
        submissionsAdapter.setHasStableIds(true)
        submissions_list.adapter = submissionsAdapter

    }

    private fun setupSubmissionList() {

        submissions_list.itemAnimator = SubmissionListItemAnimtor(0)
            .withInterpolator(FastOutSlowInInterpolator())
            .withAddDuration(250)
            .withRemoveDuration(250)
        val layoutManager = LinearLayoutManager(this)
        submissions_list.layoutManager = layoutManager
        submissions_list.setExpandablePage(submission_page)
        submissions_list.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
        submissionsAdapter.setHasStableIds(true)
        submissions_list.adapter = submissionsAdapter

    }

    override fun onBackPressed() {

        if(submission_page.isExpandedOrExpanding) {
            submissions_list.collapse()
        } else {
            super.onBackPressed()
        }

    }

    override fun onItemClick(submission: Int) {
        submissions_list.expandItem(submissionsAdapter.getItemId(submission))
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

}
