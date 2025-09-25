🔋 Battery Low – Firebase Push Notification App

An Android app demonstrating Firebase Authentication, Realtime Database, and Firebase Cloud Messaging (FCM) for handling user login/logout and sending push notifications.

The app supports two roles:

👤 Normal User – Logs in, generates, and saves their FCM token in Firebase.

👨‍💻 Admin – Sends notifications to a single user (via token) or all users (by fetching tokens from Firebase).

✨ Features

🔑 Firebase Authentication (Email/Password login & logout)

💾 Save FCM device token for each logged-in user inside Realtime Database

🔄 Automatic token refresh handling

📢 Admin panel to:

Send push notification to a single user by token

Send push notification to all users by fetching tokens from the database

🚪 Logout clears user session and removes token from Firebase

📂 Project Structure
app/
 ├── java/com/example/battery_low/
 │    ├── MainActivity.kt        # User login / entry point
 │    ├── HomeActivity.kt        # Logged-in user home, saves FCM token
 │    ├── Admin.kt               # Admin panel to send notifications
 │    ├── MyFirebaseService.kt   # Handles incoming notifications
 │
 └── res/layout/
      ├── activity_main.xml
      ├── activity_home.xml
      ├── activity_admin.xml


🔧 Setup Instructions
1. Clone the repo
git clone https://github.com/wolfy01/Battery_Low.git

. Connect Firebase

Create a Firebase project

Enable Authentication → Email/Password

Create a Realtime Database

Add your google-services.json file into the app/ folder

3. Add dependencies

In app/build.gradle:

implementation platform('com.google.firebase:firebase-bom:32.0.0')
implementation 'com.google.firebase:firebase-auth'
implementation 'com.google.firebase:firebase-database'
implementation 'com.google.firebase:firebase-messaging'
implementation("com.squareup.okhttp3:okhttp:4.12.0")

4. Update AndroidManifest.xml

Add Firebase service declarations

Request POST_NOTIFICATIONS permission for Android 13+

5. Server Key

Replace the Authorization header in Admin.kt with your Firebase Cloud Messaging Server Key.

🔑 Key Code Snippets

Save User FCM Token

FirebaseMessaging.getInstance().token.addOnCompleteListener {
    if (it.isSuccessful) {
        val token = it.result
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        val dbref = FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(userID)
            .child("token")
        dbref.setValue(token)
    }
}


Remove Token on Logout

FirebaseAuth.getInstance().signOut()
val userID = FirebaseAuth.getInstance().currentUser?.uid
if (userID != null) {
    FirebaseDatabase.getInstance()
        .getReference("Users")
        .child(userID)
        .child("token")
        .removeValue()
}


Send Notification (Admin)

val notificationObj = JSONObject().apply {
    put("title", "Admin")
    put("body", "This is a test message")
}

val jsonObject = JSONObject().apply {
    put("notification", notificationObj)
    put("to", userToken) // Single user token or "topic"
}

callApi(jsonObject)


Get All Tokens from Database

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

Kotlin – Android development

Firebase Auth – login/logout

Firebase Realtime Database – store user tokens

Firebase Cloud Messaging (FCM) – push notifications

OkHttp – send API requests to FCM

✅ Future Improvements

Use topics-based messaging (subscribeToTopic("all")) instead of iterating over tokens

Improved Admin UI (list users, select recipients)

Separate login/signup flow for Admin users

👨‍💻 Author

Built with ❤️ by Nihal Azman
GitHub: @wolfy01
