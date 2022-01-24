package romilp.socialmediaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import romilp.socialmediaapp.dao.PostDAO
import romilp.socialmediaapp.databinding.ActivityCreatePostBinding

class createPostActivity : AppCompatActivity() {

    lateinit var binding: ActivityCreatePostBinding
    private lateinit var postDAO: PostDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_post)

        postDAO = PostDAO()
        binding.postButton.setOnClickListener {
            val input = binding.postInput.text.trim().toString()
            if (input.isNotEmpty()) {
                postDAO.addPost(input)
                finish()
            }
        }

    }
}