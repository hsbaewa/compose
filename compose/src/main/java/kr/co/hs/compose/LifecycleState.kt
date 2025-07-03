package kr.co.hs.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.StateFlow

@Composable
fun StateFlow<Lifecycle.State>.collectAsState(
    onCreate: (Lifecycle.State) -> Unit = {},
    onStart: (Lifecycle.State) -> Unit = {},
    onRestart: (Lifecycle.State) -> Unit = {},
    onResume: (Lifecycle.State) -> Unit = {},
    onPause: (Lifecycle.State) -> Unit = {},
    onStop: (Lifecycle.State) -> Unit = {},
    onDestroy: (Lifecycle.State) -> Unit = {},
): State<Lifecycle.State> {
    val state = collectAsState()
    var lastState by remember { mutableStateOf<Lifecycle.State?>(null) }
    LaunchedEffect(state.value) {

        val current = state.value
        val last = lastState ?: Lifecycle.State.DESTROYED

        if (last < current) {
            when {
                current.isAtLeast(Lifecycle.State.RESUMED) -> onResume(current)
                current.isAtLeast(Lifecycle.State.STARTED) -> onStart(current)
                current.isAtLeast(Lifecycle.State.CREATED) -> onCreate(current)
            }

            if (last == Lifecycle.State.CREATED) {
                onRestart(current)
            }
        } else {
            when {
                current.isEqualsOrBelow(Lifecycle.State.CREATED) -> {
//                    onDestroy(current)
                }

                current.isEqualsOrBelow(Lifecycle.State.STARTED) -> onStop(current)
                current.isEqualsOrBelow(Lifecycle.State.RESUMED) -> onPause(current)
            }
        }

        lastState = current
    }

    return state
}

private fun Lifecycle.State.isEqualsOrBelow(state: Lifecycle.State): Boolean {
    return compareTo(state) <= 0
}

@Deprecated("use collectAsState(onCreated: onStarted: onRestarted: onResumed:)")
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

@Deprecated("use collectAsState(onCreated: onStarted: onRestarted: onResumed:)")
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