package com.cniekirk.kreddit.ui.subreddit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cniekirk.kreddit.R
import com.cniekirk.kreddit.data.Submission
import kotlin.properties.Delegates

class SubmissionsAdapter(private val clickListener: SubmissionItemClickListener):
    ListAdapter<Submission, SubmissionViewHolder>(Submission.ItemDiffer()) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubmissionViewHolder {

        val submissionLayout = LayoutInflater.from(parent.context).inflate(R.layout.list_submission, parent, false)
        return SubmissionViewHolder(submissionLayout, clickListener)

    }

    override fun onBindViewHolder(holder: SubmissionViewHolder, position: Int) {
        holder.submissionIndex = position
        holder.submissionTitle.text = getItem(position).title
        holder.submissionAuthor.text = getItem(position).author
        holder.submissionSubreddit.text = getItem(position).subReddit
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}

open class SubmissionViewHolder(
    itemView: View,
    listener: SubmissionItemClickListener
): RecyclerView.ViewHolder(itemView) {

    /*
        Lazy evaluated views to avoid null reference exceptions
     */
    val submissionTitle: TextView by lazy { itemView.findViewById<TextView>(R.id.submission_title) }
    val submissionAuthor: TextView by lazy { itemView.findViewById<TextView>(R.id.submission_author) }
    val submissionSubreddit: TextView by lazy { itemView.findViewById<TextView>(R.id.submission_subreddit) }

    var submissionIndex: Int by Delegates.notNull()

    init {
        itemView.setOnClickListener {
            listener.onItemClick(submissionIndex)
        }
    }


}