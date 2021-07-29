package com.kotlin.retrofit2.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kotlin.retrofit2.JsonPlaceHolderApi
import com.kotlin.retrofit2.databinding.FragmentMainBinding
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory


class MainFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private val RESULT_1 = "RESULT #1"
    private var IDCount = 0
    private lateinit var binding: FragmentMainBinding


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMainBinding.inflate(inflater, container, false)

        binding.textView.movementMethod = ScrollingMovementMethod()
        binding.button.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                //fakeApiRequest()
                getData()
            }
        }
        return binding.root
    }

    private suspend fun setMainThread(input: String) {
        withContext(Dispatchers.IO) {
            setNewText(input)
        }
    }

    private fun setNewText(input: String) {
        //val newText = binding.textView.text.toString() + "\n$input"
        binding.textView.append(input)
    }

    private suspend fun fakeApiRequest() {
        val result1 = getResult1FromAPI()
        println("Debug : $result1")
        setMainThread(result1)
    }

    private suspend fun getData() {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        IDCount += 1
        println(IDCount)
        val jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi::class.java)
        val response = jsonPlaceHolderApi.getSingleData("$IDCount").awaitResponse()
        var content = ""

        if (response.isSuccessful) {
            val posts = response.body()!!
            content += "\nID : ${posts.id}"
            content += "\nUserID : ${posts.userId}"
            content += "\nTitle : ${posts.title}"
            content += "\nText : ${posts.body}"
            println(content)
            setMainThread(content)
//            for (post in posts){
//                content += "\nID : ${post.id}"
//                content += "\nUserID : ${post.userId}"
//                content += "\nTitle : ${post.title}"
//                content += "\nText : ${post.body}"
//                println(content)
//                setMainThread(content)
//            }

        } else {
            setMainThread(response.code().toString())
        }

    }


    private suspend fun getResult1FromAPI(): String {
        logResult("getResult1FromAPI")
        delay(1000)
        Thread.sleep(1000)
        return RESULT_1
    }

    private fun logResult(methodName: String) {
        println("Debug : ${methodName} : ${Thread.currentThread().name}")
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putString("Save Data", binding.textView.text.toString())
//    }
//
//    override fun onViewStateRestored(savedInstanceState: Bundle?) {
//        super.onViewStateRestored(savedInstanceState)
//        binding.textView.text = savedInstanceState?.getString("Save Data","")
//    }
}
