package com.example.travelsurveyapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.example.travelsurveyapp.R
import com.example.travelsurveyapp.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class mapFragment : Fragment(), OnMapReadyCallback  {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentMapBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),201)
        binding=FragmentMapBinding.inflate(inflater, container, false)

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.onResume()
        binding.mapView.getMapAsync(this)
        return binding.root

    }
   /* override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.onResume()
        binding.mapView.getMapAsync(this)
    }*/

    override fun onMapReady(p0: GoogleMap) {
        p0?.let{
            mMap=it
        }

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title(" Sydney ki maa ki chut"))

        /*ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
            102
        )*/
        //var a=mMap.getMyLocation()
        //Toast.makeText(this, "${a.latitude} ${a.longitude}", Toast.LENGTH_SHORT).show()
        //mMap.isMyLocationEnabled = true
        //mMap.isBuildingsEnabled = true
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }


}