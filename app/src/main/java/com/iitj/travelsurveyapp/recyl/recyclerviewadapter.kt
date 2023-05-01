package com.iitj.travelsurveyapp.recyl

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import com.iitj.travelsurveyappiitj.R
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class recyclerviewadapter(var list: MutableList<String>,var qid:Int,var q: String):RecyclerView.Adapter<viewHolder>()
    {
        private lateinit var auth: FirebaseAuth
        private lateinit var refer: DatabaseReference


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        var optionview=LayoutInflater.from(parent.context).inflate(R.layout.optionslayout,parent,false)

        auth= FirebaseAuth.getInstance()
        refer=FirebaseDatabase.getInstance().getReference("user").child(auth.currentUser?.uid.toString())
        
        return viewHolder(optionview,q)
    }


    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        
        holder.data.text=list[position]
        //refer=FirebaseDatabase.getInstance().reference
        //auth= FirebaseAuth.getInstance()
        holder.box.setOnClickListener()

        {

            //refer.child("user").child(auth.currentUser?.uid.toString()).child("responces").child(q).setValue(list[position])
            /*refer.addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists())
                    {
                        Toast.makeText(holder.itemView.context, "data exist", Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(holder.itemView.context, "doesn't exist", Toast.LENGTH_SHORT).show()
                }
            })*/
             //addresponce(q,list[position])
            refer.child("responces").child(qid.toString()).setValue(list[position])
            holder.box.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context,R.anim.pop))
            Toast.makeText(holder.itemView.context, "Responce Updated", Toast.LENGTH_SHORT).show()

        }
    }

        private fun addresponce(q: String, s: String) {
            refer.child("responce").child(q).setValue(s)

        }

        override fun getItemCount(): Int {
        return list.size
    }
}
class viewHolder(view: View,q: String):RecyclerView.ViewHolder(view)
{   var ques=q
    var data: TextView =view.findViewById<TextView>(R.id.option)
    var box: CardView =view.findViewById<CardView>(R.id.box)

}
