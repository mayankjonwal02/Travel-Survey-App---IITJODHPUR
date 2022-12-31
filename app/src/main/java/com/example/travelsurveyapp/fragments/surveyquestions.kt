package com.example.travelsurveyapp.fragments

import android.content.ContentValues.TAG
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelsurveyapp.R
import com.example.travelsurveyapp.databinding.FragmentSignupBinding
import com.example.travelsurveyapp.databinding.FragmentSurveyquestionsBinding
import com.example.travelsurveyapp.recyl.recyclerviewadapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.w3c.dom.Text
import java.time.LocalDate

class surveyquestions : Fragment() {
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth
    private lateinit var refer: DatabaseReference
    private lateinit var binding: FragmentSurveyquestionsBinding
    private lateinit var oprv:RecyclerView
    private lateinit var question:MutableList<String>
    private lateinit var options:MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding=FragmentSurveyquestionsBinding.inflate(inflater,container,false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var date= LocalDate.now()

        binding.card.visibility= INVISIBLE
        binding.next.visibility=INVISIBLE
        binding.previous.visibility= INVISIBLE
        navController= Navigation.findNavController(view)
        auth= FirebaseAuth.getInstance()
        refer=FirebaseDatabase.getInstance().reference  //  .getReference("user").child(auth.currentUser?.email.toString())

        oprv=binding.ansrv
        oprv.layoutManager=LinearLayoutManager(context)
        question= mutableListOf()
        options= mutableListOf()
        var pointer=0
        var key=0
        refer.child("question").addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists())
                {
                    question.clear()
                    key=0

                    for(snap in snapshot.children)
                    {
                        var ques=snap.child("question").getValue().toString()
                        question.add(ques!!)

                    }
                    binding.next.visibility= VISIBLE
                    binding.previous.visibility=VISIBLE
                    binding.question.text=question[pointer]
                    refer.child("question").child("${pointer+1}").child("option").addValueEventListener(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists())
                            {
                                options.clear()
                                for(snap in snapshot.children)
                                {
                                    var option=snap.getValue().toString()
                                    options.add(option!!)
                                }
                                oprv.adapter=recyclerviewadapter(options,pointer+1,question[pointer])
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show()
                        }

                    })
                }
                //oprv.adapter=recyclerviewadapter(question)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show()
            }

        })


        binding.next.setOnClickListener()
        {
            options.clear()
            binding.card.visibility= INVISIBLE
            pointer+=1

            if(binding.answer.text.toString().isNotEmpty())
            {
                refer.child("user").child(auth.currentUser?.uid.toString()).child("responces").child(pointer.toString()).setValue(binding.answer.text.toString())
                binding.answer.setText("")
                Toast.makeText(context, "Responce Updated", Toast.LENGTH_SHORT).show()
            }
            if(pointer<question.size)
            {
                binding.question.startAnimation(AnimationUtils.loadAnimation(this.context,R.anim.pop))
                binding.ansrv.startAnimation(AnimationUtils.loadAnimation(this.context,R.anim.pop))

                binding.question.text=question[pointer]
                refer.child("question").child("${pointer+1}").child("option").addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists())
                        {
                            options.clear()
                            for(snap in snapshot.children)
                            {
                                var option=snap.getValue().toString()
                                options.add(option!!)
                            }
                            //oprv.adapter.
                            if(options.size==0)
                            {
                                binding.card.startAnimation(AnimationUtils.loadAnimation(context,R.anim.pop))
                                binding.card.visibility= VISIBLE
                            }
                            oprv.adapter=recyclerviewadapter(options,pointer+1,question[pointer])
                        }
                        else
                        {
                            Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show()
                    }

                })
            }
            else
            {
                //refer.child("user").child(auth.currentUser?.uid.toString()).child("key").setValue("submitted")
                navController.navigate(R.id.action_surveyquestions_to_endscreen)
            }
        }


        binding.previous.setOnClickListener()
        {
            options.clear()
            binding.card.visibility= INVISIBLE
            binding.answer.setText("")
            pointer-=1


            if(pointer>=0)
            {
                binding.question.startAnimation(AnimationUtils.loadAnimation(this.context,R.anim.pop))
                binding.ansrv.startAnimation(AnimationUtils.loadAnimation(this.context,R.anim.pop))
                binding.question.text=question[pointer]
                refer.child("question").child("${pointer+1}").child("option").addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists())
                        {
                            options.clear()
                            for(snap in snapshot.children)
                            {
                                var option=snap.getValue().toString()
                                options.add(option!!)
                            }
                            //oprv.adapter.
                            if(options.size==0)
                            {
                                binding.card.startAnimation(AnimationUtils.loadAnimation(context,R.anim.pop))

                                binding.card.visibility= VISIBLE
                            }
                            oprv.adapter=recyclerviewadapter(options,pointer+1,question[pointer])
                        }
                        else
                        {
                            Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show()
                    }

                })
            }
            else
            {
                Log.i(TAG, "onViewCreated: pressed")
                pointer+=1
                Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
                navController.navigate(R.id.action_surveyquestions_to_letsstart)
            }
        }







    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }




}