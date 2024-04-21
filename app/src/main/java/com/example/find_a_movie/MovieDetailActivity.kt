package com.example.find_a_movie

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.example.find_a_movie.R
import okhttp3.Headers
import org.json.JSONException

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var movieImage: ImageView
    private lateinit var movieName: TextView
    private lateinit var movieDesc: TextView
    private lateinit var movie_ReleaseDate: TextView
    private lateinit var movie_OriginalLanguage: TextView
    private lateinit var movie_GenreIDs: TextView


    fun onBackButtonClick(view: View) {
        finish() // Navigate back to the previous activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        movieImage = findViewById(R.id.movie_detail_image)
        movieName = findViewById(R.id.movie_detail_name)
        movieDesc = findViewById(R.id.movie_detail_desc)
        movie_ReleaseDate = findViewById(R.id.movie_release_date)
        movie_OriginalLanguage = findViewById(R.id.movie_original_language)
        movie_GenreIDs = findViewById(R.id.movie_genre_ids)


        // Fetch movie details from API
        val movieID = intent.getStringExtra("movieID")
        if (movieID != null) {
            Log.d("movieID", movieID)
            fetchMovieDetails(movieID)
        } else {
            Log.d("movieID", "ERROR:COULD NOT FIND MOVIE ID")
        }
    }

    private fun fetchMovieDetails(movieID: String) {
        val apiKey = "017b97b7179fec2b73979d58f5d79972"
        val client = AsyncHttpClient()
        val url = "https://api.themoviedb.org/3/movie/$movieID?api_key=$apiKey"

        client[url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                try {
                    Log.d("MOVIE", json.jsonObject.toString())
                    val movieTitle = json.jsonObject.getString("title")
                    val movieOverview = json.jsonObject.getString("overview")
                    val movieReleaseDate = json.jsonObject.getString("release_date")
                    val movieOriginalLanguage = json.jsonObject.getString("original_language")
                    val movieGenreIDs = json.jsonObject.getJSONArray("genres")
                    val moviePosterPath = json.jsonObject.getString("poster_path")

                    val genreNames = StringBuilder()
                    var fetchedGenres = 0

                    for (i in 0 until movieGenreIDs.length()) {
                        val genreID = movieGenreIDs.getJSONObject(i).getInt("id")

                        getGenreName(genreID) { genreName ->
                            if (fetchedGenres > 0) {
                                genreNames.append(", ")
                            }
                            genreNames.append(genreName)

                            fetchedGenres++

                            if (fetchedGenres == movieGenreIDs.length()) {
                                // All genre names fetched, set the text of the genre_IDs TextView
                                runOnUiThread {
                                    movie_GenreIDs.text = "Genre IDs: ${genreNames.toString()}"
                                }
                            }
                        }
                    }

                    movieName.text = movieTitle
                    movieDesc.text = movieOverview
                    movie_ReleaseDate.text = "Release Date: $movieReleaseDate"
                    movie_OriginalLanguage.text = "Original Language: $movieOriginalLanguage"
//                    movie_GenreIDs.text = "Genres: ${genreNames.toString()}"
                    Glide.with(this@MovieDetailActivity)
                        .load("https://image.tmdb.org/t/p/original$moviePosterPath")
                        .apply(RequestOptions().placeholder(R.drawable.placeholder_image))
                        .into(movieImage)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.e("ERROR", "ERROR")            }
        }]
    }

    private fun getGenreName(genreID: Int, callback: (String) -> Unit) {
        val apiKey = "017b97b7179fec2b73979d58f5d79972"
        val client = AsyncHttpClient()

        // URL to fetch the list of movie genres
        val genreListUrl = "https://api.themoviedb.org/3/genre/movie/list?api_key=$apiKey"

        client.get(genreListUrl, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                try {
                    val genresArray = json.jsonObject.getJSONArray("genres")

                    // Iterate through the genre array to find the genre name for the given genre ID
                    for (i in 0 until genresArray.length()) {
                        val genreObject = genresArray.getJSONObject(i)
                        val id = genreObject.getInt("id")
                        val name = genreObject.getString("name")

                        if (id == genreID) {
                            callback(name)
                            return
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.e("ERROR", "Error fetching genre list.")
            }
        })
    }



}
