package com.fidea.letter.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.fidea.letter.R
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_layout.view.*

class ItemsAdapter(
    var context: Context,
    private var itemsList: ArrayList<com.fidea.letter.models.Item>
) : RecyclerView.Adapter<ItemsAdapter.Item>() {

    private val clickSubject: PublishSubject<com.fidea.letter.models.Item> = PublishSubject
        .create<com.fidea.letter.models.Item>()

    fun getPositionClicks(): Observable<com.fidea.letter.models.Item>? {
        return clickSubject.hide()
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        clickSubject.onComplete() //here we avoid memory leaks
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item {
        val view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)

        return Item(view)
    }

    override fun getItemCount() = itemsList.size

    override fun onBindViewHolder(holder: Item, position: Int) {
        val item = itemsList[position]
        holder.itemView.title.text = item.title
        holder.itemView.description.text = item.description
        Glide.with(context).load(item.imageUrl).placeholder(R.drawable.placeholder)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?, model: Any, target: Target<Drawable?>,
                    dataSource: DataSource, isFirstResource: Boolean
                ): Boolean {
                    holder.itemView.shimmer.stopShimmer()
                    holder.itemView.shimmer.hideShimmer()
                    return false
                }
            }).centerCrop().into(holder.itemView.avatar)
        holder.itemView.setOnClickListener {
            clickSubject.onNext(item)
        }

    }

    class Item(itemView: View) : RecyclerView.ViewHolder(itemView)


}