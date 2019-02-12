package com.cniekirk.kreddit.data

object SubmissionRepository {

    fun submissions(): List<Submission> {

        return listOf(

            Submission(0, "Random reddit post 1", "Charlie", "r/android"),
            Submission(1, "Random reddit post 2", "John", "r/android"),
            Submission(2, "Random reddit post 3", "Max", "r/android"),
            Submission(3, "Random reddit post 4", "Harry", "r/android"),
            Submission(4, "Random reddit post 5", "James", "r/android")

        )

    }

}