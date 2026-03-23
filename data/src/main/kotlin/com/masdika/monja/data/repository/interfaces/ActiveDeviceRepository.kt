package com.masdika.monja.data.repository.interfaces

import kotlinx.coroutines.flow.StateFlow

interface ActiveDeviceRepository {
    val activeMacAddress: StateFlow<String?>
    fun setActiveDevice(macAddress: String)
}