package com.kotlin.retrofit2.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlin.retrofit2.JsonPlaceHolderApi
import com.kotlin.retrofit2.ListRecyclerViewAdapter
import com.kotlin.retrofit2.PostItem
import com.kotlin.retrofit2.databinding.FragmentRecyclerBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory


class RecyclerFragment : Fragment() {

    private lateinit var binding: FragmentRecyclerBinding
    private lateinit var listRecyclerViewAdapter: ListRecyclerViewAdapter


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRecyclerBinding.inflate(inflater, container, false)

        initRecyclerView()
        binding.buttonGetData.setOnClickListener {
            try {
                println("GetData")
                CoroutineScope(IO).launch {
                    getData()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Something went Wrong", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private suspend fun getData() {

        try {
            var retrofit = Retrofit.Builder()
                    .baseUrl("https://jsonplaceholder.typicode.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            var jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)
            var response = jsonPlaceHolderApi.getData().awaitResponse()


            if (response.isSuccessful) {

                withContext(Main) {
                    val posts: ArrayList<PostItem> = response.body()!!
                    //var content = ""
                    listRecyclerViewAdapter.submitData(posts)
//                for (post in posts) {
//                    content += "\nID : ${post.id}"
//                    content += "\nUserID : ${post.userId}"
//                    content += "\nTitle : ${post.title}"
//                    content += "\nText : ${post.body}"
//                    //println(content)
//
//                }
                }
            } else {
                val post: PostItem = PostItem("None", 0, "None", 0)
                val posts = ArrayList<PostItem>()
                posts.add(post)
                listRecyclerViewAdapter.submitData(posts)

            }

        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Something went Wrong", Toast.LENGTH_SHORT).show()
        }

    }

    private fun initRecyclerView() {
        binding.recycleView.apply {
            layoutManager = LinearLayoutManager(binding.root.context)
            listRecyclerViewAdapter = ListRecyclerViewAdapter()
            adapter = listRecyclerViewAdapter
            Toast.makeText(binding.root.context, "init", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBundle("123", outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.getBundle("123")
    }

}