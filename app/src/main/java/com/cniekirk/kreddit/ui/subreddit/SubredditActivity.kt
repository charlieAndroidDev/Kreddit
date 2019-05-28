package com.cniekirk.kreddit.ui.subreddit

import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet.*
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.cniekirk.kreddit.R
import com.cniekirk.kreddit.core.extensions.*
import com.cniekirk.kreddit.core.extensions.inTransaction
import com.cniekirk.kreddit.ui.settings.SettingsActivity
import com.cniekirk.kreddit.ui.submission.FragmentSubmission
import com.cniekirk.kreddit.ui.subreddit.uimodel.SubmissionUiModel
import com.cniekirk.kreddit.ui.subreddit.uimodel.SubredditRequests
import com.cniekirk.kreddit.utils.Blur
import com.cniekirk.kreddit.viewmodel.AppViewModelFactory
import com.cniekirk.kreddit.utils.animation.SubmissionListItemAnimtor
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.lang.Exception
import javax.inject.Inject

/**
 * The main Activity of the application
 *
 * A list of Subreddit submissions are displayed in a list and expanded into the [submissionFragment] on click
 */
@ExperimentalCoroutinesApi
class SubredditActivity : AppCompatActivity(), HasSupportFragmentInjector, SubmissionItemClickListener,
    SubmissionItemLongPressListener {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    private var submissionFragment = FragmentSubmission()
    private var submissionsAdapter = SubmissionsAdapter(this, this, emptyList())
    private lateinit var subredditViewModel: SubredditViewModel

    private var leftBoundary: Int = 0
    private var middleLeftBoundary: Int = 0
    private var middleRightBoundary: Int = 0
    private lateinit var selectedAction: ImageView

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

        submissionsAdapter = SubmissionsAdapter(this, this, submissionUiModels)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.settings -> {
                startActivity<SettingsActivity>()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    /**
     * Handle Submission item clicks
     */
    override fun onItemClick(submission: Int) {
        subredditViewModel.clickSubmission(submission)
        submissions_list.expandItem(submissionsAdapter.getItemId(submission))
    }

    override fun onLongPress(position: Int) {

        leftBoundary = btn_upvote.right + ((btn_downvote.left - btn_upvote.right) / 2)
        middleLeftBoundary = btn_downvote.right + ((btn_favorite.left - btn_downvote.right) / 2)
        middleRightBoundary = btn_favorite.right + ((btn_options.left - btn_favorite.right) / 2)

        val viewHolder = getViewHolderAt(position) ?: return
        val view = viewHolder.gestureLayout

        drawViewIntoPlaceholder(view)
        view.alpha = 0f

        val startMargin = resources.getDimensionPixelSize(R.dimen.submission_content_horizontal_margin)
        val topOffset = viewHolder.itemView.top
        val height = viewHolder.itemView.height
        val placeholderBottom = submissions_list.height - (height + topOffset)

        actions_blur.setImageDrawable(Blur.createBlur(this, submissions_list))

        submission_actions_scene_view.apply {

            val cs = getConstraintSet(R.id.actions_hidden)

            cs.constrainWidth(R.id.motion_submission_content, MATCH_PARENT)
            cs.constrainHeight(R.id.motion_submission_content, WRAP_CONTENT)

            if (topOffset > 0) {
                cs.setMargin(R.id.motion_submission_content, TOP, topOffset)
                cs.setMargin(R.id.motion_submission_content, BOTTOM, 0)
                cs.setVerticalBias(R.id.motion_submission_content, 0f)
            } else {
                cs.setMargin(R.id.motion_submission_content, BOTTOM, placeholderBottom)
                cs.setMargin(R.id.motion_submission_content, TOP, 0)
                cs.setVerticalBias(R.id.motion_submission_content, 1f)
            }
            cs.connect(R.id.motion_submission_content, START, R.id.submission_actions_scene_view, START, startMargin)
            cs.connect(R.id.motion_submission_content, END, R.id.submission_actions_scene_view, END, startMargin)

            updateState(R.id.actions_hidden, cs)

            setTransition(R.id.actions_hidden, R.id.actions_shown)
            motion_submission_content.isVisible = true
            btn_upvote.isVisible = true
            btn_downvote.isVisible = true
            btn_favorite.isVisible = true
            btn_options.isVisible = true
            transitionToEnd()

        }

    }

    override fun onLongPressRelease(position: Int) {

        submission_actions_scene_view.after {

            submission_actions_scene_view.stopListening()
            actions_blur.setOnTouchListener { _,_ ->
                false
            }
            val viewHolder = getViewHolderAt(position) ?: return@after
            viewHolder.gestureLayout.alpha = 1f
            motion_submission_content.isVisible = false
            btn_upvote.isVisible = false
            btn_downvote.isVisible = false
            btn_favorite.isVisible = false
            btn_options.isVisible = false

        }

        submission_actions_scene_view.apply {

            setTransition(R.id.actions_shown, R.id.actions_hidden)
            transitionToEnd()

        }

    }

    override fun onLongPressDrag(touchX: Int) {

        when (touchX) {
            in 0..leftBoundary -> {
                val color = ContextCompat.getColor(this, R.color.colorRedditUpvote)
                btn_upvote.switchImageAnim(R.drawable.ic_up_arrow_filled, color)
                //selectedAction.switchImageAnim(selectedAction.)
            }
            in leftBoundary..middleLeftBoundary -> {

            }
            in middleLeftBoundary..middleRightBoundary -> {

            }
            in middleRightBoundary..root.measuredWidth -> {

            }
        }

    }

    private fun getViewHolderAt(position: Int): SubmissionsAdapter.SubmissionViewHolder? {
        val itemCount = submissions_list?.adapter?.itemCount ?: return null
        if (itemCount <= 0) return null
        return getProfileViewHolder(position)
    }

    private fun getProfileViewHolder(position: Int): SubmissionsAdapter.SubmissionViewHolder? {


        val view = (submissions_list?.layoutManager)?.findViewByPosition(position) ?: return null

        return try {
            submissions_list?.getChildViewHolder(view) as SubmissionsAdapter.SubmissionViewHolder
        } catch (ex: IllegalArgumentException) {
            // The given view is not a child of the RecyclerView. This is possible during onPause
            null
        }
    }

    private fun drawViewIntoPlaceholder(view: View) {

        view.apply {

            isDrawingCacheEnabled = true

            val cachedBitmap = try {
                drawingCache ?: return
            } catch (ex: Exception) {
                return
            }

            val bmp = try {
                Bitmap.createBitmap(cachedBitmap)
            } catch (ex: OutOfMemoryError) {
                Runtime.getRuntime().gc()
                null
            }

            motion_submission_content.setImageBitmap(bmp)

            isDrawingCacheEnabled = false

        }

    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

}
