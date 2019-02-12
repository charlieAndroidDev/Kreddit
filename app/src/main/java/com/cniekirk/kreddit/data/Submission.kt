package com.cniekirk.kreddit.data

import androidx.recyclerview.widget.DiffUtil

data class Submission(
    val id: Int,
    val title: String,
    val author: String,
    val subReddit: String
) {

    class ItemDiffer: DiffUtil.ItemCallback<Submission>() {
        override fun areContentsTheSame(oldItem: Submission, newItem: Submission) = oldItem == newItem
        override fun areItemsTheSame(oldItem: Submission, newItem: Submission) = oldItem.id == newItem.id
    }

}