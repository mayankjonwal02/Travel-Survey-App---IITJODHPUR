package com.iitj.travelsurveyapp.fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.iitj.travelsurveyappiitj.R
import com.iitj.travelsurveyappiitj.databinding.FragmentSplashBinding
import com.google.firebase.auth.FirebaseAuth

class splash : Fragment() {

    private lateinit var binding: FragmentSplashBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentSplashBinding.inflate(inflater,container,false)

        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var textanim=AnimationUtils.loadAnimation(this.context, androidx.appcompat.R.anim.abc_slide_in_top).apply {
            duration=1000
        }
        binding.textView.startAnimation(textanim)

        auth= FirebaseAuth.getInstance()
        navController= Navigation.findNavController(view)
        Handler().postDelayed( Runnable {
            if (auth.currentUser != null)
            {
                navController.navigate(R.id.action_splash_to_startpointFragment)
            }
            else
            {
                navController.navigate(R.id.action_splash_to_message2)
            }
        },4000)



    }


}