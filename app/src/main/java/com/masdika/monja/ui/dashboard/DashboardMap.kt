package com.masdika.monja.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.ViewAnnotation
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.viewannotation.geometry
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import com.masdika.monja.data.model.Location
import com.masdika.monja.ui.icon.PinDropIcon

@OptIn(MapboxExperimental::class)
@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@Composable
fun DashboardMap(
    macAddress: String,
    isOnline: Boolean,
    deviceLocation: Location?,
    isGpsEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    val deviceLatitude = deviceLocation?.latitude?.toDoubleOrNull()
    val deviceLongitude = deviceLocation?.longitude?.toDoubleOrNull()
    val hasDeviceLocation = deviceLatitude != null && deviceLongitude != null

    val mapViewportState = rememberMapViewportState {
        setCameraOptions {
            zoom(4.0)
            if (hasDeviceLocation) {
                center(Point.fromLngLat(deviceLongitude, deviceLatitude))
            } else {
                center(Point.fromLngLat(111.441002, -7.616700))
            }
            pitch(0.0)
            bearing(0.0)
        }
    }

    Box(modifier = modifier) {
        MapboxMap(
            modifier = Modifier.fillMaxSize(),
            mapViewportState = mapViewportState,
            scaleBar = { ScaleBar() }
        ) {
            if (isGpsEnabled) {

                MapEffect(Unit) { mapView ->
                    mapView.location.updateSettings {
                        enabled = true
                        locationPuck = createDefault2DPuck(withBearing = true)
                        puckBearingEnabled = true
                        puckBearing = PuckBearing.HEADING
                        pulsingEnabled = true
                    }

                    val onIndicatorPositionChangedListener =
                        object : OnIndicatorPositionChangedListener {
                            override fun onIndicatorPositionChanged(point: Point) {
                                if (isOnline && hasDeviceLocation) { // Midpoint between device and user loc
                                    val midLng = (point.longitude() + deviceLongitude) / 2
                                    val midLat = (point.latitude() + deviceLatitude) / 2
                                    val midPoint = Point.fromLngLat(midLng, midLat)

                                    mapView.mapboxMap.setCamera(
                                        CameraOptions.Builder()
                                            .center(midPoint)
                                            .zoom(9.0)
                                            .build()
                                    )
                                } else {
                                    mapView.mapboxMap.setCamera(
                                        CameraOptions.Builder()
                                            .center(point)
                                            .zoom(10.0)
                                            .build()
                                    )
                                }
                                mapView.location.removeOnIndicatorPositionChangedListener(this)
                            }
                        }

                    mapView.location.addOnIndicatorPositionChangedListener(
                        onIndicatorPositionChangedListener
                    )
                }
            }

            if (isOnline && hasDeviceLocation) {
                PinDropPoint(
                    latitude = deviceLatitude,
                    longitude = deviceLongitude,
                    macAddress = macAddress
                )
            }

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