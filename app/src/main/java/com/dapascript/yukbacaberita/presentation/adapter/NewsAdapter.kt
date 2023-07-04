package com.dapascript.yukbacaberita.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.dapascript.yukbacaberita.data.source.remote.model.ArticlesItem
import com.dapascript.yukbacaberita.databinding.ItemNewsBinding
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class NewsAdapter(
    private val onItemClick: ((ArticlesItem) -> Unit)? = null
) : ListAdapter<ArticlesItem, RecyclerView.ViewHolder>(NewsAdapter) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NewsViewHolder(
            ItemNewsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            (holder as NewsViewHolder).bind(item)

            holder.itemView.setOnClickListener {
                onItemClick?.invoke(item)
            }
        }
    }

    inner class NewsViewHolder(
        private val binding: ItemNewsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ArticlesItem) {
            binding.apply {
                val shimmer =
                    Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
                        .setDuration(1800) // how long the shimmering animation takes to do one full sweep
                        .setBaseAlpha(0.7f) //the alpha of the underlying children
                        .setHighlightAlpha(0.6f) // the shimmer alpha amount
                        .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                        .setAutoStart(true)
                        .build()

                val shimmerDrawable = ShimmerDrawable().apply {
                    setShimmer(shimmer)
                }

                Glide.with(itemView.context)
                    .load(item.urlToImage)
                    .transform(RoundedCorners(20)).apply(
                        RequestOptions.placeholderOf(shimmerDrawable)
                    )
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(ivNewsImage)

                tvNewsTitle.text = item.title
                tvNewsDesc.text = item.description
                tvNewsSource.text = item.source?.name
                tvNewsDate.text = item.publishedAt?.let { customFormatDate(it) }
                tvNewsAuthor.text = item.author
            }
        }
    }

    private fun customFormatDate(data: String): String {
        val formatInput = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val formatOutput = SimpleDateFormat("EEEE, d MMMM HH.mm", Locale.getDefault())
        formatOutput.timeZone = TimeZone.getDefault()

        val date = formatInput.parse(data)
        return formatOutput.format(date!!)
    }

    companion object : DiffUtil.ItemCallback<ArticlesItem>() {
        override fun areItemsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ArticlesItem, newItem: ArticlesItem): Boolean {
            return oldItem.url == newItem.url
        }
    }
}