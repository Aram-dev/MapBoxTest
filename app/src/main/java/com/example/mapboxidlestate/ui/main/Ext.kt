package com.example.mapboxidlestate.ui.main

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.extension.style.expressions.generated.Expression
import com.mapbox.maps.plugin.LocationPuck
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.addOnFlingListener
import com.mapbox.maps.plugin.gestures.addOnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.R
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.scalebar.scalebar
import timber.log.Timber

val PARIS: Point = Point.fromLngLat(2.351167, 48.853354)

fun MapboxMap.addListeners(tag: String) {

    addOnMapIdleListener {
        Timber.tag("Test").d("$tag, OnMapIdle")
    }
    addOnMoveListener(object : OnMoveListener {
        override fun onMove(detector: MoveGestureDetector): Boolean = false

        override fun onMoveBegin(detector: MoveGestureDetector) {
            Timber.tag("Test").d("$tag, MoveBegin")
        }

        override fun onMoveEnd(detector: MoveGestureDetector) {
            Timber.tag("Test").d("$tag, MoveEnd")
        }
    })

    addOnFlingListener {
        Timber.tag("Test").d("$tag, OnFling")
    }
}

fun MapView.configureMap() = gestures.run {
    pitchEnabled = false
    scrollEnabled = true
    rotateEnabled = false
    scalebar.enabled = false
    quickZoomEnabled = true
    pinchToZoomEnabled = true
    doubleTapToZoomInEnabled = true
    scrollDecelerationEnabled = true
    rotateDecelerationEnabled = true
    pinchToZoomDecelerationEnabled = true
}

fun MapView.enableLocation(context: Context, enable: Boolean) {
    Timber.tag("Test").e("Location is ${if (enable) "Enabled" else "Disabled"}")
    location.let {
        it.updateSettings {
            enabled = enable
            pulsingEnabled = enable
            locationPuck = lockPack2D(context)
        }
    }
}

private fun lockPack2D(context: Context): LocationPuck = LocationPuck2D(
    topImage = AppCompatResources.getDrawable(
        context,
        R.drawable.mapbox_user_icon
    ),
    bearingImage = AppCompatResources.getDrawable(
        context,
        R.drawable.mapbox_user_bearing_icon
    ),
    shadowImage = AppCompatResources.getDrawable(
        context,
        R.drawable.mapbox_user_stroke_icon
    ),
    scaleExpression = Expression.interpolate {
        linear()
        zoom()
        stop {
            literal(0.0)
            literal(0.6)
        }
        stop {
            literal(20.0)
            literal(1.0)
        }
    }.toJson()
)