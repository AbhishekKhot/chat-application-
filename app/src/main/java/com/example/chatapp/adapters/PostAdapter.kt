package com.example.chatapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.R
import com.example.chatapp.util.TimerConvertor
import com.example.chatapp.data.Post
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.item_post.view.*

class PostAdapter(options:FirestoreRecyclerOptions<Post>, val listener:ItemPostAdapterClicked): FirestoreRecyclerAdapter<Post,PostAdapter.PostViewHolder>(options){

    class PostViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_post,parent,false))
        view.itemView.likeButton.setOnClickListener {
            listener.onLikedButtonClicked(snapshots.getSnapshot(view.adapterPosition).id)
        }
        return view
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, model: Post) {
        holder.itemView.PostText.text = model.text
        holder.itemView.UserName.text = model.createdBy.email
        holder.itemView.likeCount.text = model.likedBy.size.toString()
        holder.itemView.createdAt.text = TimerConvertor.getPostedTime(model.createdAt)

        val auth = Firebase.auth
        val user = auth.currentUser!!.uid
        val isLiked = model.likedBy.contains(user)
        if(isLiked){
            holder.itemView.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.itemView.likeButton.context,R.drawable.ic_favorite))
        }
        else{
            holder.itemView.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.itemView.likeButton.context,R.drawable.ic_favorite_border))
        }
    }

}

interface ItemPostAdapterClicked {
    fun onLikedButtonClicked(postId:String)
}