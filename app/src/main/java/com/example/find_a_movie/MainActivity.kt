package com.example.find_a_movie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class MainActivity : AppCompatActivity() {

    private lateinit var movieImages: MutableList<String>
    private lateinit var movieNames: MutableList<String>
    private lateinit var movieInfo: MutableList<String>
    private lateinit var rvMovies: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        movieImages = mutableListOf()
        movieNames = mutableListOf()
        movieInfo = mutableListOf()

        movieImages = mutableListOf()
        movieNames = mutableListOf()
        movieInfo = mutableListOf()
        rvMovies = findViewById(R.id.movie_list)

        getCurrentMovies()
    }

    private fun getCurrentMovies() {
        val apiKey = "017b97b7179fec2b73979d58f5d79972"
        val client = AsyncHttpClient()
        val url = "https://api.themoviedb.org/3/movie/now_playing?page=1&api_key=$apiKey"

        client[url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Movies", "response successful $json")
                val resArray = json.jsonObject.getJSONArray("results")

                for (i in 0 until resArray.length()) {
                    val movie = resArray.getJSONObject(i)
                    val path = movie.getString("poster_path")
                    val imageUrl = "https://image.tmdb.org/t/p/original$path"
                    val movieName = movie?.getString("original_title")
                    val movieDesc = movie?.getString("overview")
                    movieImages.add(imageUrl)
                    if (movieName != null) movieNames.add(movieName)
                    if (movieDesc != null) movieInfo.add(movieDesc)
                }

                val adapter = MovieAdapter(movieNames, movieImages, movieInfo)
                rvMovies.adapter = adapter
                rvMovies.layoutManager = LinearLayoutManager(this@MainActivity)
                rvMovies.addItemDecoration(DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL))

            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Movies Error", errorResponse)
            }
        }]
    }

    private fun searchMovies() {

    }
}