package com.cniekirk.kreddit.ui.subreddit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import com.cniekirk.kreddit.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_submission.*
import me.saket.inboxrecyclerview.globalVisibleRect
import me.saket.inboxrecyclerview.page.ExpandablePageLayout
import me.saket.inboxrecyclerview.page.InterceptResult

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

}