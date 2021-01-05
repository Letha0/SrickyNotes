package sticky.notes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import android.os.Handler
import sticky.notes.auth.LoginActivity

class SplashScreen : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spash_screen)

        var user = FirebaseAuth.getInstance().currentUser

        Handler().postDelayed({
                if(user!=null) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                else {
                    startActivity(Intent(this,LoginActivity::class.java))
                    finish()
                }
        }, 3000)


    }
}