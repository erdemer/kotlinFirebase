package com.erdemer.kotlininstagramfirebase.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.erdemer.kotlininstagramfirebase.Model.Post
import com.erdemer.kotlininstagramfirebase.R
import com.squareup.picasso.Picasso

class RVAdapter(
    private val postsArrayList: ArrayList<Post>
) : RecyclerView.Adapter<RVAdapter.PostHolder>() {
    class PostHolder(view: View) : RecyclerView.ViewHolder(view) {
        var recyclerViewUserNameText: TextView? = null
        var recyclerViewCommentText: TextView? = null
        var profilePhoto: ImageView? = null
        var uploadPhoto: ImageView? = null
        var likeButton : Button? = null

        init {
            recyclerViewUserNameText = view.findViewById(R.id.textViewUsername)
            recyclerViewCommentText = view.findViewById(R.id.textViewComment)
            profilePhoto = view.findViewById(R.id.profile_photo)
            uploadPhoto = view.findViewById(R.id.imageViewPicture)
            likeButton = view.findViewById(R.id.likeButton)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.feed_row, parent, false)
        return PostHolder(view)
    }

    override fun getItemCount(): Int {
      return postsArrayList.size
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
       holder.recyclerViewUserNameText?.text = postsArrayList[position].userName
        holder.recyclerViewCommentText?.text = postsArrayList[position].comment
        Picasso.get().load(postsArrayList[position].profilePhoto).into(holder.profilePhoto)
        Picasso.get().load(postsArrayList[position].uploadImg).into(holder.uploadPhoto)
        var isClicked : Boolean = false
        holder.likeButton?.setOnClickListener {it ->
            if (!isClicked) {
                holder.likeButton!!.setBackgroundResource(R.drawable.ic_baseline_favorite_24)
                isClicked = true
            } else {
                holder.likeButton!!.setBackgroundResource(R.drawable.ic_baseline_favorite_24_shadow)
                isClicked = false
        }
        }

    }
}