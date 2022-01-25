package com.kystudio.jobschedulerdemo

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import com.kystudio.jobschedulerdemo.utils.AppFrontBackHelper

class App : Application() {

    companion object {

    }

    override fun onCreate() {
        super.onCreate()

        JobSchedulerManager.init(this)
        JobSchedulerManager.addJob(this, JobSchedulerManager.JobType.RESET, 60000)
        JobSchedulerManager.addJob(this, JobSchedulerManager.JobType.SYNC, 30000)

        val screenReceiver = ScreenReceiver()
        val screenFilter = IntentFilter()
        screenFilter.addAction(Intent.ACTION_SCREEN_ON)
        screenFilter.addAction(Intent.ACTION_SCREEN_OFF)
        screenFilter.addAction(Intent.ACTION_USER_PRESENT)
        screenFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        screenFilter.priority = IntentFilter.SYSTEM_HIGH_PRIORITY
        registerReceiver(screenReceiver, screenFilter)

        AppFrontBackHelper.instance.register(this, object : AppFrontBackHelper.OnAppStatusListener {
            override fun onFront(activity: Activity) {
                appState = AppState.Foreground
                JobSchedulerManager.cancelJob(JobSchedulerManager.JobType.COUNT)
            }

            override fun onBack(activity: Activity) {
                appState = AppState.Background
                JobSchedulerManager.addJob(this@App, JobSchedulerManager.JobType.COUNT, 180000)
            }

        })
    }
}