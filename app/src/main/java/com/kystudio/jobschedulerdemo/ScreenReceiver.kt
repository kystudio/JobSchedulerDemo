package com.kystudio.jobschedulerdemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ScreenReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_SCREEN_ON -> {
                System.err.println("job screen on")
            }
            Intent.ACTION_SCREEN_OFF -> {
                System.err.println("job screen off")
                JobSchedulerManager.cancelJob(JobSchedulerManager.JobType.COUNT)
            }
            Intent.ACTION_USER_PRESENT -> {
                System.err.println("job user present")
                JobSchedulerManager.addJob(context!!, JobSchedulerManager.JobType.COUNT, 100000)
            }
        }
    }
}