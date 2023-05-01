package com.iitj.travelsurveyapp.fragments

import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
//import com.iitj.travelsurveyapp.R
//import com.iitj.travelsurveyapp.databinding.FragmentStartpointBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.iitj.travelsurveyappiitj.R
import com.iitj.travelsurveyappiitj.databinding.FragmentStartpointBinding
import java.time.LocalDate
import java.util.*

class startpointFragment : Fragment() ,OnMapReadyCallback{
    private lateinit var map: GoogleMap
    private lateinit var binding: FragmentStartpointBinding
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var refer: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentStartpointBinding.inflate(inflater,container,false)
        binding.startmap.onCreate(savedInstanceState)
        binding.startmap.onResume()
        binding.startmap.getMapAsync(this)
        return binding.root
    }
    override fun onMapReady(p0: GoogleMap) {
        map=p0


        map.setOnMarkerDragListener(object :GoogleMap.OnMarkerDragListener{
            override fun onMarkerDrag(p0: Marker) {

            }

            override fun onMarkerDragEnd(p0: Marker) {
                var latlog=LatLng(p0?.position!!.latitude , p0?.position!!.longitude)
                map.addMarker(MarkerOptions().position(latlog).title("hello").draggable(true))
                map.animateCamera(CameraUpdateFactory.newLatLng(latlog))
            }

            override fun onMarkerDragStart(p0: Marker) {
                TODO("Not yet implemented")
            }

        })

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var date= LocalDate.now()


        navController= Navigation.findNavController(view)
        auth= FirebaseAuth.getInstance()
        refer= FirebaseDatabase.getInstance().getReference("user").child(auth.currentUser?.uid.toString())
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.submit2.setOnClickListener(){
            if(binding.address.text.toString().isNotEmpty())
            {
                //refer.child("location").push().setValue(binding.address.text.toString())
                refer.child("email").setValue(auth.currentUser?.email.toString())
                refer.child("StartLocation").child("address").setValue(binding.address.text.toString())
                navController.navigate(R.id.action_startpointFragment_to_message1)
            }
            else
            {
                Toast.makeText(context, "Enter Address", Toast.LENGTH_SHORT).show()
            }
        }

        binding.getlocationbutton2.setOnClickListener()
        {
            getaddress()
            //  println(1)
        }
    }
    private fun getaddress() {
        var task=fusedLocationProviderClient.lastLocation
        if(getContext()?.let { ActivityCompat.checkSelfPermission(it,android.Manifest.permission.ACCESS_FINE_LOCATION) } != PackageManager.PERMISSION_GRANTED || getContext()?.let {
                ActivityCompat.checkSelfPermission(
                    it,android.Manifest.permission.ACCESS_COARSE_LOCATION)
            } != PackageManager.PERMISSION_GRANTED) {
            getActivity()?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    102
                )
            }
            return
        }

        task.addOnSuccessListener {
            if (it != null) {
                Toast.makeText(
                    context,
                    "${it.latitude} ${it.longitude}",
                    Toast.LENGTH_SHORT
                ).show()
                var geocoder = Geocoder(getContext(), Locale.getDefault())
                var addr = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                var addr1="${addr.get(0).subLocality} , ${addr.get(0).locality}  , ${addr.get(0).countryName}"
                binding.address.setText(addr[0].getAddressLine(0))
                refer.child("StartLocation").child("address").setValue(binding.address.text.toString())
                refer.child("StartLocation").child("latitude").setValue(it.latitude)
                refer.child("StartLocation").child("longitude").setValue(it.longitude)


                map.addMarker(MarkerOptions().position(LatLng(it.latitude,it.longitude)).title("your location").draggable(true))
                map.animateCamera(CameraUpdateFactory.newLatLng(LatLng(it.latitude,it.longitude)))
            }
            else
            {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
            }
        }
    }


}