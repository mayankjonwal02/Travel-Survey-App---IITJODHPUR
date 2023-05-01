package com.iitj.travelsurveyapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.iitj.travelsurveyappiitj.R
import com.iitj.travelsurveyappiitj.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth


class signup : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentSignupBinding.inflate(inflater,container,false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)
        auth= FirebaseAuth.getInstance()
        binding.dosignup.setOnClickListener()
        {
            if(binding.email1.text.toString().isNotEmpty() && binding.password1.text.toString().isNotEmpty() && binding.password2.text.toString().isNotEmpty())
            {
                if(binding.password2.text.toString()==binding.password1.text.toString())
                {
                    auth.createUserWithEmailAndPassword(binding.email1.text.toString(),binding.password1.text.toString()).addOnCompleteListener({
                        if(it.isSuccessful)
                        {
                            Toast.makeText(context, "Account Created Successfully", Toast.LENGTH_SHORT).show()
                            navController.navigate(R.id.action_signup_to_signin)

                        }
                        else
                        {
                            Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    })
                }
                else
                {
                    Toast.makeText(context, "Passwords Mismatched", Toast.LENGTH_SHORT).show()
                }
            }
            else
            {

                Toast.makeText(context, "Fields Empty", Toast.LENGTH_SHORT).show()
            }
        }
        binding.nosignin.setOnClickListener()
        {
            navController.navigate(R.id.action_signup_to_signin)
        }
    }


}