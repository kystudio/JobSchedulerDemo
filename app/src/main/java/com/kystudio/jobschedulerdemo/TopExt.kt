package com.kystudio.jobschedulerdemo

sealed class AppState {
    object Foreground : AppState()
    object Background : AppState()
}

var appState: AppState = AppState.Foreground