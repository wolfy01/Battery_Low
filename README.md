# Battery Low – Firebase Push Notification App

This Android app demonstrates how to integrate **Firebase Authentication**, **Realtime Database**, and **Firebase Cloud Messaging (FCM)** to handle **user login, logout, and sending push notifications**.  

The app has two main roles:
1. **Normal User** – can log in, generate and save their FCM token in Firebase.
2. **Admin** – can send notifications to one user or all users by fetching tokens from Firebase.

---

## 🚀 Features

- Firebase **Authentication** (Email/Password login & logout)
- Save **FCM device token** for each logged-in user inside Realtime Database
- **Automatic token refresh** handling
- Admin panel to:
  - Send push notification to a single user by token
  - Send push notification to **all users** by fetching tokens from the database
- Logout clears user session and removes token from Firebase

---

## 📂 Project Structure

app/
├── java/com/example/battery_low/
│ ├── MainActivity.kt # User login / entry point
│ ├── HomeActivity.kt # Logged-in user home, saves FCM token
│ ├── Admin.kt # Admin panel to send notifications
│ ├── MyFirebaseService.kt # Handles incoming notifications
│
└── res/layout/
├── activity_main.xml
├── activity_home.xml
├── activity_admin.xml

yaml
Copy code

---

## 🔧 Setup Instructions

1. **Clone the repo**
   ```bash
   git clone https://github.com/your-repo/battery-low.git
Connect Firebase

Create a Firebase project

Enable Authentication → Email/Password

Create a Realtime Database

Add your google-services.json file in app/

Dependencies
Add the following in app/build.gradle:

gradle
Copy code
implementation platform('com.google.firebase:firebase-bom:32.0.0')
implementation 'com.google.firebase:firebase-auth'
implementation 'com.google.firebase:firebase-database'
implementation 'com.google.firebase:firebase-messaging'
implementation("com.squareup.okhttp3:okhttp:4.12.0")
Update AndroidManifest.xml

xml
Copy code
<service
    android:name=".MyFirebaseService"
    android:exported="false">
    <intent-filter>
        <action android:name="com.google.firebase.MESSAGING_EVENT"/>
    </intent-filter>
</service>
Server Key

Replace the Authorization header in Admin.kt with your Firebase Cloud Messaging Server Key.

🔑 Key Code Snippets
Save User FCM Token
kotlin
Copy code
FirebaseMessaging.getInstance().token.addOnCompleteListener {
    if (it.isSuccessful) {
        val token = it.result
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        val dbref = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("token")
        dbref.setValue(token)
    }
}
Remove Token on Logout
kotlin
Copy code
FirebaseAuth.getInstance().signOut()
val userID = FirebaseAuth.getInstance().currentUser?.uid
if (userID != null) {
    FirebaseDatabase.getInstance().getReference("Users").child(userID).child("token").removeValue()
}
Send Notification (Admin)
kotlin
Copy code
val notificationObj = JSONObject().apply {
    put("title", "Admin")
    put("body", "This is a test message")
}

val jsonObject = JSONObject().apply {
    put("notification", notificationObj)
    put("to", userToken) // Single user token or topic
}

callApi(jsonObject)
Get All Tokens from Database
kotlin
Copy code
dbref.addListenerForSingleValueEvent(object : ValueEventListener {
    override fun onDataChange(snapshot: DataSnapshot) {
        for (userSnapshot in snapshot.children) {
            val token = userSnapshot.child("token").getValue(String::class.java)
            if (!token.isNullOrEmpty()) {
                sendNotification("Message body", token)
            }
        }
    }
    override fun onCancelled(error: DatabaseError) {}
})
📱 Example Firebase Database Structure
nginx
Copy code
Users
 ├── 1Rt17gFmJnRq2cPk1elIRLTEz5V2
 │    ├── userID: "1Rt17gFmJnRq2cPk1elIRLTEz5V2"
 │    ├── userName: "Nihal Azman"
 │    └── token: "fcm_token_here"
 ├── K7e64LTKZbP3UILPiBCP1vVCGAG3
 │    ├── userID: "K7e64LTKZbP3UILPiBCP1vVCGAG3"
 │    ├── userName: "abc hasan"
 │    └── token: "fcm_token_here"
🛠 Tech Stack
Kotlin for Android development

Firebase Auth for login/logout

Firebase Realtime Database for storing tokens

Firebase Cloud Messaging (FCM) for push notifications

OkHttp for sending API requests to FCM

✅ Future Improvements
Add topics-based messaging (subscribeToTopic("all")) instead of sending to each token.

Improve UI for Admin panel (list users, select users).

Add login/signup for Admin separately.

👨‍💻 Author
Built by Nihal Azman
