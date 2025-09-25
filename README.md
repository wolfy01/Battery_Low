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

🔧 Setup Instructions

Clone the repo: git clone https://github.com/wolfy01/Battery_Low.git

Connect Firebase

Create a Firebase project

Enable Authentication → Email/Password

Create a Realtime Database

Add your google-services.json file into the app/ folder

Add dependencies in app/build.gradle:

implementation platform('com.google.firebase:firebase-bom:32.0.0')

implementation 'com.google.firebase:firebase-auth'

implementation 'com.google.firebase:firebase-database'

implementation 'com.google.firebase:firebase-messaging'

implementation("com.squareup.okhttp3:okhttp:4.12.0")

Update AndroidManifest.xml:

Add Firebase service declarations

Request POST_NOTIFICATIONS permission (for Android 13+)

Replace the Authorization header in Admin.kt with your Firebase Cloud Messaging Server Key


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

Built by Nihal Azman
GitHub: @wolfy01
