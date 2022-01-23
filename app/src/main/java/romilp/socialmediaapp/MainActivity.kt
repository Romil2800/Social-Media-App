package romilp.socialmediaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import romilp.socialmediaapp.auth.LoginIntro
import romilp.socialmediaapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.button.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(this, LoginIntro::class.java)
            startActivity(intent)
            finishAfterTransition()
        }
    }
}