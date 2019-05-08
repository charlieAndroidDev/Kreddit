package com.cniekirk.kreddit.ui.subreddit

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cniekirk.kreddit.R
import com.cniekirk.kreddit.ui.subreddit.uimodel.SubmissionUiModel
import com.cniekirk.kreddit.widgets.gesture.GestureAction
import com.cniekirk.kreddit.widgets.gesture.GestureActionLayout
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class SubmissionsAdapter(private val clickListener: SubmissionItemClickListener,
                         private val submissionUiModels: List<SubmissionUiModel>):
    RecyclerView.Adapter<SubmissionsAdapter.SubmissionViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubmissionViewHolder {

        val submissionLayout = LayoutInflater.from(parent.context).inflate(R.layout.list_submission_item, parent, false)
        return SubmissionViewHolder(submissionLayout, clickListener)

    }
    
    override fun onBindViewHolder(holder: SubmissionViewHolder, position: Int) {
        holder.submissionIndex = position
        holder.submissionTitle.text = submissionUiModels[position].title
        holder.submissionDate.text = submissionUiModels[position].date
        holder.submissionVoteCount.text = submissionUiModels[position].voteString
        holder.submissionSubreddit.text = submissionUiModels[position].subReddit
        holder.submissionTitle.requestLayout()
    }

    override fun getItemCount(): Int {
        return submissionUiModels.size
    }

    override fun getItemId(position: Int): Long {

        val item = submissionUiModels[position]
        return item.hashCode().toLong()

    }

    open class SubmissionViewHolder(
        itemView: View,
        listener: SubmissionItemClickListener
    ): RecyclerView.ViewHolder(itemView) {

        /*
            Lazy evaluated views to avoid null reference exceptions
         */
        val submissionTitle: TextView by lazy { itemView.findViewById<TextView>(R.id.submission_title) }
        val submissionVoteCount: TextView by lazy { itemView.findViewById<TextView>(R.id.submission_vote_count) }
        val submissionDate: TextView by lazy { itemView.findViewById<TextView>(R.id.submission_date) }
        val submissionSubreddit: TextView by lazy { itemView.findViewById<TextView>(R.id.submission_subreddit) }
        private val gestureLayout: GestureActionLayout = itemView.findViewById(R.id.submission_container)

        var submissionIndex: Int by Delegates.notNull()
        var isLongPressed = false

        init {
            itemView.setOnClickListener {
                listener.onItemClick(submissionIndex)
            }
            gestureLayout.setOnLongClickListener {
                isLongPressed = true
                gestureLayout.displayActions()

                true
            }
            gestureLayout.setOnTouchListener { view, motionEvent ->

                if (isLongPressed) {

                    // Don't let RecyclerView steal our touch event while long pressing
                    gestureLayout.parent.requestDisallowInterceptTouchEvent(true)

                    if (motionEvent.action == MotionEvent.ACTION_UP) {

                        gestureLayout.hideActions()
                        isLongPressed = false
                        // Make sure we pass back control while not long pressing
                        gestureLayout.parent.requestDisallowInterceptTouchEvent(false)

                    } else if (motionEvent.action == MotionEvent.ACTION_MOVE) {

                        gestureLayout.setAction(motionEvent.x)
                        view.onTouchEvent(motionEvent)

                    } else {

                        view.onTouchEvent(motionEvent)

                    }

                } else {

                    view.onTouchEvent(motionEvent)

                }

                true
            }

            val upvote = gestureLayout.resources.getDrawable(R.drawable.ic_upvote, null)
            val downvote = gestureLayout.resources.getDrawable(R.drawable.ic_downvote, null)
            val favourite = gestureLayout.resources.getDrawable(R.drawable.ic_favourite, null)
            val reply = gestureLayout.resources.getDrawable(R.drawable.ic_reply, null)

            gestureLayout.setGestureActions(listOf(GestureAction(upvote, ContextCompat.getColor(gestureLayout.context, R.color.colorGreen)),
                GestureAction(downvote, ContextCompat.getColor(gestureLayout.context, R.color.colorRed)),
                GestureAction(favourite, ContextCompat.getColor(gestureLayout.context, R.color.colorGold)),
                GestureAction(reply, ContextCompat.getColor(gestureLayout.context, R.color.colorBlue))))

        }


    }

}