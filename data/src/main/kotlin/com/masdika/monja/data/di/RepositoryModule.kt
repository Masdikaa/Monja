package com.masdika.monja.data.di

import com.masdika.monja.data.repository.DeviceRepositoryImpl
import com.masdika.monja.data.repository.HealthStatusRepositoryImpl
import com.masdika.monja.data.repository.LocationRepositoryImpl
import com.masdika.monja.data.repository.MedicalAlertsRepositoryImpl
import com.masdika.monja.data.repository.VitalRepositoryImpl
import com.masdika.monja.data.repository.interfaces.DeviceRepository
import com.masdika.monja.data.repository.interfaces.HealthStatusRepository
import com.masdika.monja.data.repository.interfaces.LocationRepository
import com.masdika.monja.data.repository.interfaces.MedicalAlertsRepository
import com.masdika.monja.data.repository.interfaces.VitalsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindDeviceRepository(
        deviceRepositoryImpl: DeviceRepositoryImpl
    ): DeviceRepository

    @Binds
    @Singleton
    abstract fun bindVitalRepository(
        vitalRepositoryImpl: VitalRepositoryImpl
    ): VitalsRepository

    @Binds
    @Singleton
    abstract fun bindLocationRepository(
        locationRepositoryImpl: LocationRepositoryImpl
    ): LocationRepository

    @Binds
    @Singleton
    abstract fun bindHealthStatusRepository(
        healthStatusRepositoryImpl: HealthStatusRepositoryImpl
    ): HealthStatusRepository

    @Binds
    @Singleton
    abstract fun bindMedicalAlertsRepository(
        medicalAlertsRepositoryImpl: MedicalAlertsRepositoryImpl
    ): MedicalAlertsRepository
}