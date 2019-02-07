package com.cniekirk.kreddit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import me.saket.inboxrecyclerview.InboxRecyclerView
import me.saket.inboxrecyclerview.page.ExpandablePageLayout

class MainActivity : AppCompatActivity() {

    private val submissionsList = findViewById<InboxRecyclerView>(R.id.submissions_list)
    private val submissionPageLayout = findViewById<ExpandablePageLayout>(R.id.submission_page)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

}
