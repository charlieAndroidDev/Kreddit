package com.cniekirk.kreddit.ui.subreddit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cniekirk.kreddit.R
import com.cniekirk.kreddit.data.SubmissionRepository
import kotlinx.android.synthetic.main.activity_main.*

class SubredditActivity : AppCompatActivity(), SubmissionItemClickListener {

    private val submissionsAdapter = SubmissionsAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupSubmissionList()
        setupSubmissionPage()

    }

    private fun setupSubmissionPage() {
        var submissionFragment = supportFragmentManager.findFragmentById(submission_page.id) as FragmentSubmission?
        if(submissionFragment == null) {
            submissionFragment = FragmentSubmission()
        }

        supportFragmentManager
            .beginTransaction()
            .replace(submission_page.id, submissionFragment)
            .commitNowAllowingStateLoss()

    }

    private fun setupSubmissionList() {

        val layoutManager = LinearLayoutManager(this)
        submissions_list.layoutManager = layoutManager
        submissions_list.setExpandablePage(submission_page)
        submissions_list.addItemDecoration(DividerItemDecoration(this, layoutManager.orientation))

        submissionsAdapter.submitList(SubmissionRepository.submissions())
        submissions_list.adapter = submissionsAdapter

    }

    override fun onItemClick(submission: Int) {
        submissions_list.expandItem(submission.toLong())
    }

    override fun onBackPressed() {

        if(submission_page.isExpandedOrExpanding) {
            submissions_list.collapse()
        } else {
            super.onBackPressed()
        }

    }

}
