package com.cniekirk.kreddit.ui.subreddit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cniekirk.kreddit.R
import com.cniekirk.kreddit.data.Submission
import com.cniekirk.kreddit.di.Injectable
import com.cniekirk.kreddit.utils.AppViewModelFactory
import kotlinx.android.synthetic.main.fragment_subreddit.*
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

class SubredditFragment: Fragment(), Injectable, SubmissionItemClickListener {

    @Inject
    private lateinit var viewModelFactory: AppViewModelFactory
    private lateinit var subredditViewModel: SubredditViewModel

    private var submissionsAdapter = SubmissionsAdapter(this, emptyList())

    companion object {
        val instance: SubredditFragment by lazy(NONE) { SubredditFragment() }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_subreddit, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        subredditViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(SubredditViewModel::class.java)
        subredditViewModel.submissions.observe(this, Observer { updateSubmissionsList(it) })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSubmissionList()
        (requireActivity() as SubredditActivity).setupSubmissionPage()
    }

    private fun updateSubmissionsList(submissions: List<Submission>) {

        submissionsAdapter = SubmissionsAdapter(this, submissions)
        submissions_list.adapter = submissionsAdapter

    }

    private fun setupSubmissionList() {

        val layoutManager = LinearLayoutManager(requireContext())
        submissions_list.layoutManager = layoutManager
        submissions_list.setExpandablePage(submission_page)
        submissions_list.addItemDecoration(DividerItemDecoration(requireContext(), layoutManager.orientation))
        submissions_list.adapter = submissionsAdapter

    }

    override fun onItemClick(submission: Int) {
        submissions_list.expandItem(submission.toLong())
    }

}