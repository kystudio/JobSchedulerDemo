package com.kystudio.jobschedulerdemo

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.os.PersistableBundle
import android.util.Log

object JobSchedulerManager {

    private const val TAG = "JobSchedulerManager"

    private var jobScheduler: JobScheduler? = null

    enum class JobType(val id: Int) {
        COUNT(0),
        DELAY(1),
        RESET(2),
        SYNC(3),
    }

    fun init(context: Context) {
        jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
    }

    fun addJob(context: Context, jobType: JobType, jobDuration: Long) {
        if (null == jobScheduler)
            return

        Log.i(TAG, "添加任务类型 : ${jobType.name}")

        var pendingJob: JobInfo? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            pendingJob = jobScheduler!!.getPendingJob(jobType.id)
        } else {
            val allPendingJobs = jobScheduler!!.allPendingJobs
            for (info in allPendingJobs) {
                if (info.id == jobType.id) {
                    pendingJob = info
                    break
                }
            }
        }

        if (pendingJob != null) {
            jobScheduler!!.cancel(jobType.id)
        }

        val extras = PersistableBundle()
        extras.putInt("JOB_TYPE", jobType.id)
        extras.putLong("JOB_DURATION", jobDuration)

        //创建一个任务
        val jobInfo = JobInfo.Builder(
            jobType.id, ComponentName(context, JobSchedulerService::class.java)
        )
            .setOverrideDeadline(60000)
            .setRequiresCharging(false)  // 是否要求在充电时执行
            .setRequiresDeviceIdle(false) // 是否要求在没有使用该设备时执行
            .setPersisted(true) // 设备重启后是否继续执行
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE) // 网络条件，默认值 NETWORK_TYPE_NONE
            .setExtras(extras)
            .build()

        // 将任务提交到队列中
        jobScheduler!!.schedule(jobInfo)
    }

    fun cancelJob(jobType: JobType) {
        if (null == jobScheduler)
            return

        jobScheduler!!.cancel(jobType.id)
    }

    fun cancelAllJob() {
        if (null == jobScheduler)
            return

        jobScheduler!!.cancelAll()
    }
}