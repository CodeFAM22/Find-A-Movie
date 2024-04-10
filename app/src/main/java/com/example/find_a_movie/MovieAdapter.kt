package com.example.find_a_movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MovieAdapter (private val movieNames: List<String>, private val movieImages: List<String>, private val movieInfo: List<String>) : RecyclerView.Adapter<MovieAdapter.ViewHolder>(){
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val movieImage: ImageView
        val movieName: TextView
        val movieInfo: TextView

        init {
            // Find our RecyclerView item's ImageView for future use
            movieImage = view.findViewById(R.id.movie_image)
            movieName = view.findViewById(R.id.movie_name)
            movieInfo = view.findViewById(R.id.movie_info)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(movieImages[position])
            .centerCrop()
            .into(holder.movieImage)

        holder.movieName.text = movieNames[position]
        if (movieInfo[position] != "") {
            holder.movieInfo.text = movieInfo[position]
        } else {
            holder.movieInfo.text = "Uh Oh! No description for this movie :("
        }

        holder.movieImage.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Movie at position $position clicked", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = movieNames.size
}