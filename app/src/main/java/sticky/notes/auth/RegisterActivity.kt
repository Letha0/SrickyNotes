package sticky.notes.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*
import sticky.notes.MainActivity
import sticky.notes.R
import sticky.notes.Validation

class RegisterActivity : AppCompatActivity() {


    //Firebase references
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mProgressBar: ProgressBar

    private val TAG = "RegisterActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("Users")
        mAuth = FirebaseAuth.getInstance()

        mProgressBar = findViewById(R.id.loading);

        register.setOnClickListener {

            val email = input_email.text.toString().trim()
            val password = input_password.text.toString().trim()
            val password_confirmation = input_password_conf.text.toString().trim()

            if(!validateFields(email, password, password_confirmation))
            {
                return@setOnClickListener
            }

            mProgressBar.visibility = View.VISIBLE;


            mAuth
                .createUserWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener(this) { task ->
                    //mProgressBar!!.hide()
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val userId = mAuth.currentUser!!.uid
                        //Verify Email
                        verifyEmail();
                        //update user profile information
                        val currentUserDb = mDatabaseReference!!.child(userId)
                        currentUserDb.child("email").setValue(email)
                        updateUserInfoAndUI()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        mProgressBar.visibility = View.INVISIBLE;
                        Toast.makeText(
                            this@RegisterActivity, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                }

        }
    }
    private fun verifyEmail() {
        val mUser = mAuth!!.currentUser;
        mUser!!.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@RegisterActivity,
                        "Verification email sent to " + mUser.getEmail(),
                        Toast.LENGTH_SHORT).show()
                } else {
                    Log.e(TAG, "sendEmailVerification", task.exception)
                    Toast.makeText(this@RegisterActivity,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT).show()
                }
            }

        btn_signIn.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateUserInfoAndUI() {
        //start next activity
        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun validateFields(email: String, password:String, password_confirmation:String): Boolean {

        if (email.isEmpty()) {
            input_email.error = "Email required"
            input_email.requestFocus()
            return false
        }


        if (!Validation.isValidEmail(email)) {
            input_email.error = "Enter correct email"
            input_email.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            input_password.error = "Password required"
            input_password.requestFocus()
            return false
        }

        if (password_confirmation.isEmpty()) {
            input_password_conf.error = "Password confirmation required"
            input_password_conf.requestFocus()
            return false
        }

        if (password_confirmation != password) {
            input_password_conf.error = "Passwords fields are not equals"
            input_password_conf.requestFocus()
            return  false
        }

        return true

    }

}
