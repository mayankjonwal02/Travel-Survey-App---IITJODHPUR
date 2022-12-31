package com.example.travelsurveyapp.fragments

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.livelocation.locationService
import com.example.travelsurveyapp.R
import com.example.travelsurveyapp.databinding.FragmentTracklocationBinding
import com.example.travelsurveyapp.locationservice2
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*


class tracklocationFragment : Fragment() , OnMapReadyCallback {

    private lateinit var reference: DatabaseReference

    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentTracklocationBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var navController: NavController
    private lateinit var auth:FirebaseAuth
    //private lateinit var points:MutableList<>
    var currentLocation: Location? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTracklocationBinding.inflate(inflater, container, false)
        binding.map.onCreate(savedInstanceState)
        binding.map.onResume()
        binding.map.getMapAsync(this)
        return binding.root
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)
        auth=FirebaseAuth.getInstance()
        reference=FirebaseDatabase.getInstance().getReference("user").child(auth.currentUser?.uid.toString())
        /*fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        locationRequest = LocationRequest.create().apply {
            interval = 5000
            fastestInterval = 2000
            maxWaitTime = 2
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }*/
        reference.child("coordinates").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    for(snap in snapshot.children)
                    {
                        //var a= mutableListOf<Double>(snap.child())
                        mMap.addMarker(MarkerOptions().position(LatLng(snap.child("0").getValue().toString().toDouble(), snap.child("1").getValue().toString().toDouble())).title(snap.child("1").getValue().toString()).draggable(true))
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(snap.child("0").getValue().toString().toDouble(), snap.child("1").getValue().toString().toDouble()),21.0f))
                    }
                }
                else
                {
                    Toast.makeText(requireContext(), "no data found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "no data found", Toast.LENGTH_SHORT).show()
            }

        })
       /* locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                var task = fusedLocationProviderClient.lastLocation
                if (getContext()?.let {
                        ActivityCompat.checkSelfPermission(
                            it,
                            android.Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    } != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                        getContext() as Activity,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                        102
                    )

                } else {
                    task.addOnSuccessListener {
                        if (it != null) {
                            Toast.makeText(requireContext(), "${it.longitude}", Toast.LENGTH_SHORT).show()

                            var loc = mutableListOf<String>(it.latitude.toString(),it.longitude.toString() , Calendar.getInstance().time.toString())
                            reference.child("coordinates").push().setValue(loc)
                        }
                    }
                }


            }


        } */
        binding.start.setOnClickListener()
        {

            //requireActivity().startService(Intent(context,locationService::class.java))
         /*Intent(context, locationService::class.java).apply {
                action= locationService.ACTION_START
                requireActivity().startService(this)
            }*/

            requireActivity().startForegroundService(Intent(context,locationservice2::class.java))
            Toast.makeText(requireContext(), "Tracking started", Toast.LENGTH_SHORT).show()
            binding.message.text = "tracking...."
        }
        binding.stop.setOnClickListener()
        {
            /*Intent(getContext(),locationService::class.java).apply {
                action=locationService.ACTION_STOP
                getActivity()?.startService(this)
            }*/
            requireActivity().stopService(Intent(context,locationservice2::class.java))
            Toast.makeText(requireContext(), "Tracking Stopped", Toast.LENGTH_SHORT).show()
            binding.message.text = "Live Location"
            navController.navigate(R.id.action_tracklocationFragment_to_endpointFragment)


        }

    }

}
