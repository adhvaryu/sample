package com.kotlin.retrofit2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kotlin.retrofit2.databinding.LayoutListItemBinding

class ListRecyclerViewAdapter : RecyclerView.Adapter<ListRecyclerViewAdapter.PostViewHolder>() {

    private var items: List<PostItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PostViewHolder(LayoutListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val itemView = holder.itemBinding
        val postItem = items[position]
        itemView.textId.text = "Id: " + postItem.id.toString()
        itemView.textUserId.text = "UserID: " + postItem.userId.toString()
        itemView.textTitle.text = "Title: " + postItem.title
        itemView.textBody.text = "Text: " + postItem.body

        Glide.with(itemView.root.context)
                .load("https://picsum.photos/id/${postItem.id}/200/300")
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(itemView.imageView)
        println("BindCalled")

    }

    override fun getItemCount() = items.size

    fun submitData(postList: ArrayList<PostItem>) {
        items = postList
        notifyDataSetChanged()
    }

    class PostViewHolder(val itemBinding: LayoutListItemBinding) : RecyclerView.ViewHolder(itemBinding.root)


}




