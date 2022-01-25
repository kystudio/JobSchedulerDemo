package com.kystudio.jobschedulerdemo

import android.app.Activity
import android.os.Bundle
import android.widget.ImageView

class CleanDialogActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clean_dialog)

        findViewById<ImageView>(R.id.ivClose).setOnClickListener {
            finish()
        }
    }
}