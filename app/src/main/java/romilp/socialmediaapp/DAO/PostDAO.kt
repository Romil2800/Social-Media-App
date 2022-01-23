package romilp.socialmediaapp.DAO

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import romilp.socialmediaapp.models.Post
import romilp.socialmediaapp.models.User

class PostDAO {
    val db = FirebaseFirestore.getInstance()
    val postCollections = db.collection("posts")
    val auth = Firebase.auth
    val userDAO = UserDAO()

    fun addPost(text: String) {
        val currrentUserId = auth.currentUser!!.uid
        GlobalScope.launch {

            //get User
            val user = userDAO.getUserById(currrentUserId).await().toObject(User::class.java)!!

            //Current Time
            val currentTime = System.currentTimeMillis()
            val post = Post(text, user, currentTime)
            postCollections.document().set(post)
        }
    }
}