package com.masdika.monja.ui.dashboard

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.ViewAnnotation
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.viewannotation.geometry
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import com.masdika.monja.data.model.Location
import com.masdika.monja.ui.icon.GPSCenterIcon
import com.masdika.monja.ui.icon.HikerIcon
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

    val devicePoint =
        if (hasDeviceLocation) Point.fromLngLat(deviceLongitude, deviceLatitude) else null

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

        when {
            isOnline && devicePoint != null && userLocation != null -> {
                Log.i("MapFlow", "-> Action: Scenario A is executed (Zoom to Midpoint)")
                val midPoint = Point.fromLngLat(
                    (devicePoint.longitude() + userLocation!!.longitude()) / 2,
                    (devicePoint.latitude() + userLocation!!.latitude()) / 2
                )
                animateCameraTo(
                    viewPortState = mapViewportState,
                    point = midPoint,
                    zoom = 8.5,
                    durationMs = 2000
                )
            }

            isOnline && devicePoint != null -> {
                Log.i("MapFlow", "-> Action: Scenario B is executed (Zoom to Device)")
                animateCameraTo(
                    viewPortState = mapViewportState,
                    point = devicePoint,
                    zoom = 8.5,
                    durationMs = 2000
                )
            }

            userLocation != null -> {
                Log.i("MapFlow", "-> Action: Scenario C is executed (Zoom to User Location)")
                animateCameraTo(
                    viewPortState = mapViewportState,
                    point = userLocation,
                    zoom = 8.5,
                    durationMs = 2000
                )
            }

            else -> {
                Log.i("MapFlow", "-> Action: No conditions met, stay in Jakarta")
            }
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
                color = Color.Red,
                trackColor = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(MaterialTheme.colorScheme.background)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
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

                ShowDeviceAnnotation(
                    isLocationLoading = isLocationLoading,
                    isOnline = isOnline,
                    hasDeviceLocation = hasDeviceLocation,
                    latitude = deviceLatitude,
                    longitude = deviceLongitude,
                    macAddress = macAddress
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 5.dp, bottom = 5.dp)
            ) {
                IconButton(
                    enabled = userLocation != null,
                    onClick = {
                        animateCameraTo(
                            viewPortState = mapViewportState,
                            point = userLocation,
                            zoom = 11.0,
                            durationMs = 1000
                        )
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        disabledContainerColor = Color.DarkGray.copy(0.6f),
                        disabledContentColor = Color.LightGray.copy(0.8f)
                    )
                ) {
                    Icon(
                        imageVector = GPSCenterIcon,
                        contentDescription = "Center to Device",
                        modifier = Modifier
                            .size(55.dp)
                            .padding(8.dp)
                    )
                }

                IconButton(
                    enabled = !isLocationLoading && isOnline && hasDeviceLocation,
                    onClick = {
                        animateCameraTo(
                            viewPortState = mapViewportState,
                            point = devicePoint,
                            zoom = 11.0,
                            durationMs = 1000
                        )
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        disabledContainerColor = Color.DarkGray.copy(0.6f),
                        disabledContentColor = Color.LightGray.copy(0.8f)
                    )
                ) {
                    Icon(
                        imageVector = HikerIcon,
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = "Center to Device",
                        modifier = Modifier
                            .size(55.dp)
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}

@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@Composable
private fun ShowDeviceAnnotation(
    isLocationLoading: Boolean,
    isOnline: Boolean,
    hasDeviceLocation: Boolean,
    latitude: Double?,
    longitude: Double?,
    macAddress: String
) {
    if (!isLocationLoading && isOnline && hasDeviceLocation) {
        ViewAnnotation(
            options = viewAnnotationOptions {
                geometry(Point.fromLngLat(longitude!!, latitude!!))
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
            deviceLocation = location,
            isGpsEnabled = true,
            isLocationLoading = true,
            modifier = Modifier.fillMaxSize()
        )
    }

}