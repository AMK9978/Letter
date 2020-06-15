package com.fidea.letter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fidea.letter.R
import com.fidea.letter.models.Board
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.board_layout.view.*

class BoardsAdapter(
    var context: Context,
    public var itemsList: ArrayList<Board>
) : RecyclerView.Adapter<BoardsAdapter.Item>() {

    private val clickSubject: PublishSubject<Board> = PublishSubject
        .create<Board>()

    fun getPositionClicks(): Observable<Board>? {
        return clickSubject.hide()
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        clickSubject.onComplete() //here we avoid memory leaks
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item {
        val view = LayoutInflater.from(context).inflate(R.layout.board_layout, parent, false)

        return Item(view)
    }

    override fun getItemCount() = itemsList.size

    override fun onBindViewHolder(holder: Item, position: Int) {
        val item = itemsList[position]
        holder.itemView.title.text = item.title
        Glide.with(context).load(item.imagePath).placeholder(R.drawable.placeholder)
            .centerCrop().into(holder.itemView.avatar)
        holder.itemView.setOnClickListener {
            clickSubject.onNext(item)
        }

    }

    class Item(itemView: View) : RecyclerView.ViewHolder(itemView)


}