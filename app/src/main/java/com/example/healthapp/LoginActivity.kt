package com.example.healthapp


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.healthapp.databinding.ActivityLoginBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        binding.loginButton.setOnClickListener {
            val loginName = binding.loginName.text.toString()
            val emailId = binding.emailId.text.toString()

            if (loginName.isNotEmpty() && emailId.isNotEmpty()) {
                loginUser(loginName, emailId)
            } else {
                Toast.makeText(this@LoginActivity, "All fields are mandatory", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(name: String, email: String) {
        databaseReference.orderByChild("name").equalTo(name).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // User doesn't exist, create a new user
                    val id = databaseReference.push().key
                    val userData = UserData(id, name, email)
                    databaseReference.child(id!!).setValue(userData).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@LoginActivity, "User created successfully", Toast.LENGTH_SHORT).show()
                            navigateToMainActivity() // Navigate to MainActivity
                        } else {
                            Toast.makeText(this@LoginActivity, "Failed to create user", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // User exists
                    Toast.makeText(this@LoginActivity, "User already exists, logging in...", Toast.LENGTH_SHORT).show()
                    navigateToMainActivity() // Navigate to MainActivity
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@LoginActivity, "Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Close LoginActivity
    }
}
