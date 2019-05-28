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
import kotlinx.android.extensions.LayoutContainer
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

/**
 * Adapter to render each Subreddit [SubmissionUiModel]
 */
class SubmissionsAdapter(private val clickListener: SubmissionItemClickListener,
                         private val longPressListener: SubmissionItemLongPressListener,
                         private val submissionUiModels: List<SubmissionUiModel>):
    RecyclerView.Adapter<SubmissionsAdapter.SubmissionViewHolder>() {

    init {
        setHasStableIds(true)
    }

    /**
     * Inflate layout and create the ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubmissionViewHolder {

        val submissionLayout = LayoutInflater.from(parent.context).inflate(R.layout.list_submission_item, parent, false)
        return SubmissionViewHolder(submissionLayout, clickListener, longPressListener)

    }

    /**
     * Set all attributes for the [SubmissionUiModel] item in the list
     */
    override fun onBindViewHolder(holder: SubmissionViewHolder, position: Int) {
        holder.submissionIndex = position
        holder.submissionTitle.text = submissionUiModels[position].title
        holder.submissionDate.text = submissionUiModels[position].date
        holder.submissionVoteCount.text = submissionUiModels[position].voteString
        holder.submissionSubreddit.text = submissionUiModels[position].subReddit
        holder.submissionTitle.requestLayout()
    }

    /**
     * Gets the item count in the list
     * @return The list item count
     */
    override fun getItemCount(): Int {
        return submissionUiModels.size
    }

    /**
     * @return The Unique ID associated with each item
     */
    override fun getItemId(position: Int): Long {

        val item = submissionUiModels[position]
        return item.hashCode().toLong()

    }

    /**
     * The ViewHolder class for each item in the list
     */
    open class SubmissionViewHolder(
        itemView: View,
        listener: SubmissionItemClickListener,
        longPressListener: SubmissionItemLongPressListener
    ): RecyclerView.ViewHolder(itemView), LayoutContainer {

        override val containerView: View?
            get() = itemView

        /*
            Lazy evaluated views to avoid null reference exceptions
         */
        val submissionTitle: TextView by lazy { itemView.findViewById<TextView>(R.id.submission_title) }
        val submissionVoteCount: TextView by lazy { itemView.findViewById<TextView>(R.id.submission_vote_count) }
        val submissionDate: TextView by lazy { itemView.findViewById<TextView>(R.id.submission_date) }
        val submissionSubreddit: TextView by lazy { itemView.findViewById<TextView>(R.id.submission_subreddit) }
        val gestureLayout: GestureActionLayout = itemView.findViewById(R.id.submission_container)

        var submissionIndex: Int by Delegates.notNull()
        var isLongPressed = false

        init {
            itemView.setOnClickListener {
                listener.onItemClick(submissionIndex)
            }
            gestureLayout.setOnLongClickListener {
                isLongPressed = true
                //gestureLayout.displayActions()
                longPressListener.onLongPress(submissionIndex)

                true
            }
            gestureLayout.setOnTouchListener { view, motionEvent ->

                if (isLongPressed) {

                    // Don't let RecyclerView steal our touch event while long pressing
                    gestureLayout.parent.requestDisallowInterceptTouchEvent(true)

                    if (motionEvent.action == MotionEvent.ACTION_UP) {

                        //gestureLayout.hideActions()
                        longPressListener.onLongPressRelease(submissionIndex)
                        isLongPressed = false
                        // Make sure we pass back control while not long pressing
                        gestureLayout.parent.requestDisallowInterceptTouchEvent(false)

                    } else if (motionEvent.action == MotionEvent.ACTION_MOVE) {

                        //gestureLayout.setAction(motionEvent.x)
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