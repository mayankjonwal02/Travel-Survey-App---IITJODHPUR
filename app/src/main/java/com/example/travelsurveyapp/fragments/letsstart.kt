package com.example.travelsurveyapp.fragments

import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.travelsurveyapp.R
import com.example.travelsurveyapp.databinding.FragmentLetsstartBinding
import com.example.travelsurveyapp.databinding.FragmentSurveyquestionsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.time.LocalDate


class letsstart : Fragment() {
    private lateinit var binding: FragmentLetsstartBinding
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth
    private lateinit var refer: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentLetsstartBinding.inflate(inflater,container,false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var date= LocalDate.now()
        navController=Navigation.findNavController(view)
        binding.startsurveytext.setOnClickListener()
        {
            auth=FirebaseAuth.getInstance()
            refer= FirebaseDatabase.getInstance().getReference("user").child(auth.currentUser?.uid.toString()).child(date.toString())
            refer.child("key").addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var key=snapshot.getValue().toString()
                    if(key=="submitted")
                    {
                        navController.navigate(R.id.action_letsstart_to_endscreen)
                    }
                    else
                    {
                        navController.navigate(R.id.action_letsstart_to_surveyquestions)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.i(TAG, "onCancelled:not found ")
                }

            })

           // navController.navigate(R.id.action_letsstart_to_surveyquestions)
        }
    }
}