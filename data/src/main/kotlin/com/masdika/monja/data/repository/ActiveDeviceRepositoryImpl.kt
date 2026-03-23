package com.masdika.monja.data.repository

import com.masdika.monja.data.repository.interfaces.ActiveDeviceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActiveDeviceRepositoryImpl @Inject constructor() : ActiveDeviceRepository {
    private val _activeMacAddress = MutableStateFlow<String?>(null)

    override val activeMacAddress: StateFlow<String?> = _activeMacAddress.asStateFlow()

    override fun setActiveDevice(macAddress: String) {
        _activeMacAddress.value = macAddress
    }
}