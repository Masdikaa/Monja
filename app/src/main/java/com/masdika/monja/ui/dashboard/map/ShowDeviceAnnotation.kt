package com.masdika.monja.ui.dashboard.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import com.mapbox.maps.extension.compose.annotation.ViewAnnotation
import com.mapbox.maps.viewannotation.geometry
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import com.masdika.monja.ui.icon.PinDropIcon

@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@Composable
fun ShowDeviceAnnotation(
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