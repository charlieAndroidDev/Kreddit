package com.cniekirk.kreddit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.cniekirk.kreddit.data.SubmissionRepository
import com.cniekirk.kreddit.ui.FragmentSubmission
import com.cniekirk.kreddit.ui.SubmissionItemClickListener
import com.cniekirk.kreddit.ui.SubmissionsAdapter
import kotlinx.android.synthetic.main.activity_main.*
import me.saket.inboxrecyclerview.InboxRecyclerView
import me.saket.inboxrecyclerview.page.ExpandablePageLayout

class MainActivity : AppCompatActivity(), SubmissionItemClickListener {

    //private val submissionsList = findViewById<InboxRecyclerView>(R.id.submissions_list)
    //private val submissionPageLayout = findViewById<ExpandablePageLayout>(R.id.submission_page)

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

        submissions_list.layoutManager = LinearLayoutManager(this)
        submissions_list.setExpandablePage(submission_page)

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
