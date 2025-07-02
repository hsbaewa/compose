package kr.co.hs.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

@Composable
fun LifecycleOwner.rememberLifecycleState(): LifecycleState {
    val lifecycleState = rememberSaveable(
        saver = LifecycleState.Saver,
        init = { LifecycleState() }
    )

    val state by lifecycle.currentStateFlow.collectAsState()

    val isPaused by remember { derivedStateOf { !state.isAtLeast(Lifecycle.State.STARTED) } }

    val isStarted by remember { derivedStateOf { state.isAtLeast(Lifecycle.State.STARTED) } }
    val isResumed by remember { derivedStateOf { state.isAtLeast(Lifecycle.State.RESUMED) } }
    val hasSeenResumed by remember {
        derivedStateOf {
            var seen = false
            if (state.isAtLeast(Lifecycle.State.RESUMED)) seen = true
            seen
        }
    }

    LaunchedEffect(isPaused) { lifecycleState.isPaused = isPaused }
    LaunchedEffect(isStarted) {
        with(lifecycleState) {
            this.isStarted = isStarted
            isRestarted = isStarted && hasSeenResumed
        }
    }
    LaunchedEffect(isResumed) { lifecycleState.isResumed = isResumed }

    return lifecycleState
}

class LifecycleState(
    isPaused: Boolean = false,
    isStarted: Boolean = false,
    isRestarted: Boolean = false,
    isResumed: Boolean = false,
) {
    private val stateOfPaused = mutableStateOf(isPaused)
    var isPaused: Boolean
        get() = stateOfPaused.value
        set(value) {
            stateOfPaused.value = value
        }

    private val stateOfStarted = mutableStateOf(isStarted)
    var isStarted: Boolean
        get() = stateOfStarted.value
        set(value) {
            stateOfStarted.value = value
        }

    private val stateOfRestarted = mutableStateOf(isRestarted)
    var isRestarted: Boolean
        get() = stateOfRestarted.value
        set(value) {
            stateOfRestarted.value = value
        }

    private val stateOfResumed = mutableStateOf(isResumed)
    var isResumed: Boolean
        get() = stateOfResumed.value
        set(value) {
            stateOfResumed.value = value
        }

    companion object {
        val Saver: Saver<LifecycleState, *> = Saver(
            save = {
                mapOf(
                    "paused" to it.isPaused,

                    "started" to it.isStarted,
                    "restarted" to it.isRestarted,
                    "resumed" to it.isResumed
                )
            },
            restore = {
                LifecycleState(
                    isPaused = it["paused"] ?: false,

                    isStarted = it["started"] ?: false,
                    isRestarted = it["restarted"] ?: false,
                    isResumed = it["resumed"] ?: false
                )
            }
        )
    }
}