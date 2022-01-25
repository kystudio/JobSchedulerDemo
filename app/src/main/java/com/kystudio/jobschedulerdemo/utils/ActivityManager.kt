package com.kystudio.jobschedulerdemo.utils

import android.app.Activity
import java.util.concurrent.CopyOnWriteArrayList

class ActivityManager private constructor() {
    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity?) {
        activityStack.add(activity)
    }

    /**
     * 是否包含指定Activity
     */
    fun containsActivity(activity: Activity?): Boolean {
        return if (activity != null && activityStack.size >= 1) {
            activityStack.contains(activity)
        } else false
    }

    /**
     * 是否包含指定Class
     */
    fun containsActivity(cls: Class<*>): Boolean {
        if (activityStack.size >= 1) {
            for (activity in activityStack) {
                if (activity!!.javaClass == cls) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 是否包含指定类名的Activity
     */
    fun containsActivity(clsName: String): Boolean {
        if (activityStack.size >= 1) {
            for (activity in activityStack) {
                if (activity!!.javaClass.name == clsName) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity?) {
        if (activity != null) {
            activityStack.remove(activity)
            activity.finish()
        }
    }

    /**
     * 结束指定Class
     */
    fun finishActivity(cls: Class<*>) {
        for (activity in activityStack) {
            if (activity!!.javaClass == cls) {
                finishActivity(activity)
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(clsName: String) {
        for (activity in activityStack) {
            if (activity!!.javaClass.name == clsName) {
                finishActivity(activity)
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        if (activityStack.size < 1) {
            return
        }
        var i = 0
        val size = activityStack.size
        while (i < size) {
            if (null != activityStack[i]) {
                activityStack[i]!!.finish()
            }
            i++
        }
        activityStack.clear()
    }

    companion object {
        val instance: ActivityManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { ActivityManager() }
        private val activityStack: MutableList<Activity?> by lazy { CopyOnWriteArrayList() }
    }
}