package com.raywenderlich.placebook.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.placebook.R
import com.raywenderlich.placebook.ui.MapsActivity
import com.raywenderlich.placebook.viewmodel.MapsViewModel.BookmarkView
import kotlinx.android.synthetic.main.bookmark_item.view.*

class BookmarkListAdapter(
    private var bookmarkData: List<BookmarkView>?,
    private val mapsActivity: MapsActivity
) : RecyclerView.Adapter<BookmarkListAdapter.ViewHolder>() {

    class ViewHolder(
        view: View,
        private val mapsActivity: MapsActivity
    ) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.bookmarkNameText
        val categoryImageView: ImageView = view.bookmarkIcon
    }

    fun setBookmarkData(bookmarks: List<BookmarkView>) {
        bookmarkData = bookmarks
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.bookmark_item, parent, false),
            mapsActivity
        )
    }

    override fun getItemCount(): Int {
        return bookmarkData?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bookmarkData = bookmarkData ?: return
        val bookmarkViewData = bookmarkData[position]

        holder.itemView.tag = bookmarkViewData
        holder.nameTextView.text = bookmarkViewData.name
        holder.categoryImageView.setImageResource(R.drawable.ic_other)
    }
}