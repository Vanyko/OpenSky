package com.vanyko.opensky.ui.utils

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class TickHandler constructor(
    private val tickIntervalMs: Long = 10000, // 10 s,
    private val externalScope: CoroutineScope,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    private val _tickFlow = MutableSharedFlow<Unit>(replay = 0)
    val tickFlow: SharedFlow<Unit> = _tickFlow

    init {
        externalScope.launch(defaultDispatcher) {
            while(true) {
                _tickFlow.emit(Unit)
                delay(tickIntervalMs)
            }
        }
    }
}