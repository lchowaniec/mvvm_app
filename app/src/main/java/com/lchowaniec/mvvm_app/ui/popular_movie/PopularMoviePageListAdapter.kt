package com.lchowaniec.mvvm_app.ui.popular_movie

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lchowaniec.mvvm_app.R
import com.lchowaniec.mvvm_app.data.api.POSTER_BASE_URL
import com.lchowaniec.mvvm_app.data.repository.NetworkState
import com.lchowaniec.mvvm_app.data.vo.movie
import com.lchowaniec.mvvm_app.ui.single_movie.SingleMovie
import kotlinx.android.synthetic.main.network_state_item.view.*
import kotlinx.android.synthetic.main.single_movie_item.view.*

class PopularMoviePageListAdapter(public val context: Context): PagedListAdapter<movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {
    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState : NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View
        if(viewType == MOVIE_VIEW_TYPE){
            view = layoutInflater.inflate(R.layout.single_movie_item, parent, false)
            return MovieItemViewHolder(view)
        }else{
                view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
                return NetworkStateItemViewHolder(view)

        }
    }
    fun hasExtraRow(): Boolean{
        return networkState!= null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if(hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount-1){
            NETWORK_VIEW_TYPE
        }else{
            MOVIE_VIEW_TYPE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == MOVIE_VIEW_TYPE){
            (holder as MovieItemViewHolder).bind(getItem(position),context)
        }else{
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }

    }
    class MovieDiffCallback: DiffUtil.ItemCallback<movie>(){
        override fun areItemsTheSame(oldItem: movie, newItem: movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: movie, newItem: movie): Boolean {
            return oldItem == newItem
        }

    }
    class MovieItemViewHolder(view: View): RecyclerView.ViewHolder(view){
        fun bind(movie:movie?, context: Context){
            itemView.item_movie_title.text = movie?.title
            itemView.item_movie_release_date.text = movie?.releaseDate
            val posterURL = POSTER_BASE_URL + movie?.posterPath
            Glide.with(itemView.context)
                .load(posterURL)
                .into(itemView.item_movie_poster)
            itemView.setOnClickListener{
                val intent = Intent(context, SingleMovie::class.java)
                intent.putExtra("id", movie?.id)
                context.startActivity(intent)

            }



        }
    }
    class NetworkStateItemViewHolder(view:View): RecyclerView.ViewHolder(view){
        fun bind(networkState: NetworkState?){
            if(networkState != null && networkState == NetworkState.LOADING){
                itemView.progress_bar.visibility = View.VISIBLE
            }else{
                itemView.progress_bar.visibility = View.GONE
            }

            if(networkState != null && networkState == NetworkState.ERROR){
                itemView.error_text.visibility = View.VISIBLE
                itemView.error_text.text = networkState.msg
            }
            else if(networkState != null && networkState == NetworkState.ENDLIST){
                itemView.error_text.visibility = View.VISIBLE
                itemView.error_text.text = networkState.msg
            }
            else{
                itemView.error_text.visibility = View.GONE
            }

        }
    }
    fun setNetworkState(newNetworkState:NetworkState){
        val previousState :NetworkState? = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if(hadExtraRow != hasExtraRow){
            if(hadExtraRow){
                notifyItemRemoved(super.getItemCount())
            }else{
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState){
            notifyItemChanged(itemCount-1)
        }
    }
}