package com.kystudio.jobschedulerdemo

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.os.Message


class JobSchedulerService : JobService() {
    override fun onStartJob(params: JobParameters?): Boolean {
        System.err.println("job start")
        val type = params?.extras?.getInt("JOB_TYPE") ?: 0
        val duration = params?.extras?.getLong("JOB_DURATION") ?: 5000
        System.err.println("job start type: ${JobSchedulerManager.JobType.values()[type]}, duration: $duration")
        // 返回true，表示该工作耗时，同时工作处理完成后需要调用jobFinished销毁
//        mJobHandler.sendMessage(Message.obtain(mJobHandler, 1, params))
        jobHandler.sendMessageDelayed(Message.obtain(jobHandler, type, params), duration)

        if (JobSchedulerManager.JobType.DELAY.id == type) {
            System.err.println("job stop by delay")
            JobSchedulerManager.cancelJob(JobSchedulerManager.JobType.COUNT)
        }

        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        System.err.println("job stop")
        val type = params?.extras?.getInt("JOB_TYPE") ?: 0
        System.err.println("job stop type: ${JobSchedulerManager.JobType.values()[type]}")
        jobHandler.removeMessages(type)
        return false
    }

    // 创建一个handler来处理对应的job
    private val jobHandler: Handler = Handler(Looper.getMainLooper()) { msg ->
        System.err.println("job running")
        val type = msg.what
        val jobParameters = msg.obj as JobParameters?
        val duration = jobParameters?.extras?.getLong("JOB_DURATION") ?: 5000
        System.err.println("job running type: ${JobSchedulerManager.JobType.values()[type]}")

        // 在Handler中，需要实现handleMessage(Message msg)方法来处理任务逻辑。
//        Toast.makeText(applicationContext, "JobService task running", Toast.LENGTH_SHORT).show()
//        if (JobSchedulerManager.JobType.SYNC.type == type) {
//            System.err.println("job restart sync")
//            JobSchedulerManager.addJob(this, JobSchedulerManager.JobType.SYNC, duration)
//        }
//        if (JobSchedulerManager.JobType.RESET.type == type) {
//            System.err.println("job restart reset")
//            JobSchedulerManager.addJob(this, JobSchedulerManager.JobType.RESET, duration)
//        }

        if (appState == AppState.Background) {
            System.err.println("job Background")

            if (JobSchedulerManager.JobType.DELAY.id == type) {
                System.err.println("job start by delay")
                JobSchedulerManager.addJob(this, JobSchedulerManager.JobType.COUNT, 100000)
            }
            if (JobSchedulerManager.JobType.COUNT.id == type) {
                System.err.println("job Background1")
                val intent = Intent(this, CleanDialogActivity::class.java)
                System.err.println("job Background2")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                System.err.println("job Background3")
                startActivity(intent)
                System.err.println("job Background4")
            }
        } else {
            System.err.println("job Foreground")
        }
        // 调用jobFinished
        jobFinished(jobParameters, false)
        true
    }
}