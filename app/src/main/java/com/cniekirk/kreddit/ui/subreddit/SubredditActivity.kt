package com.cniekirk.kreddit.ui.subreddit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cniekirk.kreddit.R
import com.cniekirk.kreddit.core.extensions.inTransaction
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.fragment_subreddit.*
import javax.inject.Inject

class SubredditActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupSubmissionPage()

    }

    fun setupSubmissionPage() {
        var submissionFragment = supportFragmentManager.findFragmentById(submission_page.id) as FragmentSubmission?
        if(submissionFragment == null) {
            submissionFragment = FragmentSubmission()
        }

        supportFragmentManager.inTransaction {
            replace(submission_page.id, submissionFragment)
        }

    }

    override fun onBackPressed() {

        if(submission_page.isExpandedOrExpanding) {
            submissions_list.collapse()
        } else {
            super.onBackPressed()
        }

    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

}
