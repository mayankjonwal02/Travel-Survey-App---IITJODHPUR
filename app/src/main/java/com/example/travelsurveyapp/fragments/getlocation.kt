package com.example.travelsurveyapp.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.travelsurveyapp.MainActivity
import com.example.travelsurveyapp.R
import com.example.travelsurveyapp.databinding.FragmentGetlocationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.internal.ContextUtils
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class getlocation : Fragment()  {
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var binding: FragmentGetlocationBinding
    private lateinit var refer:DatabaseReference
    private lateinit var supportfragment:SupportMapFragment



    /*override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        binding=FragmentGetlocationBinding.inflate(inflater,container,false)
        binding.mapView4.getMapAsync(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController= Navigation.findNavController(view)
        auth= FirebaseAuth.getInstance()
        refer=FirebaseDatabase.getInstance().getReference("user").child(auth.currentUser?.uid.toString())
        //navController.navigate(R.id.action_getlocation_to_letsstart)
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(requireActivity())//requestpermit()
        binding.submit.setOnClickListener(){
            if(binding.address.text.toString().isNotEmpty())
            {
                refer.child("location").push().setValue(binding.address.text.toString())
                refer.child("email").setValue(auth.currentUser?.email.toString())
                navController.navigate(R.id.action_getlocation_to_letsstart)
            }
            else
            {
                Toast.makeText(context, "Enter Address", Toast.LENGTH_SHORT).show()
            }
        }

        binding.getlocationbutton.setOnClickListener()
        {
            getaddress()
          //  println(1)
        }

    }


    @SuppressLint("MissingPermission")
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
                var addr1="${addr.get(0).subLocality} , ${addr.get(0).locality} , ${addr.get(0).countryName}"
                binding.address.setText(addr1)
            }
            else
            {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
            }
        }
    }*/



}



//getContext() getActivity() requiredActivity()
/*fun getActivity(): Activity {
    return Activity()

}*/
