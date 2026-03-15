package com.masdika.monja.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location

@Composable
fun MapboxMap(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        val mapViewportState = rememberMapViewportState {
            setCameraOptions {
                zoom(8.5)
                center(Point.fromLngLat(111.441002, -7.616700))
                pitch(0.0)
                bearing(0.0)
            }
        }
        MapboxMap(
            modifier = Modifier.fillMaxSize(),
            mapViewportState = mapViewportState,
            scaleBar = { ScaleBar() },
            logo = { Logo() },
            attribution = { Attribution() }
        ) {
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
                            mapView.mapboxMap.setCamera(
                                CameraOptions.Builder()
                                    .center(point)
                                    .zoom(9.0)
                                    .build()
                            )
                            mapView.location.removeOnIndicatorPositionChangedListener(this)
                        }
                    }

                mapView.location.addOnIndicatorPositionChangedListener(
                    onIndicatorPositionChangedListener
                )
            }
        }
    }
}