package com.example.graduationproject.chef

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.graduationproject.databinding.FragmentMapsBinding
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapController
import org.osmdroid.views.overlay.Marker

class MapsFragment : Fragment() {

    private lateinit var binding: FragmentMapsBinding
    private lateinit var mapController: MapController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Configuration.getInstance().load(requireContext(), activity?.getPreferences(MODE_PRIVATE))
        binding.mapView.apply {
            setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
            setBuiltInZoomControls(true)
            setMultiTouchControls(true)
            mapController = controller as MapController
            val initialLocation = GeoPoint(
                arguments?.getString(com.example.graduationproject.Constants.LATITUDE)
                    ?.toDouble()!!,
                arguments?.getString(com.example.graduationproject.Constants.LONGITUDE)
                    ?.toDouble()!!
            )

            mapController.setZoom(18)
            mapController.animateTo(initialLocation)
            val marker = Marker(this)
            marker.position = initialLocation
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            overlays.add(marker)
            invalidate()
        }
        binding.openGsm.setOnClickListener {
            val uri = Uri.parse("geo:${ arguments?.getString(com.example.graduationproject.Constants.LATITUDE)},${arguments?.getString(com.example.graduationproject.Constants.LONGITUDE)}?q=${arguments?.getString(com.example.graduationproject.Constants.LATITUDE)},${arguments?.getString(com.example.graduationproject.Constants.LONGITUDE)}")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.google.android.apps.maps")
           // if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
           // }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }
}