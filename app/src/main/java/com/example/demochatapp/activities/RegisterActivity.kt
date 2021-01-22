package com.example.demochatapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.demochatapp.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUsers: DatabaseReference
    private var firebaseUserID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        FirebaseApp.initializeApp(applicationContext)

        val toolbar: Toolbar = findViewById(R.id.toolbar_register)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Register"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this@RegisterActivity, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        mAuth = FirebaseAuth.getInstance()
        btn_register.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val username: String = user_register.text.toString()
        val email: String = email_register.text.toString()
        val password: String = password_register.text.toString()

        if (username == "") {
            Toast.makeText(this@RegisterActivity, "please write username", Toast.LENGTH_SHORT)
        } else if (email == "") {
            Toast.makeText(this@RegisterActivity, "please write email", Toast.LENGTH_SHORT)

        } else if (password == "") {
            Toast.makeText(this@RegisterActivity, "please write password", Toast.LENGTH_SHORT)

        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        firebaseUserID = mAuth.currentUser!!.uid
                        refUsers = FirebaseDatabase.getInstance().reference.child("Users")
                            .child(firebaseUserID)
                        val userHashMap = HashMap<String, Any>()
                        userHashMap["uid"] = firebaseUserID
                        userHashMap["username"] = username
                        userHashMap["profile"] =
                            "https://firebasestorage.googleapis.com/v0/b/android-chat-app-47aee.appspot.com/o/loho3.png?alt=media&token=c0538674-27e9-4494-a441-a3f34b72c619"
                        userHashMap["cover"] = "https://firebasestorage.googleapis.com/v0/b/android-chat-app-47aee.appspot.com/o/115152.jpg?alt=media&token=207acaef-bf37-4d22-911f-e43a05130911"
                        userHashMap["status"] = "offline"
                        userHashMap["search"] = username.toLowerCase()
                        userHashMap["email"] = email
                        userHashMap["facebook"] = "https://m.facebook.com"
                        userHashMap["instagram"] = "https://m.instargram.com"
                        userHashMap["website"] = "https://www.google.com"

                        refUsers.updateChildren(userHashMap)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful){
                                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

                                    startActivity(intent)
                                    finish()
                                }
                            }


                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Error Message: " + task.exception!!.message.toString(),
                            Toast.LENGTH_SHORT
                        )

                    }

                }
        }
    }
}