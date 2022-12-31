package com.example.travelsurveyapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.travelsurveyapp.R
import com.example.travelsurveyapp.databinding.FragmentMessage1Binding
import com.example.travelsurveyapp.databinding.FragmentMessage2Binding

// TODO: Rename parameter arguments, choose names that match

class message1 : Fragment() {
    private lateinit var binding: FragmentMessage1Binding
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentMessage1Binding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)
        binding.next.setOnClickListener()
        {
            navController.navigate(R.id.action_message1_to_tracklocationFragment)
        }
    }
}