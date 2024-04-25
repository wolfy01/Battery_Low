package com.example.battery_low

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging

class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var dbref: DatabaseReference
    private lateinit var logoutButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        logoutButton = findViewById(R.id.logoutButton)
        auth = FirebaseAuth.getInstance()
        getFCMToken()
        logoutButton.setOnClickListener {
            val user = auth.currentUser
            val userID = user!!.uid.toString()
            dbref = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("token")
            dbref.removeValue()
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun getFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if(it.isSuccessful){
                var token=it.result
                val user = auth.currentUser
                val userID=user!!.uid.toString()
                dbref= FirebaseDatabase.getInstance().getReference("Users").child(userID).child("token")
                dbref.setValue(token)
            }
        }
    }
}