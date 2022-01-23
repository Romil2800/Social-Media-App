package romilp.socialmediaapp.auth

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import romilp.socialmediaapp.DAO.userDAO
import romilp.socialmediaapp.MainActivity
import romilp.socialmediaapp.databinding.ActivitySignUpBinding
import romilp.socialmediaapp.models.User
import romilp.socialmediaapp.R
import java.io.ByteArrayOutputStream


class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    lateinit var firebaseAuth: FirebaseAuth

    // instance for firebase storage and StorageReference
    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null

    private val REQUEST_IMAGE_CAMERA = 142
    private val REQUEST_IMAGE_GALLERY = 132


    lateinit var builder: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        firebaseAuth = FirebaseAuth.getInstance()
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_sign_up)


        builder = AlertDialog.Builder(this)
        binding.personImage.setOnClickListener {

            //Setting message manually and performing action on button click
            builder.setPositiveButton("Camera", DialogInterface.OnClickListener { dialog, id ->
                dialog.dismiss()
                Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                    takePictureIntent.resolveActivity(packageManager)?.also {
                        val permission = ContextCompat.checkSelfPermission(
                            this,
                            android.Manifest.permission.CAMERA
                        )

                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAMERA)

                    }
                }
            })
                .setNegativeButton(
                    "Galllery",
                    DialogInterface.OnClickListener { dialog, id -> //  Action for 'NO' Button
                        dialog.cancel()
                        val intent = Intent(Intent.ACTION_PICK)
                        intent.type = "image/"
                        startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
                    })
            //Creating dialog box
            val alert: AlertDialog = builder.create()
            //Setting the title manually
            alert.setTitle("Choose your Image")
            alert.show()
        }

        binding.btnSignUp.setOnClickListener {
            signUpUser()
        }
        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finishAfterTransition()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            binding.personImage.setImageURI(data.data)
        } else if (requestCode == REQUEST_IMAGE_CAMERA && resultCode == Activity.RESULT_OK && data != null) {
            binding.personImage.setImageBitmap(data.extras?.get("data") as Bitmap)
        } else {
            Toast.makeText(
                applicationContext, "you are wrong",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun signUpUser() {

        val email: String = binding.etEmailAddress.text.toString()
        val password: String = binding.etPassword.text.toString()
        val confirmPassword: String = binding.etConfirmPassword.text.toString()


        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_LONG).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_LONG)
                .show()
            return
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                val user = User(
                    firebaseAuth.currentUser!!.uid,
                    binding.etName.text.toString(),
                    binding.personImage.toString()
                )
                val userDAO = userDAO()
                userDAO.addUser(user)
                Toast.makeText(this, "Sign Up Successful", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finishAfterTransition()
            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
            }
        }
    }
}