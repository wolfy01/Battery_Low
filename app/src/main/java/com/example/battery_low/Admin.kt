package com.example.battery_low

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import org.json.JSONObject


class Admin : AppCompatActivity() {
    private lateinit var dbref: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var textInput: EditText
    private lateinit var SendButton: Button
    private lateinit var logoutButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        textInput = findViewById(R.id.textInput)
        SendButton = findViewById(R.id.SendButton)
        logoutButton = findViewById(R.id.logoutButton)

        SendButton.setOnClickListener {
            getUsersAndSendNotification(textInput.text.toString())
        }
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

    }
    private fun getUsersAndSendNotification(message: String) {
        dbref = FirebaseDatabase.getInstance().getReference("Users")
        dbref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (userSnapshot in dataSnapshot.children) {
                    val userToken = userSnapshot.child("token").getValue(String::class.java)
                    userToken?.let { sendNotification(message, it.toString()) }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun sendNotification(mString: String,token: String) {
        val jsonObject = JSONObject()
        val notificationObj = JSONObject().apply {
            put("title", "Admin")
            put("body", mString)
        }
        jsonObject.put("notification", notificationObj)
        jsonObject.put("to",token)
        callApi(jsonObject)


    }
    private fun callApi(jsonObject: JSONObject) {
        val JSON = "application/json;charset=utf-8".toMediaType()

        val client = OkHttpClient()

        val url = "https://fcm.googleapis.com/fcm/send"

        //val jsonObject = JSONObject() // Your JSON object here

        val body = jsonObject.toString().toRequestBody(JSON)

        val request = Request.Builder()
            .url(url)
            .post(body)
            .header("Authorization", "Bearer AAAAgpnaJmE:APA91bHtWbhDJBzD4ciFENo4PovIHbaq1HEfnscXJbgwSWN5VesnjCUMuuEtpJALe7bSLfG1fYsrIP_x1PBZ_UOhKau6P0RrJyj-eScqj0UBdaYovZ--rDAj2BvEWYA50a9inXdLUzjF")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure
            }

            override fun onResponse(call: Call, response: Response) {
                // Handle response
                // Note: This is called on the background thread, so make sure to switch to the UI thread if you need to update UI elements
            }
        })

    }

}