package com.iitj.travelsurveyapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.google.firebase.auth.FirebaseAuth
import com.iitj.travelsurveyappiitj.R
import com.iitj.travelsurveyappiitj.databinding.FragmentEndscreenBinding


class endscreen : Fragment() {
    private lateinit var binding: FragmentEndscreenBinding
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentEndscreenBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController= Navigation.findNavController(view)
        auth= FirebaseAuth.getInstance()

        var anima=AnimationUtils.loadAnimation(this.context, com.google.android.material.R.anim.abc_fade_in).apply {
            duration=5000
        }
        binding.thanks.startAnimation(anima)
        binding.signout.setOnClickListener()
        {
            auth.signOut()
            navController.navigate(R.id.action_endscreen_to_signin)
        }
    }
}