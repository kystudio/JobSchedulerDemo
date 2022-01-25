package com.kystudio.jobschedulerdemo

import android.Manifest
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private val permissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (!it[Manifest.permission.READ_EXTERNAL_STORAGE]!! or !it[Manifest.permission.WRITE_EXTERNAL_STORAGE]!!) {
                Toast.makeText(this, "未完全授权读写权限", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissions.launch(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )

        findViewById<TextView>(R.id.tv_start_count).setOnClickListener {
            JobSchedulerManager.addJob(this@MainActivity, JobSchedulerManager.JobType.COUNT, 120000)
        }

        findViewById<TextView>(R.id.tv_stop_count).setOnClickListener {
            JobSchedulerManager.cancelJob(JobSchedulerManager.JobType.COUNT)
        }

        findViewById<TextView>(R.id.tv_start_delay).setOnClickListener {
            JobSchedulerManager.addJob(this@MainActivity, JobSchedulerManager.JobType.DELAY, 65000)
        }

        findViewById<TextView>(R.id.tv_stop_delay).setOnClickListener {
            JobSchedulerManager.cancelJob(JobSchedulerManager.JobType.DELAY)
        }

        findViewById<TextView>(R.id.tv_start_reset).setOnClickListener {
            JobSchedulerManager.addJob(this@MainActivity, JobSchedulerManager.JobType.RESET, 60000)
        }

        findViewById<TextView>(R.id.tv_stop_reset).setOnClickListener {
            JobSchedulerManager.cancelJob(JobSchedulerManager.JobType.RESET)
        }

        findViewById<TextView>(R.id.tv_start_sync).setOnClickListener {
            JobSchedulerManager.addJob(this@MainActivity, JobSchedulerManager.JobType.SYNC, 80000)
        }

        findViewById<TextView>(R.id.tv_stop_sync).setOnClickListener {
            JobSchedulerManager.cancelJob(JobSchedulerManager.JobType.SYNC)
        }
    }
}