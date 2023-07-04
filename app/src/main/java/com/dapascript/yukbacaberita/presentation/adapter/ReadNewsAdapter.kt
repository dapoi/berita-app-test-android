package com.dapascript.yukbacaberita.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.dapascript.yukbacaberita.data.source.local.model.NewsEntity
import com.dapascript.yukbacaberita.databinding.ItemNewsBinding
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable

class ReadNewsAdapter(
    private val onItemClick: ((NewsEntity) -> Unit)? = null
) : RecyclerView.Adapter<ReadNewsAdapter.ReadNewsViewHolder>() {

    private var getListReadNews = ArrayList<NewsEntity>()

    fun setListReadNews(listReadNews: List<NewsEntity>) {
        getListReadNews.clear()
        getListReadNews.addAll(listReadNews)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReadNewsViewHolder {
        return ReadNewsViewHolder(
            ItemNewsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ReadNewsViewHolder, position: Int) {
        holder.bind(getListReadNews[position])

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(getListReadNews[position])
        }
    }

    override fun getItemCount(): Int = getListReadNews.size

    inner class ReadNewsViewHolder(
        private val binding: ItemNewsBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NewsEntity) {
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
                    .load(item.image)
                    .transform(RoundedCorners(20)).apply(
                        RequestOptions.placeholderOf(shimmerDrawable)
                    )
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(ivNewsImage)

                tvNewsTitle.text = item.title
                tvNewsDesc.isVisible = false
                tvNewsAuthor.isVisible = false
                tvNewsDate.isVisible = false
                tvNewsSource.isVisible = false
            }
        }
    }
}