package hku.hk.cs.a3330gp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class SignupActivity : AppCompatActivity() {
    //check
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var signupButton: Button
    private lateinit var loginRedirect: TextView
    private lateinit var signupUsername: EditText
    private lateinit var signupPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        databaseHelper = DatabaseHelper(this)
        signupButton = findViewById(R.id.signupButton)
        loginRedirect = findViewById(R.id.loginRedirect)
        signupUsername = findViewById(R.id.signupUsername)
        signupPassword = findViewById(R.id.signupPassword)

        signupButton.setOnClickListener{
            val signupUsername = signupUsername.text.toString()
            val signupPassword = signupPassword.text.toString()
            signupDatabase(signupUsername, signupPassword)
        }

        loginRedirect.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun signupDatabase(username: String, password: String){
        val insertedRowId = databaseHelper.insertUser(username, password)
        if(insertedRowId != -1L){
            Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            Toast.makeText(this, "Signup Failed", Toast.LENGTH_SHORT).show()
        }
    }
}