package com.example.mapboxidlestate.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mapboxidlestate.R
import com.example.mapboxidlestate.databinding.MainFragmentBinding
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import timber.log.Timber

class MainFragment : Fragment(), Style.OnStyleLoaded {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: MainFragmentBinding
    private lateinit var map: MapView
    private lateinit var mapboxMap: MapboxMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.initMapBox()
    }

    private fun MainFragmentBinding.initMapBox() {
        map = mapView
        mapboxMap = map.getMapboxMap()
        mapboxMap.loadStyleUri(Style.MAPBOX_STREETS, this@MainFragment)
    }

    override fun onStyleLoaded(style: Style) {
        Timber.tag("Test").d("Fragment StyleLoaded")
        map.configureMap()
        mapboxMap.setCamera(
            cameraOptions {
                center(PARIS)
                zoom(12.0)
            }
        )
        mapboxMap.addListeners("Fragment")
    }
}
