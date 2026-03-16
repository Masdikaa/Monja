package com.masdika.monja.ui.dashboard

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.ViewAnnotation
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.viewannotation.geometry
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import com.masdika.monja.data.model.Location
import com.masdika.monja.ui.icon.PinDropIcon
import com.masdika.monja.ui.theme.MonjaTheme

@OptIn(MapboxExperimental::class)
@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@Composable
fun DashboardMap(
    macAddress: String,
    isOnline: Boolean,
    deviceLocation: Location?,
    isGpsEnabled: Boolean,
    isLocationLoading: Boolean,
    modifier: Modifier = Modifier
) {
    val deviceLatitude = deviceLocation?.latitude?.toDoubleOrNull()
    val deviceLongitude = deviceLocation?.longitude?.toDoubleOrNull()
    val hasDeviceLocation = deviceLatitude != null && deviceLongitude != null

    var userLocation by remember { mutableStateOf<Point?>(null) }

    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            zoom(8.5)
            center(Point.fromLngLat(106.83580, -6.21438)) // Jakarta
        }
    }

    LaunchedEffect(isLocationLoading, userLocation, hasDeviceLocation, isGpsEnabled) {
        Log.i("MapFlow", "--- LaunchedEffect Triggered ---")
        Log.i("MapFlow", "Loading Status: $isLocationLoading")
        Log.i("MapFlow", "App Permission: $isGpsEnabled")
        Log.i("MapFlow", "User Location: ${if (userLocation != null) "AVAILABLE" else "NULL"}")
        Log.i("MapFlow", "Device Location (hasDeviceLocation): $hasDeviceLocation")

        if (isLocationLoading) {
            Log.i("MapFlow", "-> Action: Detained. Awaiting Supabase Loading")
            return@LaunchedEffect
        }

        val devicePoint =
            if (hasDeviceLocation) Point.fromLngLat(deviceLongitude, deviceLatitude) else null

        when {
            isOnline && devicePoint != null && userLocation != null -> {
                Log.i("MapFlow", "-> Action: Scenario A is executed (Zoom to Midpoint)")
                val midPoint = Point.fromLngLat(
                    (devicePoint.longitude() + userLocation!!.longitude()) / 2,
                    (devicePoint.latitude() + userLocation!!.latitude()) / 2
                )
                mapViewportState.flyTo(
                    CameraOptions.Builder().center(midPoint).zoom(8.5).build(),
                    MapAnimationOptions.mapAnimationOptions { duration(2000) }
                )
            }

            isOnline && devicePoint != null -> {
                Log.i("MapFlow", "-> Action: Scenario B is executed (Zoom to Device)")
                mapViewportState.flyTo(
                    CameraOptions.Builder().center(devicePoint).zoom(8.5).build(),
                    MapAnimationOptions.mapAnimationOptions { duration(2000) }
                )
            }

            userLocation != null -> {
                Log.i("MapFlow", "-> Action: Scenario C is executed (Zoom to User Location)")
                mapViewportState.flyTo(
                    CameraOptions.Builder().center(userLocation!!).zoom(8.5).build(),
                    MapAnimationOptions.mapAnimationOptions { duration(2000) }
                )
            }

            else -> {
                Log.i("MapFlow", "-> Action: No conditions met, stay in Jakarta")
            }
        }
    }

    Box(modifier = modifier) {
        MapboxMap(
            modifier = Modifier.fillMaxSize(),
            mapViewportState = mapViewportState
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
                            userLocation = point
                            mapView.location.removeOnIndicatorPositionChangedListener(this)
                        }
                    })
                }
            }

            if (!isLocationLoading && isOnline && hasDeviceLocation) {
                PinDropPoint(
                    latitude = deviceLatitude,
                    longitude = deviceLongitude,
                    macAddress = macAddress
                )
            }
        }

        if (isLocationLoading) {
            LinearProgressIndicator(
                color = Color.Red,
                trackColor = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .align(Alignment.TopCenter)
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(MaterialTheme.colorScheme.background)
            )
        }
    }
}

@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@Composable
private fun PinDropPoint(
    latitude: Double,
    longitude: Double,
    macAddress: String
) {
    ViewAnnotation(
        options = viewAnnotationOptions {
            geometry(Point.fromLngLat(longitude, latitude))
            allowOverlap(true)
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = PinDropIcon,
                contentDescription = "Pin Drop Icon",
                tint = Color.Red,
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.height(5.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary.copy(0.3f))
                    .padding(horizontal = 5.dp, vertical = 2.dp)
                    .clip(shape = RoundedCornerShape(16)),
            ) {
                Text(
                    text = macAddress,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
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
            deviceLocation = location,
            isGpsEnabled = true,
            isLocationLoading = true,
            modifier = Modifier.fillMaxSize()
        )
    }

}