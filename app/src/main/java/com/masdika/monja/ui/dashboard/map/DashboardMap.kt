package com.masdika.monja.ui.dashboard.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.masdika.monja.data.Result
import com.masdika.monja.data.model.Location
import com.masdika.monja.ui.theme.MonjaTheme

@OptIn(MapboxExperimental::class)
@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@Composable
fun DashboardMap(
    macAddress: String,
    isOnline: Boolean,
    locationState: Result<Location?>,
    isGpsEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    val deviceLocation = (locationState as? Result.Success)?.data
    val isLocationLoading = locationState is Result.Loading

    val deviceLatitude = deviceLocation?.latitude?.toDoubleOrNull()
    val deviceLongitude = deviceLocation?.longitude?.toDoubleOrNull()
    val hasDeviceLocation = deviceLatitude != null && deviceLongitude != null
    val devicePoint =
        if (hasDeviceLocation) Point.fromLngLat(deviceLongitude, deviceLatitude) else null

    var isMapDarkMode by rememberSaveable { mutableStateOf(false) }
    var isSatelliteMode by rememberSaveable { mutableStateOf(false) }

    var userLat by rememberSaveable { mutableStateOf<Double?>(null) }
    var userLng by rememberSaveable { mutableStateOf<Double?>(null) }
    val userLocation =
        if (userLat != null && userLng != null) Point.fromLngLat(userLng!!, userLat!!) else null

    var isInitialCameraSet by rememberSaveable(macAddress) { mutableStateOf(false) }

    val currentMapStyle = when {
        isSatelliteMode -> Style.SATELLITE_STREETS
        isMapDarkMode -> Style.DARK
        else -> Style.MAPBOX_STREETS
    }

    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            zoom(8.5)
            center(Point.fromLngLat(106.83580, -6.21438)) // Jakarta
        }
    }

    LaunchedEffect(isLocationLoading, userLocation, hasDeviceLocation, isGpsEnabled) {
        if (isInitialCameraSet || isLocationLoading) return@LaunchedEffect

        val isSuccess = handleInitialCameraZoom(
            isOnline = isOnline,
            devicePoint = devicePoint,
            userLocation = userLocation,
            mapViewportState = mapViewportState
        )

        if (isSuccess) {
            isInitialCameraSet = true
        }
    }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        if (isLocationLoading) {
            LinearProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
            )
        } else {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            MapboxMap(
                modifier = Modifier.fillMaxSize(),
                mapViewportState = mapViewportState,
                style = { MapStyle(style = currentMapStyle) }
            ) {
                if (isGpsEnabled) {
                    MapEffect(Unit) { mapView ->
                        mapView.location.updateSettings {
                            enabled = true
                            pulsingEnabled = true
                        }
                        mapView.location.addOnIndicatorPositionChangedListener(object :
                            OnIndicatorPositionChangedListener {
                            override fun onIndicatorPositionChanged(point: Point) {
                                userLat = point.latitude()
                                userLng = point.longitude()
                                mapView.location.removeOnIndicatorPositionChangedListener(this)
                            }
                        })
                    }
                }

                ShowDeviceAnnotation(
                    isLocationLoading = isLocationLoading,
                    isOnline = isOnline,
                    hasDeviceLocation = hasDeviceLocation,
                    latitude = deviceLatitude,
                    longitude = deviceLongitude,
                    macAddress = macAddress
                )
            }

            MapControlButtons(
                isSatelliteMode = isSatelliteMode,
                isMapDarkMode = isMapDarkMode,
                onToggleTheme = { isMapDarkMode = !isMapDarkMode },
                onToggleSatellite = { isSatelliteMode = !isSatelliteMode },
                userLocation = userLocation,
                devicePoint = devicePoint,
                isLocationLoading = isLocationLoading,
                isOnline = isOnline,
                onCenterUser = { animateCameraTo(mapViewportState, userLocation, 11.0, 1000) },
                onCenterDevice = { animateCameraTo(mapViewportState, devicePoint, 11.0, 1000) },
                modifier = Modifier.align(Alignment.BottomEnd)
            )

        }
    }
}

private fun handleInitialCameraZoom(
    isOnline: Boolean,
    devicePoint: Point?,
    userLocation: Point?,
    mapViewportState: MapViewportState
): Boolean {
    if (isOnline && devicePoint != null && userLocation != null) {
        val midPoint = Point.fromLngLat(
            (devicePoint.longitude() + userLocation.longitude()) / 2,
            (devicePoint.latitude() + userLocation.latitude()) / 2
        )
        animateCameraTo(mapViewportState, midPoint, 8.5, 2000)
        return true
    }

    if (isOnline && devicePoint != null) {
        animateCameraTo(mapViewportState, devicePoint, 8.5, 2000)
        return true
    }

    if (userLocation != null) {
        animateCameraTo(mapViewportState, userLocation, 8.5, 2000)
        return true
    }

    return false
}

private fun animateCameraTo(
    viewPortState: MapViewportState,
    point: Point?,
    zoom: Double,
    durationMs: Long
) {
    viewPortState.flyTo(
        cameraOptions = CameraOptions.Builder()
            .center(point)
            .zoom(zoom)
            .pitch(0.0)
            .build(),
        animationOptions = MapAnimationOptions.mapAnimationOptions {
            duration(durationMs)
        }
    )
}

@Preview
@Composable
private fun DashboardMapPreview() {
    MonjaTheme {
        val location = Location(
            latitude = "0.0",
            longitude = "0.0"
        )
        DashboardMap(
            macAddress = "TEST",
            isOnline = false,
            locationState = Result.Success(location),
            isGpsEnabled = true,
            modifier = Modifier.fillMaxSize()
        )
    }

}