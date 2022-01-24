package romilp.socialmediaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import romilp.socialmediaapp.adapter.IPostAdapter
import romilp.socialmediaapp.adapter.PostAdapter
import romilp.socialmediaapp.auth.LoginIntro
import romilp.socialmediaapp.dao.PostDAO
import romilp.socialmediaapp.databinding.ActivityMainBinding
import romilp.socialmediaapp.models.Post

class MainActivity : AppCompatActivity(), IPostAdapter {

    lateinit var binding: ActivityMainBinding
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var postDao: PostDAO
    private lateinit var adapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        firebaseAuth = FirebaseAuth.getInstance()


        binding.fab.setOnClickListener {
            val intent = Intent(this, createPostActivity::class.java)
            startActivity(intent)
        }

        setUpRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.logout -> {
                firebaseAuth.signOut()
                val intent = Intent(this, LoginIntro::class.java)
                startActivity(intent)
                finishAfterTransition()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUpRecyclerView() {
        postDao = PostDAO()
        val postsCollections = postDao.postCollections
        val query = postsCollections.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()

        adapter = PostAdapter(recyclerViewOptions,this)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onLikeClicked(postId: String) {
        postDao.updateLikes(postId)
    }
}