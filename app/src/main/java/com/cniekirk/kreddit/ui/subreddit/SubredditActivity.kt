package com.cniekirk.kreddit.ui.subreddit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cniekirk.kreddit.R
import com.cniekirk.kreddit.core.extensions.inTransaction
import com.cniekirk.kreddit.data.Submission
import com.cniekirk.kreddit.utils.AppViewModelFactory
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

class SubredditActivity : AppCompatActivity(), HasSupportFragmentInjector, SubmissionItemClickListener {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    private var submissionsAdapter = SubmissionsAdapter(this, emptyList())
    private val subredditViewModel: SubredditViewModel by lazy(NONE) { ViewModelProviders.of(this, viewModelFactory)
        .get(SubredditViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupSubmissionList()
        setupSubmissionPage()

        subredditViewModel.submissions.observe(this, Observer { updateSubmissionsList(it) })

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

    private fun updateSubmissionsList(submissions: List<Submission>) {

        submissionsAdapter = SubmissionsAdapter(this, submissions)
        submissions_list.adapter = submissionsAdapter

    }

    private fun setupSubmissionList() {

        val layoutManager = LinearLayoutManager(this)
        submissions_list.layoutManager = layoutManager
        submissions_list.setExpandablePage(submission_page)
        submissions_list.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))
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
        submissions_list.expandItem(submission.toLong())
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

}
