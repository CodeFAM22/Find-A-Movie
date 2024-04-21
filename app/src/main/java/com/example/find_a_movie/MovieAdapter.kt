package com.example.find_a_movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MovieAdapter(
    private val movieNames: List<String>,
    private val movieImages: List<String>,
    private val movieInfo: List<String>,
    private val movieIDs: List<String>,
    private val onMovieClickListener: (String, String, String) -> Unit
) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            itemView.setOnClickListener {
                val id = movieIDs[position]
                val name = movieNames[position]
                val desc = movieInfo[position]
                onMovieClickListener(id, name, desc)
            }
            // Bind data to views
            itemView.findViewById<TextView>(R.id.movie_name).text = movieNames[position]
            itemView.findViewById<TextView>(R.id.movie_info).text = movieInfo[position]
            Glide.with(itemView)
                .load(movieImages[position])
                .centerCrop()
                .into(itemView.findViewById(R.id.movie_image))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = movieNames.size
}
