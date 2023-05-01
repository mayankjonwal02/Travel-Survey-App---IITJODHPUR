package com.iitj.travelsurveyapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.iitj.travelsurveyappiitj.R
import com.iitj.travelsurveyappiitj.databinding.FragmentSigninBinding

class signin : Fragment() {
    private lateinit var binding: FragmentSigninBinding
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth
    private lateinit var refer: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentSigninBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)
        auth= FirebaseAuth.getInstance()
        //refer= FirebaseDatabase.getInstance().getReference("user").child(auth.currentUser?.email.toString())
        binding.dosignin.setOnClickListener()
        {
            if(binding.email.text.toString().isNotEmpty() && binding.password.text.toString().isNotEmpty())
            {
                auth.signInWithEmailAndPassword(binding.email.text.toString(),binding.password.text.toString()).addOnCompleteListener({
                    if(it.isSuccessful)
                    {
                        //refer.child("email").setValue(binding.email.text.toString())
                        Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                        getActivity()?.let { it1 -> ActivityCompat.requestPermissions(it1, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1000) }
                        navController.navigate(R.id.action_signin_to_startpointFragment)
                    }
                    else
                    {
                        Toast.makeText(context, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            else
            {
                Toast.makeText(context, "Fields Empty", Toast.LENGTH_SHORT).show()
            }
        }
        binding.nosignup.setOnClickListener()
        {
            navController.navigate(R.id.action_signin_to_signup)
        }

    }

}