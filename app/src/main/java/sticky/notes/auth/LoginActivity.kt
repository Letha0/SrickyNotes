package sticky.notes.auth

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import sticky.notes.MainActivity
import sticky.notes.R


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var fAuth: FirebaseAuth
    private lateinit var spinner: ProgressBar
    private lateinit var fStore: FirebaseFirestore
    private lateinit var user: FirebaseUser

    private val TAG = "LoginActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true);
        supportActionBar!!.title = "Login to Sticky Notes";

        val login = findViewById<View>(R.id.login);
        spinner = findViewById(R.id.loading);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        login.setOnClickListener {

            val email: String = input_email.text.toString()
            val password: String = input_password.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this@LoginActivity, "Fields Are Required.", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            spinner.setVisibility(View.VISIBLE);


            fAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    Toast.makeText(this@LoginActivity, "Success !", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                }.addOnFailureListener { e ->
                    Toast.makeText(
                        this@LoginActivity,
                        "Login Failed. " + e.message,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    spinner.visibility = View.GONE
                }
        }

        btn_signUp.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }


        }

}