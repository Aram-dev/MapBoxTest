package com.example.mapboxidlestate.ui.map

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.expressions.generated.Expression
import com.mapbox.maps.plugin.LocationPuck
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.addOnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.R
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.scalebar.scalebar

class MapFragmentIntegration : Fragment() {

    private var viewCreated: OnViewCreated? = null
    lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mapView = MapView(inflater.context)
        return mapView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewCreated?.onViewCreated()
    }

    @SuppressLint("Lifecycle")
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    @SuppressLint("Lifecycle")
    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    @SuppressLint("Lifecycle")
    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    fun getViewAsync(onViewCreated: OnViewCreated) {
        this.viewCreated = onViewCreated
    }

    fun loadMap(idleListener: OnMapIdle, moveListener: OnMapMoveBegan) {
        val map = mapView.getMapboxMap()
        map.loadStyleUri(Style.MAPBOX_STREETS) {
            mapView.configureMap()
            mapView.enableLocation(requireContext())
        }
        map.addOnMapIdleListener {
            idleListener.onIdle()
        }
        map.addOnMoveListener(object : OnMoveListener {
            override fun onMove(detector: MoveGestureDetector): Boolean = false
            override fun onMoveBegin(detector: MoveGestureDetector) = moveListener.onMoveBegan()
            override fun onMoveEnd(detector: MoveGestureDetector) = Unit

        })
    }

    @SuppressLint("MissingPermission")
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

    private fun MapView.enableLocation(context: Context) {
        location.let {
            it.updateSettings {
                enabled = true
                pulsingEnabled = true
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

    fun interface OnViewCreated {
        fun onViewCreated()
    }

    fun interface OnMapIdle {
        fun onIdle()
    }

    fun interface OnMapMoveBegan {
        fun onMoveBegan()
    }
}