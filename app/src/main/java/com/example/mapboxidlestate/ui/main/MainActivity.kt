package com.example.mapboxidlestate.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mapboxidlestate.databinding.MainActivityBinding
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import timber.log.Timber

class MainActivity : AppCompatActivity(), Style.OnStyleLoaded {

    private lateinit var bind: MainActivityBinding
    private lateinit var map: MapView
    private lateinit var mapboxMap: MapboxMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = MainActivityBinding.inflate(layoutInflater)
        setContentView(bind.root)

        bind.setUpClick()
        bind.initMapBox(this@MainActivity)
    }

    private fun MainActivityBinding.setUpClick() {
        btnFragment.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(bind.container.id, MainFragment.newInstance())
                .commitNow().also { btnFragment.text = "Activity" }
        }

        scLocation.setOnCheckedChangeListener { _, checked ->
            map.enableLocation(this@MainActivity, checked)
        }
    }

    private fun MainActivityBinding.initMapBox(loaded: Style.OnStyleLoaded) {
        map = mapView
        mapboxMap = map.getMapboxMap()
        mapboxMap.loadStyleUri(Style.MAPBOX_STREETS, loaded)
    }

    override fun onStyleLoaded(style: Style) {
        Timber.tag("Test").d("Activity StyleLoaded")
        map.configureMap()
        mapboxMap.setCamera(cameraOptions {
            center(PARIS)
            zoom(12.0)
        })
        mapboxMap.addListeners("Activity")
    }
}
