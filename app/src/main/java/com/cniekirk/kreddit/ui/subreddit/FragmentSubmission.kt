package com.cniekirk.kreddit.ui.subreddit

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.TransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.cniekirk.kreddit.R
import com.cniekirk.kreddit.core.extensions.isImage
import com.cniekirk.kreddit.ui.subreddit.uimodel.SubmissionUiModel
import kotlinx.android.synthetic.main.fragment_submission.*
import me.saket.inboxrecyclerview.globalVisibleRect
import me.saket.inboxrecyclerview.page.ExpandablePageLayout
import me.saket.inboxrecyclerview.page.InterceptResult
import ru.noties.markwon.Markwon

class FragmentSubmission: Fragment() {

    private val submissionPage by lazy { view!!.parent as ExpandablePageLayout }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_submission, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        close_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        submissionPage.pullToCollapseInterceptor = { downX, downY, upwardPull ->
            if (submission_scrollable_container.globalVisibleRect().contains(downX, downY).not()) {
                InterceptResult.IGNORED
            }

            val directionInt = if (upwardPull) +1 else -1
            val canScrollFurther = submission_scrollable_container.canScrollVertically(directionInt)
            when {
                canScrollFurther -> InterceptResult.INTERCEPTED
                else -> InterceptResult.IGNORED
            }
        }

    }

    fun populateUi(submissionUiModel: SubmissionUiModel) {

        val constraintSet = ConstraintSet()
        constraintSet.clone(submission_layout)

        val marginPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            8f,
            resources.displayMetrics
        )

        Log.d("FRAGMENT", "Margin: ${marginPx.toInt()}")

        constraintSet.connect(R.id.submission_title, ConstraintSet.TOP, R.id.close_btn, ConstraintSet.BOTTOM, marginPx.toInt())
        constraintSet.applyTo(submission_layout)

        // Reset so no image is displayed
        submission_image.visibility = View.GONE
        submission_content_container.visibility = View.VISIBLE
        submission_title.text = submissionUiModel.title

        // Need to do this async
        submissionUiModel.content?.let {
            Markwon.setMarkdown(submission_content, it)
        }

        submissionUiModel.url?.let {

            if (it.isImage()) {

                submission_image.visibility = View.VISIBLE
                Glide.with(requireContext()).load(it)
                    .transition(DrawableTransitionOptions.withCrossFade()).into(submission_image)
                submission_content_container.visibility = View.GONE

                //val imageConstraintSet = ConstraintSet()
                constraintSet.clone(submission_layout)
                constraintSet.connect(R.id.submission_title, ConstraintSet.TOP, R.id.submission_image, ConstraintSet.BOTTOM, marginPx.toInt())
                constraintSet.applyTo(submission_layout)

            }
        }

    }

}