package com.example.find_a_movie

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import okhttp3.Headers


class MainActivity : AppCompatActivity() {

    private lateinit var movieImages: MutableList<String>
    private lateinit var movieNames: MutableList<String>
    private lateinit var movieInfo: MutableList<String>
    private lateinit var movieIDs: MutableList<String>
    private lateinit var rvMovies: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchLayout = findViewById<TextInputLayout>(R.id.search_layout)
        val searchValue = findViewById<TextInputEditText>(R.id.search_bar)
        searchLayout.setEndIconOnClickListener {
            getSearchMovies(searchValue.text.toString())
        }

        movieImages = mutableListOf()
        movieNames = mutableListOf()
        movieInfo = mutableListOf()
        movieIDs = mutableListOf()
        rvMovies = findViewById(R.id.movie_list)

        getCurrentMovies()
    }

    private fun getSearchMovies(query: String) {
        val apiKey = "017b97b7179fec2b73979d58f5d79972" // Replace with your API key
        val client = AsyncHttpClient()
        val url = "https://api.themoviedb.org/3/search/movie?query=$query&page=1&api_key=$apiKey"

        movieImages.clear()
        movieNames.clear()
        movieInfo.clear()
        movieIDs.clear()

        client[url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                val resArray = json.jsonObject.getJSONArray("results")

                for (i in 0 until resArray.length()) {
                    val movie = resArray.getJSONObject(i)
                    val path = movie.getString("poster_path")
                    val imageUrl = "https://image.tmdb.org/t/p/original$path"
                    val movieID = movie.getString("id") // Fetch movie ID
                    val movieName = movie.getString("original_title") // Use getString instead of nullable getString
                    val movieDesc = movie.getString("overview") // Use getString instead of nullable getString
                    movieImages.add(imageUrl)
                    movieNames.add(movieName)
                    movieInfo.add(movieDesc)
                    movieIDs.add(movieID) // Add movie ID to the list
                }

                val adapter = MovieAdapter(movieNames, movieImages, movieInfo, movieIDs) { movieID, movieName, movieDesc ->
                    // Handle movie click event
                    val intent = Intent(this@MainActivity, MovieDetailActivity::class.java).apply {
                        putExtra("movieID", movieID)
                        putExtra("movieName", movieName)
                        putExtra("movieDesc", movieDesc)
                    }
                    startActivity(intent)
                }
                rvMovies.adapter = adapter
                rvMovies.layoutManager = LinearLayoutManager(this@MainActivity)
                rvMovies.addItemDecoration(DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL))
            }

            override fun onFailure(statusCode: Int,
                                   headers: Headers?,
                                   errorResponse: String,
                                   throwable: Throwable?) {
                TODO("Not yet implemented")
            }
        }]
    }

    private fun getCurrentMovies() {
        val apiKey = "017b97b7179fec2b73979d58f5d79972" // Replace with your API key
        val client = AsyncHttpClient()
        val url = "https://api.themoviedb.org/3/movie/now_playing?page=1&api_key=$apiKey"

        client[url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                val resArray = json.jsonObject.getJSONArray("results")

                for (i in 0 until resArray.length()) {
                    val movie = resArray.getJSONObject(i)
                    val path = movie.getString("poster_path")
                    val imageUrl = "https://image.tmdb.org/t/p/original$path"
                    val movieID = movie.getString("id") // Fetch movie ID
                    val movieName = movie.getString("original_title") // Use getString instead of nullable getString
                    val movieDesc = movie.getString("overview") // Use getString instead of nullable getString
                    movieImages.add(imageUrl)
                    movieNames.add(movieName)
                    movieInfo.add(movieDesc)
                    movieIDs.add(movieID) // Add movie ID to the list
                }

                val adapter = MovieAdapter(movieNames, movieImages, movieInfo, movieIDs) { movieID, movieName, movieDesc ->
                    // Handle movie click event
                    val intent = Intent(this@MainActivity, MovieDetailActivity::class.java).apply {
                        putExtra("movieID", movieID)
                        putExtra("movieName", movieName)
                        putExtra("movieDesc", movieDesc)
                    }
                    startActivity(intent)
                }
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
                // Handle failure
            }
        }]
    }
}
