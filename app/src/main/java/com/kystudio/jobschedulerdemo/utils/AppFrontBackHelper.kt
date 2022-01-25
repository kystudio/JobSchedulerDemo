package com.kystudio.jobschedulerdemo.utils

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle

/**
 * 应用前后台状态监听帮助类，仅在Application中使用
 */
class AppFrontBackHelper private constructor() {
    private var mOnAppStatusListener: OnAppStatusListener? = null
    private val activityLifecycleCallbacks: ActivityLifecycleCallbacks =
        object : ActivityLifecycleCallbacks {
            //打开的Activity数量统计
            private var activityStartCount = 0
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                ActivityManager.instance.addActivity(activity)
            }

            override fun onActivityStarted(activity: Activity) {
                activityStartCount++
                //数值从0变到1说明是从后台切到前台
                if (activityStartCount == 1) {
                    //从后台切到前台
                    mOnAppStatusListener?.onFront(activity)
                }
            }

            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {
//                if (activity.javaClass.name == AdManager.getInstance().fullscreenVideoActivityName) {
//                    activity.finish()
//                }
//                if (activity.javaClass.name == AdManager.getInstance().rewardVideoActivityName) {
//                    activity.finish()
//                }
            }

            override fun onActivityStopped(activity: Activity) {
                activityStartCount--
                //数值从1到0说明是从前台切到后台
                if (activityStartCount == 0) {
                    //从前台切到后台
                    mOnAppStatusListener?.onBack(activity)
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {
                ActivityManager.instance.finishActivity(activity)
            }
        }

    /**
     * 注册状态监听，仅在Application中使用
     */
    fun register(application: Application, listener: OnAppStatusListener?) {
        mOnAppStatusListener = listener
        application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }

    interface OnAppStatusListener {
        fun onFront(activity: Activity)
        fun onBack(activity: Activity)
    }

    companion object {
        val instance: AppFrontBackHelper
            get() = AppFrontBackHelper()
    }
}