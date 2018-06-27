package akhmedoff.usman.videoforvk.ui.profile

import akhmedoff.usman.data.model.Album
import akhmedoff.usman.data.model.Video
import akhmedoff.usman.videoforvk.R
import akhmedoff.usman.videoforvk.ui.view.holders.ProfileAlbumsSectorViewHolder
import akhmedoff.usman.videoforvk.ui.view.holders.SearchViewHolder
import android.arch.paging.PagedList
import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso

class ProfileRecyclerAdapter(private val videoClickListener: (Video, View) -> Unit,
                             private val albumClickListener: (Album, View) -> Unit,
                             private val albumsClickListener: (View) -> Unit) : PagedListAdapter<Video, RecyclerView.ViewHolder>(CATALOG_COMPARATOR) {

    var albums: PagedList<Album>? = null
        set(value) {
            field = value
            notifyItemChanged(0)
        }

    companion object {
        val CATALOG_COMPARATOR = object : DiffUtil.ItemCallback<Video>() {
            override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean =
                    oldItem.id == newItem.id && oldItem.ownerId == newItem.ownerId

            override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean =
                    oldItem.title == newItem.title && oldItem.photo130 == newItem.photo130
        }
    }

    override fun getItemViewType(position: Int): Int = position

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            if (viewType == 0 && albums?.isNotEmpty() == true) {
                ProfileAlbumsSectorViewHolder(albumClickListener, LayoutInflater.from(parent.context).inflate(R.layout.albums_item, parent, false)).apply {
                    cardView.setOnClickListener {
                        cardView.transitionName = "transition_name_$adapterPosition"
                        albumsClickListener(cardView)
                    }
                }
            } else {
                SearchViewHolder(Picasso.get(), LayoutInflater.from(parent.context).inflate(R.layout.search_videos, parent, false)).apply {
                    videoFrame.setOnClickListener {
                        getItem(adapterPosition)?.let {
                            videoClickListener(it, videoFrame.apply { transitionName = "transition_name_$adapterPosition" })
                        }
                    }
                }
            }

    override fun getItemCount(): Int = when {
        super.getItemCount() == 0 && albums?.isNotEmpty() == true -> 1
        albums?.isNotEmpty() == true -> super.getItemCount() + 1
        else -> super.getItemCount()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProfileAlbumsSectorViewHolder) {
            albums?.let { holder.bind(it) }
        } else if (holder is SearchViewHolder) {
            getItem(if (albums != null) position - 1 else position)?.let { holder.bind(it) }
        }
    }
}