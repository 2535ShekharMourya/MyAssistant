package com.example.mygoogleassistant.assistant

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mygoogleassistant.R
import com.example.mygoogleassistant.data.newsmodel.Article
import com.example.mygoogleassistant.uiscreens.WebViewActivity
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class NewsAdapter(private val newsList: List<Article>?,private val context:Context) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    // ViewHolder class for item layout
    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val urlImage: ImageView = itemView.findViewById(R.id.url_image)
        val autherName: TextView = itemView.findViewById(R.id.auther)
        val title=itemView.findViewById<TextView>(R.id.news_title)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.news_items, parent, false)
        return NewsViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = newsList?.get(position)

        // Assuming NewsItem has properties: imageUrl and title
        holder.autherName.text = newsItem?.author
        holder.title.text=newsItem?.title
        // Assume imageView is your ImageView and imageUrl is the URL of the image
        Picasso.get()
            .load(newsItem?.urlToImage)
            .into(holder.urlImage)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra("URL_KEY", newsItem?.url)  // Pass the URL to the WebViewActivity
            context.startActivity(intent)}
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int = newsList?.size as Int
}
